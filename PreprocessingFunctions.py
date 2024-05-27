from imports import *
class PreProcessorFunctions:
    
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


    def PreProcessorFuncDeepLearning(self, source_code : str ,train_test = "train") -> list:
        porter_stemmer = PorterStemmer()
        numeric_chars_to_remove = r"[0-9]"

        method_tokenized = list()
        source_code = re.sub("\ufeff", "", source_code)
        source_code = re.sub("\u200b", "", source_code)
        source_code = re.sub(self.chars_to_remove," " ,source_code)
        source_code = re.sub( r"(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])", " ", source_code)
        source_code = re.sub(numeric_chars_to_remove, "", source_code)

        for token in source_code.split():
            token_lower = token.lower()
            if token_lower not in self.stop_words and token != "" and len(token) != 1:
                split_words_tokenized = word_tokenize(token_lower)
                for word in split_words_tokenized:
                    token_stem = porter_stemmer.stem(word)
                    if train_test == "train":
                        self.Vocab[token_stem] = self.Vocab.get(token_stem, 0) + 1
                    method_tokenized.append(token_stem)
                            
        return method_tokenized

    def setUpUnknown (self,arg1 : list,train_test : str) -> None:

        for i,row in enumerate(arg1):
            for j,arg in enumerate(row):
                for k,token in enumerate(arg):
                    if ( train_test == 'train' and  self.Vocab[token] < 2):
                        arg1[i][j][k] = "__unk__"
                    elif ( train_test == 'test' and  not self.Vocab.get(token)):
                        arg1[i][j][k] = "__unk__"

    def vocabToIndex(self, vocab: dict):
        #convert each word in the vocan to index in order to map them later in the dataset
        self.word_index = {word: idx + 1 for idx, word in enumerate(vocab)}

    def dataSetToIndex(self, arg1) -> None:
        for i,row in enumerate(arg1):
            for j,arg in enumerate(row):
                for k,token in enumerate(arg):
                    if(self.word_index.get(token) != None):
                        arg1[i][j][k]=self.word_index[token]
                    else:
                        # In the case of unknown words
                        arg1[i][j][k]= self.word_index["__unk__"]
                #arg1[i]= torch.tensor(arg1[i], dtype=torch.int64)


