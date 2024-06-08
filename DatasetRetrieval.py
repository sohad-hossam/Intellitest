from imports import * 

class DatasetRetrieval:
    
    def __init__(self) -> None:
        pass

    def parseSqlite(self, sqlite_path: str):
        con = sqlite3.connect(sqlite_path)
        cur = con.cursor()

        issue_df = pd.read_sql_query("SELECT issue_id, type, resolution, summary, description FROM issue WHERE resolution='Done' AND description is not NULL", con)
        change_set_link_df = pd.read_sql_query("SELECT issue_id, commit_hash FROM change_set_link", con)
        change_set_df = pd.read_sql_query("SELECT commit_hash, file_path FROM code_change WHERE file_path LIKE '%.java'", con)

        merged_df = pd.merge(pd.merge(change_set_link_df, change_set_df, on='commit_hash'), issue_df, on='issue_id')
        merged_df = merged_df.drop(columns=['issue_id'])

        return merged_df

    def getGlobalDatasetData(self, merged_df, repo_clone_path:str) -> tuple:
        token = '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'

        headers = {
            'Authorization': f'token {token}'
        }

        repo_local = git.Repo(repo_clone_path)

        CC_UC_dict_true = dict()
        UC_to_index = dict()
        index_to_UC = dict()
        CC_index_dict = dict()
        index_to_CC = dict()


        counter = 0
        UC_index = 0
        CC_index = 0

        merged_list = list(merged_df.to_records(index=False))
        for commit_hash, file_path, type, resolution, summary, description in merged_list:

            commit_local = repo_local.commit(commit_hash) 
            commit_parent_hash = commit_local.parents[0].hexsha
            commit_parent = repo_local.commit(commit_parent_hash)

            try: 
                UC = summary+'\n'+description
                if UC_to_index.get(UC) == None:
                    UC_to_index[UC] = UC_index
                    index_to_UC[UC_index] = UC
                    UC_index += 1

                if type == 'Bug':

                    old_content = commit_parent.tree[file_path].data_stream.read().decode('utf-8')
                    new_content = commit_local.tree[file_path].data_stream.read().decode('utf-8')
                    if CC_index_dict.get(old_content) == None:
                        CC_index_dict[old_content] = CC_index
                        index_to_CC[CC_index] = old_content
                        CC_UC_dict_true[CC_index] = UC_to_index.get(UC)
                        CC_index += 1
                else:
                    new_content = commit_local.tree[file_path].data_stream.read().decode('utf-8')
                    if CC_index_dict.get(new_content) == None:
                        CC_index_dict[new_content] = CC_index
                        index_to_CC[CC_index] = new_content
                        CC_UC_dict_true[CC_index] = UC_to_index.get(UC)
                        CC_index += 1

            except:
                counter+=1
        return CC_UC_dict_true, UC_to_index, index_to_UC, CC_index_dict, index_to_CC
    
    def splitTestTrain(self, CC_UC_dict_true: dict, UC_to_index: dict, index_to_UC: dict, CC_index_dict: dict, index_to_CC: dict) -> tuple:
        UC_train, UC_test = train_test_split(list(UC_to_index.values()), test_size = 0.2, random_state = 42)

        CC_train_docs = list()
        UC_train_docs = list()

        CC_test_docs = list()
        UC_test_docs = list()

        UC_to_index_train_new = dict()
        CC_to_index_train_new = dict()
        UC_to_index_test_new = dict()
        CC_to_index_test_new = dict()

        UC_index_train = 0
        CC_index_train = 0
        UC_index_test = 0
        CC_index_test = 0

        for CC_index_old, UC_index_old in CC_UC_dict_true.items():

            if UC_index_old in UC_train:
                if UC_to_index_train_new.get(UC_index_old) == None:
                    UC_to_index_train_new[UC_index_old] = UC_index_train
                    UC_index_train += 1
                if CC_to_index_train_new.get(CC_index_old) == None:
                    CC_to_index_train_new[CC_index_old] = CC_index_train
                    CC_index_train += 1

                if index_to_CC[CC_index_old] not in CC_train_docs:
                    CC_train_docs.append(index_to_CC[CC_index_old])
                if index_to_UC[UC_index_old] not in UC_train_docs:
                    UC_train_docs.append(index_to_UC[UC_index_old])

                
                
            elif UC_index_old in UC_test:
                if UC_to_index_test_new.get(UC_index_old) == None:
                    UC_to_index_test_new[UC_index_old] = UC_index_test
                    UC_index_test += 1
                if CC_to_index_test_new.get(CC_index_old) == None: 
                    CC_to_index_test_new[CC_index_old] = CC_index_test
                    CC_index_test += 1

                if index_to_CC[CC_index_old] not in CC_test_docs:
                    CC_test_docs.append(index_to_CC[CC_index_old])
                if index_to_UC[UC_index_old] not in UC_test_docs:
                    UC_test_docs.append(index_to_UC[UC_index_old])
                
                
        # to be used for the mapping we took the global dictionary and split it into train and test based on 
        # split results to do that we mapped the old UC and CC indices to new ones based on train and test

        CC_UC_train = list()
        CC_UC_test = list()

        for CC_index_old, UC_index_old in CC_UC_dict_true.items():
            if UC_index_old in UC_to_index_train_new.keys():
                CC_UC_train.append((CC_to_index_train_new[CC_index_old], UC_to_index_train_new[UC_index_old]))
            elif CC_index_old in CC_to_index_test_new.keys():
                CC_UC_test.append((CC_to_index_test_new[CC_index_old], UC_to_index_test_new[UC_index_old]))

        true_train_length = len(CC_UC_train)
        true_test_length = len(CC_UC_test)

        CC_UC_train_labels = [1] * true_train_length
        CC_UC_test_labels = [1] * true_test_length

        for CC_index_new in CC_to_index_train_new.values():
            random_UC_index_new = random.choice(list(UC_to_index_train_new.values()))
            while (CC_index_new, random_UC_index_new) in CC_UC_train:
                random_UC_index_new = random.choice(list(UC_to_index_train_new.values()))
            CC_UC_train.append((CC_index_new, random_UC_index_new))

        for CC_index_new in CC_to_index_test_new.values():
            random_UC_index_new = random.choice(list(UC_to_index_test_new.values()))
            while (CC_index_new, random_UC_index_new) in CC_UC_train:
                random_UC_index_new = random.choice(list(UC_to_index_test_new.values()))
            CC_UC_test.append((CC_index_new, random_UC_index_new))

        CC_UC_train_labels += [0] * (len(CC_UC_train) - true_train_length)
        CC_UC_test_labels += [0] * (len(CC_UC_test) - true_test_length)

        return CC_train_docs, UC_train_docs, CC_test_docs, UC_test_docs, CC_UC_train, CC_UC_test, CC_UC_train_labels, CC_UC_test_labels
    
    def setupCSV(self, CC_UC: list, CC_UC_labels: list, out_dir: str):
        CC, UC = list(zip(*CC_UC))
        train_csv = {
            'CC': CC,
            'UC': UC,
            'Labels': CC_UC_labels
        }

        train_df = pd.DataFrame(train_csv)
        train_df.to_csv(out_dir, index=False)
    
    def findChangedFunctions(self,old_content,new_content,summary,description):
        JAVA_LANGUAGE = Language(tsjava.language())
        parser = Parser(JAVA_LANGUAGE)

        # JAVA = Language("build/my-languages.so", "java")
        # parser = Parser()
        # parser.set_language(JAVA)

        data = list()

        src_new = bytes(
        new_content,
        "utf-8",
        )
        src_old = bytes(
        old_content,
        "utf-8",
        )

        tree_new = parser.parse(src_new)
        curr_node_new = tree_new.root_node
        queue_new=list()
        queue_new.append(curr_node_new)

        tree_old = parser.parse(src_old)
        curr_node_old = tree_old.root_node
        queue_old=list()
        queue_old.append(curr_node_old)

        method_dict = dict()
        
        while(len(queue_new)):
            curr_node = queue_new.pop(0)
            for child in curr_node.children:
                queue_new.append(child)
                if(child.parent.type == "method_declaration" and child.type == "block"):
                    segment = new_content[child.start_byte:child.end_byte]
                    method_name=new_content[child.parent.children[2].start_byte:child.parent.children[2].end_byte]
                    method_dict[method_name] = segment

                    
        while(len(queue_old)):
            curr_node = queue_old.pop(0)
            for child in curr_node.children:
                queue_old.append(child)
                if(child.parent.type == "method_declaration" and child.type == "block"):
                    segment = old_content[child.start_byte:child.end_byte]
                    method_name=old_content[child.parent.children[2].start_byte:child.parent.children[2].end_byte]
                    
                    # we put the functions that were deleted in the new commit but not the function that was added
                    # as the deleted func isa buggy one but the added one is not a buggy one

                    if (method_dict.get(method_name) != None and method_dict[method_name] != segment) or method_dict.get(method_name) == None:
                        data.append([method_name+segment,summary+description,1])
                    elif random.randint(1, 3) == 3: #just a dummy number
                        data.append([method_name+segment,summary+description,0])
