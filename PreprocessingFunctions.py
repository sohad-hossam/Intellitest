from imports import *
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
        self.word_index=dict()
        self.Vocab=dict()


def PreProcessorFuncDeepLearning(self, source_code: str,train_test="train") -> tuple:
        porter_stemmer = PorterStemmer()
        numeric_chars_to_remove = r"[0-9]"

        source_code = re.sub("\ufeff", "", source_code)
        source_code = re.sub("\u200b", "", source_code)
        source_code = re.sub(self.chars_to_remove," " ,source_code)
        source_code = re.sub(r"(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])", " ", source_code)
        source_code = re.sub(numeric_chars_to_remove, "", source_code)
        

        method_tokenized = list()

        for line in source_code:
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
                        method_tokenized.append(token_stem)
                            
        return method_tokenized
def setUpUnknown (self,arg1,arg2,CC_UC:str) -> None:

        # for CC: arg1 = function_names --> 3d array, arg2 = function_segments --> 3d array
        # for UC: arg1 = descriptions --> 2d, arg2 = summary --> 2d
        # if CC_UC == "CC":
        #     for arg in [arg1,arg2]:
        #         for i,file in enumerate(arg):
        #             for j,name_list in enumerate(file):
        #                 for k,name_token in enumerate(name_list):
        #                     if ( not self.Vocab.get(name_token) or  self.Vocab[name_token] < 2):
        #                         arg[i][j][k] = "__unk__"
        # else:
            for i,function in enumerate(arg1):
                for j,func_token in enumerate(function):
                    if ( not self.Vocab.get(func_token) or  self.Vocab[func_token] < 2):
                        arg1[i][j]="__unk__"

def vocabToIndex(self, vocab: dict):
        #convert each word in the vocan to index in order to map them later in the dataset
    self.word_index = {word: idx + 1 for idx, word in enumerate(vocab)}

def dataSetToIndex(self, arg1, arg2, UC_CC) -> None:
        for i,func in enumerate(arg1):
            for j,token in enumerate(func):
                if(self.word_index.get(token) != None):
                    arg1[i][j]=self.word_index[token]
                else:
                    # In the case of unknown words
                    arg1[i][j]= self.word_index["__unk__"]
            arg1[i]= torch.tensor(arg1[i], dtype=torch.int64)


def setUpLabels(self,function_names_train,function_segments_train,descriptions_train,summaries_train,directory_csv
 ):
#[[functions<s>functions<s>]]
        #reading the csv and creating a list containing the UC and CC and their coresponding label
        Features = list()
        labels=list()

        DataSet_train = pd.read_csv(directory_csv)
        for row in DataSet_train.index:
           
            index_code = int(DataSet_train.loc[row, 'CC'])
            index_UC = int(DataSet_train.loc[row, 'UC'])
            label = int(DataSet_train.loc[row, 'Labels'])
           
            if len(function_names_train[index_code]) != 0 and len(function_segments_train[index_code]) != 0 and len(descriptions_train[index_UC]) != 0 and len(summaries_train[index_UC]) != 0:
                
                description = descriptions_train[index_UC]
                summaries = summaries_train[index_UC]
                
                if len(descriptions_train[index_UC]) > 200 :
                    description = descriptions_train[index_UC][0:200]
                if len(summaries_train[index_UC]) > 200 :
                    summaries = summaries_train[index_UC][0:200]

                for name,function in zip(function_names_train[index_code],function_segments_train[index_code]):
                   
                    if len(name) !=0 and len(function)!=0: 
                        Features.append([name,function,description,summaries])
                        labels.append(label)
            

        return Features,labels