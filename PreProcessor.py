from imports import *
#import dask.array 

class PreProcessor:
    
    def __init__(self) -> None:
        # 1) All the interpunction was removed.
        #|||||||
        self.chars_to_remove = r"""(?x)
        \s*_+\s*|\s*;+\s*|\s*,+\s*|\s*\.+\s*| #Standalone Punctuation Marks
        \s*\+\s*|\s*-+\s*|\s*\/+\s*|\s*\+\s*|  #arethmatic operations
        \s*=+\s*|\s*!=+\s*|\s*>+\s*|\s*<+\s*|\s*&+\s*|
        \s*\|+\s* | \s*\*+\s*
        |\s*!+\s*|\s*\"+\s*|\s*\'+\s*|    #assignment 
        \s*\(+\s*|\s*\)+\s*|\s*\{+\s*|\s*\}+\s*|\s*\[+\s*|\s*\]+\s*|\s*@+\s*|\s*:+\s*|  # brackets
        \s*\+\s*|\s*\#+\s*|\s*\\n+\s*|\s*\%+\s*|\s*\^+\s*|\s*\~+\s*|\s*\?+\s*|\s*\\+\s*|\s*\$+\s*"""
        self.Vocabulary_frequenecy_dict = dict()
        self.stop_words = set(stopwords.words("english"))
        self.keywords_java = {
            "abstract",
            "continue",
            "for",
            "new",
            "switch",
            "assert",
            "default",
            "goto",
            "package",
            "synchronized",
            "boolean",
            "do",
            "if",
            "private",
            "this",
            "break",
            "double",
            "implements",
            "protected",
            "throw",
            "byte",
            "else",
            "import",
            "public",
            "throws",
            "case",
            "enum",
            "instanceof",
            "return",
            "transient",
            "catch",
            "extends",
            "int",
            "short",
            "try",
            "null",
            "char",
            "final",
            "interface",
            "static"
            "void",
            "class",
            "finally",
            "long",
            "strictfp",
            "volatile",
            "override",
            "deprecated",
            "safevarargs",
            "suppress",
            "warnings",
            "funtional",
            "inherited",
            "documented",
            "target",
            "retention",
            "repeatable",
            "list",
            "set",
            "array",
            "iterator",
            "linked",
            "hash",
            "map",
        }  # java non_primitive datatypes not added ex: "List"

        self.keywords_UC = {
            "Use",
            "case",
            "name",
            "Delete",
            "Actor",
            "Entry",
            "Operator",
            "conditions",
            "Flow",
            "events",
            "User",
            "System",
            ".",
            "Exit",
            "Quality",
            "Partecipating",
            "Actors",
            "Returns",
        }

        self.Vocab=dict()
        self.Vocab["</s>"] = 2

        # self.word_index=dict()
        # Language.build_library(
        # # # Store the library in the `build` directory
        # "build/my-languages.so",
        # # # Include one or more languages
        #  ["./tree-sitter-java"],
        #  )
        # JAVA = Language("build/my-languages.so", "java")
        JAVA_LANGUAGE = Language(tsjava.language())
        self.parser = Parser(JAVA_LANGUAGE)
        # self.parser = Parser()
        # self.parser.set_language(JAVA)

    def PreProcessor(self, file_content: str, UC_or_CC: str = 'UC',train_or_test: str ='train'):
        dataset_txt = file_content
        # 1) All the interpunction was removed.
        SourceCodeCleaned = re.split(self.chars_to_remove, dataset_txt)
        # 2) All numeric characters were removed.
        numeric_chars_to_remove = r"[0-9]"
        SourceCodeCleanedOfNumbers = [
            re.sub(numeric_chars_to_remove, "", x) for x in SourceCodeCleaned
        ]
        # 3) All sentences were tokenized with NLTK.
        # 4) The stop words corpus from NLTK was used to eliminate all stop words.
        words_tokenized = ""

        # 5) All remaining terms were stemmed using the Porter Stemming Algorithm
        porter_stemmer = PorterStemmer()

        # 6) remove keywords in java

        for sentence in SourceCodeCleanedOfNumbers:
            # 7) All words were lowercased.
            NTLKTokenized = word_tokenize(sentence)
            for word in NTLKTokenized:
                word = re.sub("\ufeff", "", word)
                word = re.sub("\u200b", "", word)
                
                check = False
                if UC_or_CC == 'UC' and word.lower() not in self.keywords_UC:
                    check = True
                elif UC_or_CC == 'CC' and word.lower() not in self.keywords_java:
                    check = True

                if (
                    word not in self.stop_words
                    and word != ""
                    and len(word) != 1
                    and check
                ):
                    split_words = re.sub(
                        r"(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])", " ", word
                    )
                    split_words_tokenized = word_tokenize(split_words)
                    for word_part in split_words_tokenized:
                        if len(word_part) == 1:
                            continue
                        word_lower = word_part.lower()
                        word_stem = porter_stemmer.stem(word_lower)
                        words_tokenized += word_stem + " "
                        if train_or_test == 'train':
                            self.Vocabulary_frequenecy_dict[word_stem] = self.Vocabulary_frequenecy_dict.get(word_stem, 0) + 1
        return words_tokenized

    # setup documents
    def setupCC(self, code_files: list, train_or_test:str)->tuple:
        all_tokens=set()
        code_documents = list()
        
        for i, file_content in enumerate(code_files):
            tokens = self.PreProcessor(file_content, 'CC',train_or_test)
            code_documents.append(tokens)

        for i, doc in enumerate(code_documents):
            tokens = doc.split()
            for j, token in enumerate(tokens):
                if self.Vocabulary_frequenecy_dict.get(token, 0) <= 1 and train_or_test == 'train':
                    tokens[j] = '__unk__'
                elif  tokens[j] not in self.Vocabulary_frequenecy_dict.keys() and train_or_test == 'test':
                    tokens[j] = '__unk__'

                all_tokens.add(tokens[j])
            code_documents[i] = ' '.join(tokens)
        return code_documents, all_tokens
    
    def setupUC(self, UC_files: list, train_or_test: str)->tuple:
        UC_documents = list()
        all_tokens=set()
        for i, file_content in enumerate(UC_files):
            tokens = self.PreProcessor(file_content, 'UC',train_or_test)
            UC_documents.append(tokens)
        for i, doc in enumerate(UC_documents):
            tokens = doc.split()
            for j, token in enumerate(tokens):
                if self.Vocabulary_frequenecy_dict.get(token, 0) <= 1 and train_or_test == 'train':
                    tokens[j] = '__unk__'
                elif  tokens[j] not in self.Vocabulary_frequenecy_dict.keys() and train_or_test == 'test':
                    tokens[j] = '__unk__'

                all_tokens.add(tokens[j])
            UC_documents[i] = ' '.join(tokens)
        return UC_documents, all_tokens
    
    
    def setupCSV(self, csv_dir: str, modified_csv_dir: str,UC_to_index,CC_to_index) -> None:
        filenames_CC = CC_to_index.keys()
        filenames_UC = UC_to_index.keys()
        
        DataSet = pd.read_csv(csv_dir, names=['UC', 'CC'], header=None)
        
        # file_to_index_list = []
        # for file in DataSet['CC'].str.lower():
        #     if file_index.get(file)==None:
        #         file_index[file]= "./dataset/teiid_dataset/cc/"+str(index)+".java"
        #     file_to_index_list.append(file_index.get(file))
        #     index+=1
        DataSet['UC'] = DataSet['UC'].astype(str) + ".txt"
        sum=0
        artifacts_done = set(zip(DataSet['UC'].str.lower(),DataSet['CC'].str.lower()))
        print("len(artifacts_done) = ", len(artifacts_done))
        test = set()
        artifacts_not_done = []
        for filename_UC in filenames_UC:
            for filename_CC in filenames_CC:
                if (filename_UC.lower(),filename_CC.lower()) not in artifacts_done:
                    random_number = random.randint(0, 50)
                    if random_number < 45:
                        continue
                    artifacts_not_done.append((UC_to_index[filename_UC.lower()], CC_to_index[filename_CC.lower()], 0))
                else:
                    artifacts_not_done.append((UC_to_index[filename_UC.lower()], CC_to_index[filename_CC.lower()], 1))
                    test.add((filename_UC.lower(), filename_CC.lower()))
                    sum+=1
        print("sum = ", sum)
        diff = artifacts_done.symmetric_difference(test)
        print(diff)
        dataset_modified = pd.DataFrame(artifacts_not_done, columns=['UC', 'CC', 'Labels'])
        dataset_modified.to_csv(modified_csv_dir, index = False)    


    def PreProcessorCCDeepLearning(self, file: str, train_test="train"):

        source_code = re.sub("\ufeff", "", file)
        source_code = re.sub("\u200b", "", source_code)
        src = bytes(
            source_code,
            "utf-8",
        )
        
        tree = self.parser.parse(src)
        curr_node = tree.root_node
        functions_names=list()
        functions_segments = list()
        curr_node = tree.root_node
        queue=list()
        queue.append(curr_node)
        porter_stemmer = PorterStemmer()
        numeric_chars_to_remove = r"[0-9]"

        while(len(queue)):
            curr_node = queue.pop(0)
            for child in curr_node.children:
                queue.append(child)
                if(child.type == "method_declaration" ):
                    method_name=source_code[child.children[2].start_byte:child.children[2].end_byte]
                    method_name = re.sub(self.chars_to_remove," " ,method_name)
                    split_words = re.sub(r"(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])|_+", " ", method_name).lower().split()
                    name_temp = []

                    for word in split_words:
                        if ( word not in self.stop_words and word != "" and len(word) != 1):
                            # 2) All numeric characters were removed.
                            word = re.sub(numeric_chars_to_remove, "", word)
                            word_stem = porter_stemmer.stem(word)
                            if train_test == "train":
                                self.Vocab[word_stem] = self.Vocab.get(word_stem, 0) + 1
                            name_temp.append(word_stem) #list of tokens in fucntion name 1 in file 1 
                    functions_names.append(name_temp) #append list for each file name for first file
                    # functions_names.append("</s>")

                elif (child.parent.type == "method_declaration" and child.type == "block"):
                    segment = source_code[child.start_byte:child.end_byte]
                    if (segment == ""):
                        continue
                    segmentCleaned = re.sub(self.chars_to_remove," " ,segment)
                    split_words = re.sub(r"(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])|_+", " ", segmentCleaned).lower().split()
                    function_temp = []
                    for word in split_words:
                        word = word.strip()
                        if ( word not in self.stop_words and word != "" and len(word) != 1 and word not in self.keywords_java):
                            # 2) All numeric characters were removed.
                            word = re.sub(numeric_chars_to_remove, "", word)
                            word_stem = porter_stemmer.stem(word)
                            if train_test == "train":
                                self.Vocab[word_stem] = self.Vocab.get(word_stem, 0) + 1
                            function_temp.append(word_stem) #list of tokens in fucntion 1 in file 1 
                    functions_segments.append(function_temp)#append list for each file segment for first file
                    # functions_segments.append("</s>")

        return functions_names,functions_segments
    
    def setupDeepLearning(self, CC_UC_files: list, CC_or_UC:str = 'CC' ,train_test="train")->tuple:
        
        # for CC: arg1 = function_names --> 3d array, arg2 = function_segments --> 3d array
        # for UC: arg1 = descriptions --> 2d, arg2 = summary --> 2d
        arg = list()        
        if CC_or_UC == 'CC':
            for file in CC_UC_files:
                for i,function in enumerate(file):
                    file[i] = self.PreProcessorCCDeepLearning(function,train_test).split()
                arg.append(file)
        else:
            for file in CC_UC_files: 
                file = self.PreProcessor(file,CC_or_UC,train_test)
                file_tokens = file.split()
                arg.append(file_tokens)
                      
        return arg

    def PreProcessorUCDeepLearning(self, file: str,train_test="train") -> tuple:
        porter_stemmer = PorterStemmer()
        numeric_chars_to_remove = r"[0-9]"

        source_code = file.split('\n')
        

        summary = source_code[0]
        summary = re.sub("\ufeff", "", summary)
        summary = re.sub("\u200b", "", summary)
        summary = re.sub(self.chars_to_remove," " ,summary)
        summary = re.sub(r"(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])", " ", summary)
        summary = re.sub(numeric_chars_to_remove, "", summary)
        

        description = source_code[1:]
        summary_tokenized = list()

        for token in summary.split():
            token_lower = token.lower()
            if token not in self.stop_words and token != "" and len(token) != 1:
                token_stem = porter_stemmer.stem(token_lower)
                if train_test == "train":
                    self.Vocab[token_stem] = self.Vocab.get(token_stem, 0) + 1
                summary_tokenized.append(token_stem)

        description_tokenized = list()
        for line in description:
            line = re.sub("\ufeff", "", line)
            line = re.sub("\u200b", "", line)
            line = re.sub(self.chars_to_remove," " ,line)
            line = re.sub( r"(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])", " ", line)
            line = re.sub(numeric_chars_to_remove, "", line)

            for token in line.split():
                token_lower = token.lower()
                if token_lower not in self.stop_words and token != "" and len(token) != 1:
                    split_words_tokenized = word_tokenize(token_lower)
                    for word in split_words_tokenized:
                        token_stem = porter_stemmer.stem(word)
                        if train_test == "train":
                            self.Vocab[token_stem] = self.Vocab.get(token_stem, 0) + 1
                        description_tokenized.append(token_stem)

        return description_tokenized, summary_tokenized

    def setUpUnknown (self,arg,train_test:str) -> None:

        # for CC: arg1 = function_names --> 3d array, arg2 = function_segments --> 3d array
        # for UC: arg1 = descriptions --> 2d, arg2 = summary --> 2d
            for i,file in enumerate(arg): # function_names or function_segments
                for j,token in enumerate(file):
                        if ( train_test == 'train' and  self.Vocabulary_frequenecy_dict[token] < 2):
                            arg[i][j] = "__unk__"
                        elif ( train_test == 'test' and  not self.Vocabulary_frequenecy_dict.get(token)):
                            arg[i][j] = "__unk__"

                            
    def vocabToIndex(self, vocab: dict):
        #convert each word in the vocan to index in order to map them later in the dataset
        self.word_index = {word: idx + 1 for idx, word in enumerate(vocab)}

    def dataSetToIndex(self, arg) -> None:
        # for CC: arg1 = function_names --> 3d array, arg2 = function_segments --> 3d array
        # for UC: arg1 = descriptions --> 2d, arg2 = summary --> 2d

        for i,file in enumerate(arg):
            for j,token in enumerate(file):
                if(self.word_index.get(token) != None):
                    arg[i][j]=self.word_index[token]
                else:
                    # In the case of unknown words
                    arg[i][j]= self.word_index['__unk__']
            # arg[i] = torch.tensor(arg[i], dtype=torch.int64)
    
                    
    def word2VecProcessor(self, arg1, arg2, UC_CC) -> list:
        # is used because the word2vec takes 2d array , and we want to give the word2vec name+segement 
        # output = both UC and CC are 1d array CC now contains all the functions regardless the file
        # output = in Case of UC the list now contains all UC files where each entry containg the summary concatented with the description 
        if UC_CC == 'CC':
            CC_docs = list()
            # names: [[<s>,'n1','n2'</s>,<s>,n11,n22,</s>],[<s> , n1' ,</s>,<s>'n2',n11,n22</s>],[n1','n2',n11,n22]]
            # segments : [[<s>,'nte1','ntre2'</s>,<s>,ngvd11,nvrcdxsz22,</s>],[<s> , nscd1' ,</s>,<s>'ncdd2',nfdsd11,nfdewe22</s>],[n1','n2',n11,n22]]

            for file_name, file_seg in zip(arg1, arg2): #msknaawel file fy kol whda

                '''
                Example for the 2nd zip
                [(['title1'], ['hi my name is', 'lol']), (['title2'], ['bassant'])]
                [(['title3'], ['hi my name is2']), (['title4'], ['bassant2'])]
                '''
                # [<s>,'n1','n2',</s>,<s>,n11,n22,</s>] ->file_name
                # [<s>,'nte1','ntre2'</s>,<s>,ngvd11,nvrcdxsz22,</s>] -> 
                # names_segmants_zipped = list(zip(file_name, file_seg))

                # file_name_joined = ' '.join(file_name).split('</s>') #['func1 in file1', ' func2 in file1', '']
                # file_seg_joined = ' '.join(file_seg).split('</s>') #['func1_seg in file1 ', ' func2_seg in file1', '']

                # name_seg_concatenated = list(zip(file_name_joined,file_seg_joined)) #[('func1 in file1','func1_seg in file1 ')]
                name_seg_tokens = list()

                # for name_seg in name_seg_concatenated:
                for name,seg in zip(file_name,file_seg): #mskn awel name w seg fy kol whda
                # [[file1:[funct1],[function2]] , [file2:[],[]]]
                    # name_seg_tokens = name_seg[0].split()+name_seg[1].split() #[func1 ,in ,file1,func1_seg ,in, file1 ]
                    temp = name + seg
                    if (len(temp)):
                        name_seg_tokens.extend(temp)
                CC_docs.append(name_seg_tokens) 
            return CC_docs
        
        elif UC_CC == 'UC':
            UC_docs = list()
            
            for summary, description in zip(arg1, arg2):
                UC_docs.append(summary+description)

        return UC_docs
    
    def setUpLabels(self,CC,UC,directory_csv):
        #[[functions<s>functions<s>]]
        #reading the csv and creating a list containing the UC and CC and their coresponding label
        Features = list()
        labels=list()

        DataSet_train = pd.read_csv(directory_csv)
        for row in DataSet_train.index:
           
            index_code = int(DataSet_train.loc[row, 'CC'])
            index_UC = int(DataSet_train.loc[row, 'UC'])
            label = int(DataSet_train.loc[row, 'Labels'])

                
            UC_segmented = UC[index_UC][0:1000]
            CC_segmented = CC[index_code][0:4000]
            Features.append([CC_segmented,UC_segmented])
            labels.append(label)

            if (len(CC[index_code]) > 4000):
                Features.append([CC[index_code][4000:8000],UC_segmented])
                labels.append(label)
            
        return Features,labels
    
    def splitToFunctions(self,CC_docs):

        list_of_docs = list()
        
        for doc in CC_docs:
            list_of_methods = list()
            src_new = bytes(doc,"utf-8")
            tree_new = self.parser.parse(src_new)
            curr_node_new = tree_new.root_node
            queue_new=list()
            queue_new.append(curr_node_new)

            
            while(len(queue_new)):
                curr_node = queue_new.pop(0)
                for child in curr_node.children:
                    queue_new.append(child)
                    if(child.parent.type == "method_declaration" and child.type == "block"):
                        segment = doc[child.start_byte:child.end_byte]
                        method_name=doc[child.parent.children[2].start_byte:child.parent.children[2].end_byte]
                        list_of_methods.append(method_name+segment)
            if len(list_of_methods) != 0:
                list_of_docs.append(list_of_methods)

        return list_of_docs