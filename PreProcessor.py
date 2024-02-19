# from treelib import Node, Tree
# from tree_sitter import Language, Parser
import math
import re
import math
from nltk.corpus import stopwords
import nltk
#nltk.download() #download_folder = C:/nltk_data 
from nltk.stem import PorterStemmer
from nltk.tokenize import word_tokenize

dataset_txt = open("operation1.java", "r", encoding='utf-8').read()

# 1) All the interpunction was removed. 
chars_to_remove = r'''(?x)
_|\s*;\s*|\s*,\s*|\s*\.\s*|  #Standalone Punctuation Marks
\s*\+\s*|\s*-\s*|\s*\/\s*|\s*\*\s*|  #arethmatic operations
\s*=\s*|\s*==\s*|\s*!=\s*|\s*>\s*|\s*>=\s*|\s*<\s*|\s*<=\s*|\s*&&\s*|
\s*\|\|\s*
|\s*!\s*|\s*\"\s*|\s*\'\s*|    #assignment 
\s*\(\s*|\s*\)\s*|\s*{\s*|\s*}\s*|\s*\[\s*|\s*\]\s*|\s*@\s*|\s*:\s*'''   #brackets
SourceCodeCleaned =re.split(chars_to_remove,dataset_txt)

#print(SourceCodeCleaned)


#print(SourceCodeLower)

# 2) All numeric characters were removed. 
numeric_chars_to_remove = r"[0-9]" 
SourceCodeCleanedOfNumbers = [re.sub(numeric_chars_to_remove, "", x) for x in SourceCodeCleaned]
#print(SourceCodeCleanedOfNumbers)
#3) All sentences were tokenized with NLTK. 
#4) The stop words corpus from NLTK was used to eliminate all stop words. 
stop_words = set(stopwords.words('english'))
words_tokenized=list()

#5) All remaining terms were stemmed using the Porter Stemming Algorithm
porter_stemmer = PorterStemmer()

#6) remove keywords in java
keywords_java={
    "abstract", "continue", "for", "new", "switch",
    "assert", "default", "goto", "package", "synchronized",
    "boolean", "do", "if", "private", "this",
    "break", "double", "implements", "protected", "throw",
    "byte", "else", "import", "public", "throws",
    "case", "enum", "instanceof", "return", "transient",
    "catch", "extends", "int", "short", "try",
    "char", "final", "interface", "static", "void",
    "class", "finally", "long", "strictfp", "volatile","Override","Deprecated","SafeVarArgs","SuppressWarnings","FuntionalInterface","Inherited","Documented","Target","Retention","Repeatable"} #java non_primitive datatypes not added ex: "List"

for sentence in SourceCodeCleanedOfNumbers:
    #7) All words were lowercased. 
    NTLKTokenized=word_tokenize(sentence)
    for word in NTLKTokenized:             
        if word not in stop_words and word != '' and word not in keywords_java:
            split_words = re.sub(r'(?<=[a-z])(?=[A-Z])', ' ', word)
            split_words_tokenized=word_tokenize(split_words)
            print(split_words)
            for word_part in split_words_tokenized:
                word_lower=word_part.lower()
                word_stem=porter_stemmer.stem(word_lower)
                words_tokenized.append(word_stem)

print(words_tokenized)


