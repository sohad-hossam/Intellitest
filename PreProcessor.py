from imports import *
import os
#import dask.array 

class PreProcessor:
    
    def __init__(self) -> None:
        # 1) All the interpunction was removed.
        #|||||||
        self.chars_to_remove = r"""(?x)
        \s*_+\s*|\s*;+\s*|\s*,+\s*|\s*\.+\s*| #Standalone Punctuation Marks
        \s*\+\s*|\s*-+\s*|\s*\/+\s*|\s*\+\s*|  #arethmatic operations
        \s*=+\s*|\s*!=+\s*|\s*>+\s*|\s*<+\s*|\s*&+\s*|
        \s*\|+\s*
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

        self.UC_to_index = dict()
        self.CC_to_index = dict()
        self.Vocab=dict()
        self.word_index=dict()
        Language.build_library(
        # Store the library in the `build` directory
        "build/my-languages.so",
        # Include one or more languages
        ["../tree-sitter-java"],
        )

        JAVA = Language("build/my-languages.so", "java")
        self.parser = Parser()
        self.parser.set_language(JAVA)

    def PreProcessor(self, filepath, UC_or_CC: str = 'UC',train_or_test: str ='train'):
        dataset_txt = open(filepath, "r", encoding="utf-8").read()
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
    def setupCC(self, code_path: str, train_or_test:str)->tuple:
        all_tokens=set()
        code_documents = list()
        code_file_index = 0
        file_stack = list()
        file_stack.append(code_path)
        while len(file_stack) > 0:
            filepath = file_stack.pop()
            for filename in os.listdir(filepath):
                filepath_integrated = filepath+"/"+filename
                if os.path.isdir(filepath_integrated):
                    file_stack.append(filepath_integrated)
                elif filename.endswith(".java"):
                    tokens = self.PreProcessor(filepath_integrated, 'CC',train_or_test)
                    code_documents.append(tokens)
                    filepath_integrated.replace(code_path, "")
                    self.CC_to_index[filepath_integrated.lower()] = code_file_index
                    code_file_index += 1
        for i, doc in enumerate(code_documents):
            tokens = doc.split()
            for j, token in enumerate(tokens):
                if self.Vocabulary_frequenecy_dict.get(token, 0) <= 1 and train_or_test == 'train':
                    tokens[j] = '__unk__'
                elif  tokens[j] not in self.Vocabulary_frequenecy_dict.keys() and train_or_test == 'test':
                    tokens[j] = '__unk__'

                all_tokens.add(tokens[j])
            code_documents[i] = ' '.join(tokens)
        return code_documents,self.CC_to_index,all_tokens
    
    def setupUC(self, UC_path: str, train_or_test: str)->tuple:
        UC_documents = list()
        all_tokens=set()
        for i, filename in enumerate(os.listdir(UC_path)):
            self.UC_to_index[filename.lower()] = i
            filepath = os.path.join(UC_path, filename)
            tokens = self.PreProcessor(filepath, 'UC',train_or_test)
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
        return UC_documents, self.UC_to_index,all_tokens
    
    
    def setupCSV(self, csv_dir: str, modified_csv_dir: str,UC_to_index,CC_to_index) -> None:
        filenames_CC = CC_to_index.keys()
        filenames_UC = UC_to_index.keys()
        
        DataSet = pd.read_csv(csv_dir, names=['UC', 'CC'])
        
        # file_to_index_list = []
        # for file in DataSet['CC'].str.lower():
        #     if file_index.get(file)==None:
        #         file_index[file]= "./dataset/teiid_dataset/cc/"+str(index)+".java"
        #     file_to_index_list.append(file_index.get(file))
        #     index+=1
        DataSet['UC'] = DataSet['UC'].astype(str) + ".txt"
        sum=0
        artifacts_done = set(zip(DataSet['UC'].str.lower(),DataSet['CC'].str.lower()))
        artifacts_not_done = []
        for filename_UC in filenames_UC:
            for filename_CC in filenames_CC:
                if filename_CC.endswith('.java'):
                    if (filename_UC.lower(),filename_CC.lower()) not in artifacts_done:
                        artifacts_not_done.append((UC_to_index[filename_UC.lower()], CC_to_index[filename_CC.lower()], 0))
                    else:
                        artifacts_not_done.append((UC_to_index[filename_UC.lower()], CC_to_index[filename_CC.lower()], 1))
                        sum+=1
        print("sum = ", sum)
        dataset_modified = pd.DataFrame(artifacts_not_done, columns=['UC', 'CC', 'Labels'])
        dataset_modified.to_csv(modified_csv_dir, index = False)    


    def PreProcessorCCDeepLearning(self, filepath,train_test="train"):

        with open(filepath, "r", encoding='utf-8') as f:
            source_code = f.read()
            source_code = re.sub("\ufeff", "", source_code)
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
                    temp_function_names=list()

                    for word in split_words:
                        if ( word not in self.stop_words and word != "" and len(word) != 1):
                            # 2) All numeric characters were removed.
                            word = re.sub(numeric_chars_to_remove, "", word)
                            word_stem = porter_stemmer.stem(word)
                            if train_test == "train":
                                self.Vocab[word_stem] = self.Vocab.get(word_stem, 0) + 1
                            temp_function_names.append(word_stem)

                    functions_names.append(temp_function_names)

                elif (child.parent.type == "method_declaration" and child.type == "block"):
                    segment = source_code[child.start_byte:child.end_byte]
                    segmentCleaned = re.sub(self.chars_to_remove," " ,segment)
                    split_words = re.sub(r"(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])|_+", " ", segmentCleaned).lower().split()
                    temp_function_segments=list()

                    for word in split_words:
                        word = word.strip()
                        if ( word not in self.stop_words and word != "" and len(word) != 1 and word not in self.keywords_java):
                            # 2) All numeric characters were removed.
                            word = re.sub(numeric_chars_to_remove, "", word)
                            word_stem = porter_stemmer.stem(word)
                            if train_test == "train":
                                self.Vocab[word_stem] = self.Vocab.get(word_stem, 0) + 1
                            temp_function_segments.append(word_stem)

                    functions_segments.append(temp_function_segments)

        return functions_names,functions_segments
    
    def setupDeepLearning(self, code_path: str,CC_or_UC:str = 'CC' ,train_test="train")->tuple:
        
        # for CC: arg1 = function_names --> 3d array, arg2 = function_segments --> 3d array
        # for UC: arg1 = descriptions --> 2d, arg2 = summary --> 2d
        arg1 = list()
        arg2 = list()
        
        file_stack = list()
        file_stack.append(code_path)
        while len(file_stack) > 0:
            filepath = file_stack.pop()
            for filename in os.listdir(filepath):
                filepath_integrated = filepath+"/"+filename
                if CC_or_UC == 'CC':
                    if os.path.isdir(filepath_integrated):
                        file_stack.append(filepath_integrated)
                    elif filename.endswith(".java"):
                        function_names,function_segments = self.PreProcessorCCDeepLearning(filepath_integrated,train_test)

                        arg1.append(function_names)
                        arg2.append(function_segments)
                elif CC_or_UC == 'UC':
                    if os.path.isdir(filepath_integrated):
                        file_stack.append(filepath_integrated)
                    else:
                        description, summary = self.PreProcessorUCDeepLearning(filepath_integrated,train_test)

                        arg1.append(description)
                        arg2.append(summary)
                      
        return arg1,arg2

    def PreProcessorUCDeepLearning(self, filename: str,train_test="train"):
        porter_stemmer = PorterStemmer()
        numeric_chars_to_remove = r"[0-9]"

        with open(filename, "r", encoding='utf-8') as f:
            source_code = f.readlines()

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

    def setUpUnknown (self,arg1,arg2,UC_CC):

        # for CC: arg1 = function_names --> 3d array, arg2 = function_segments --> 3d array
        # for UC: arg1 = descriptions --> 2d, arg2 = summary --> 2d
        if UC_CC == 'CC':
            for i,file in enumerate(arg1):
                for j,name in enumerate(file):
                    for k,name_part in enumerate(name):
                        if ( not self.Vocab.get(name_part) or  self.Vocab[name_part] < 2):
                            arg1[i][j][k]="__unk__"
                            # print("entered1")

            for i,file in enumerate(arg2):
                for j,name in enumerate(file):
                    for k,name_part in enumerate(name):
                        if (not self.Vocab.get(name_part) or self.Vocab[name_part] < 2):
                            arg2[i][j][k]="__unk__"
                            # print("entered2")
        else:
            for i,file in enumerate(arg1):
                for j,name in enumerate(file):
                      if (not self.Vocab.get(name) or self.Vocab[name] < 2):
                            arg1[i][j]="__unk__"    
                            # print("entered3")       

            for i,file in enumerate(arg2):
                for j,name in enumerate(file):
                    if (not self.Vocab.get(name) or self.Vocab[name] < 2):
                            arg2[i][j]="__unk__"  
                            # print("entered4")   
    def vocabToIndex(self):
        #convert each word in the vocan to index in order to map them later in the dataset
        self.word_index = {word: idx + 1 for idx, word in enumerate(self.Vocab.keys())}

    def dataSetToIndex(self,arg1,arg2,UC_CC):
        # for CC: arg1 = function_names --> 3d array, arg2 = function_segments --> 3d array
        # for UC: arg1 = descriptions --> 2d, arg2 = summary --> 2d
        if UC_CC == 'CC':
            for i,file in enumerate(arg1):
                for j,name in enumerate(file):
                    for k,name_part in enumerate(name):
                        if(self.word_index.get(name_part) != None):
                            arg1[i][j][k]=self.word_index[name_part]
                        else:
                            # In the case of unknown words
                            arg1[i][j][k] = len(self.word_index.keys()) + 1
                    arg1[i][j] = torch.tensor(arg1[i][j])

            for i,file in enumerate(arg2):
                for j,name in enumerate(file):
                    for k,name_part in enumerate(name):
                        if(self.word_index.get(name_part) != None):
                            arg2[i][j][k]=self.word_index[name_part]
                        else:
                            # In the case of unknown words
                            arg2[i][j][k] = len(self.word_index.keys()) + 1
                    arg2[i][j] = torch.tensor(arg2[i][j])

        else:
            for i,file in enumerate(arg1):
                for j,name in enumerate(file):
                    if(self.word_index.get(name) != None):
                        arg1[i][j]=self.word_index[name]
                    else:
                        # In the case of unknown words
                       arg1[i][j]= len(self.word_index.keys()) + 1  
                arg1[i] = torch.tensor(arg1[i])

            for i,file in enumerate(arg2):
                for j,name in enumerate(file):
                    if(self.word_index.get(name) != None):
                        arg2[i][j] = self.word_index[name]
                    else:
                        # In the case of unknown words
                        arg2[i][j] = len(self.word_index.keys()) + 1   
                arg2[i] = torch.tensor(arg2[i])
                            
    
    def setUpLabels(self,function_names_train,function_segments_train,descriptions_train,summaries_train):
        #reading the csv and creating a list containing the UC and CC and their coresponding label
        Features = list()
        labels=list()
        DataSet_train = pd.read_csv('Dataset/teiid_dataset/train_modified.csv')
        for row in DataSet_train.index:
            index_code = int(DataSet_train.loc[row, 'CC'])
            index_UC = int(DataSet_train.loc[row, 'UC'])
            label = int(DataSet_train.loc[row, 'Labels'])
            Features.append([function_names_train[index_code],function_segments_train[index_code],descriptions_train[index_UC],summaries_train[index_UC]])
            labels.append(label)
        return Features,labels


