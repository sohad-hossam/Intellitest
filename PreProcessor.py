from imports import *
import os
#import dask.array 

class PreProcessor:
    Vocabulary_frequenecy_dict = dict()
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
        \s*\+\s*|\s*\#+\s*|\s*\\n+\s*|\s*\%+\s*|\s*\^+\s*|\s*\~+\s*|\s*\?+\s*|\s*\\+\s*"""
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
            "static",
            "void",
            "class",
            "finally",
            "long",
            "strictfp",
            "volatile",
            "Override",
            "Deprecated",
            "SafeVarArgs",
            "SuppressWarnings",
            "FuntionalInterface",
            "Inherited",
            "Documented",
            "Target",
            "Retention",
            "Repeatable",
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

    def CodePreProcessor(self, filepath):
       
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
                if (
                    word not in self.stop_words
                    and word != ""
                    and word.lower() not in self.keywords_java
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
                        self.Vocabulary_frequenecy_dict[word_stem] = self.Vocabulary_frequenecy_dict.get(word_stem, 0) + 1
        return words_tokenized

    def UCPreProcessor(self, filepath):
        dataset_txt = open(filepath, "r", encoding="utf-8").read()
         # 1) All the interpunction was removed.
        UCCleaned = re.split(self.chars_to_remove, dataset_txt)
        # 2) All numeric characters were removed.
        numeric_chars_to_remove = r"[0-9]"
        UCCleanedOfNumbers = [
            re.sub(numeric_chars_to_remove, "", x) for x in UCCleaned
        ]

        # 3) All sentences were tokenized with NLTK.
        # 4) The stop words corpus from NLTK was used to eliminate all stop words.
        stop_words = set(stopwords.words("english"))
        words_tokenized = ""

        # 5) All remaining terms were stemmed using the Porter Stemming Algorithm
        porter_stemmer = PorterStemmer()

        # 6) remove keywords in java
        for sentence in UCCleanedOfNumbers:
            # 7) All words were lowercased.
            NTLKTokenized = word_tokenize(sentence)
            for word in NTLKTokenized:
                if (
                    word not in stop_words
                    and word != ""
                    and len(word) != 1
                    and word not in self.keywords_UC
                ):
                    word_lower = word.lower()
                    word_stem = porter_stemmer.stem(word_lower)
                    words_tokenized += word_stem + " "
                    self.Vocabulary_frequenecy_dict[word_stem] = self.Vocabulary_frequenecy_dict.get(word_stem, 0) + 1
        return words_tokenized

    # setup documents
    def setupCC(self, code_path: str)->tuple:
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
                    
                    tokens = self.CodePreProcessor(filepath_integrated)
                    code_documents.append(tokens)
                    filepath_integrated.replace(code_path, "")
                    self.CC_to_index[filepath_integrated.lower()] = code_file_index
                    code_file_index += 1
        for i, doc in enumerate(code_documents):
            tokens = doc.split()
            for j, token in enumerate(tokens):
                if self.Vocabulary_frequenecy_dict.get(token, 0) <= 1:
                    tokens[j] = '<unk>'
                all_tokens.add(tokens[j])
            code_documents[i] = ' '.join(tokens)
        return code_documents,self.CC_to_index,all_tokens
    
    def setupUC(self, UC_path: str)->tuple:
        UC_documents = list()
        all_tokens=set()
        for i, filename in enumerate(os.listdir(UC_path)):
            self.UC_to_index[filename.lower()] = i
            filepath = os.path.join(UC_path, filename)
            tokens = self.UCPreProcessor(filepath)
            UC_documents.append(tokens)
        for i, doc in enumerate(UC_documents):
            tokens = doc.split()
            for j, token in enumerate(tokens):
                if self.Vocabulary_frequenecy_dict.get(token, 0) <= 1:
                    tokens[j] = '<unk>'
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
         