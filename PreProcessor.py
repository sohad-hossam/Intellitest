from imports import *

# 1) All the interpunction was removed. 
chars_to_remove = r'''(?x)
_|\s*;\s*|\s*,\s*|\s*\.\s*|  #Standalone Punctuation Marks
\s*\+\s*|\s*-\s*|\s*\/\s*|\s*\*\s*|  #arethmatic operations
\s*=\s*|\s*==\s*|\s*!=\s*|\s*>\s*|\s*>=\s*|\s*<\s*|\s*<=\s*|\s*&&\s*|\s*&\s*|
\s*\|\|\s*
|\s*!\s*|\s*\"\s*|\s*\'\s*|    #assignment 
\s*\(\s*|\s*\)\s*|\s*{\s*|\s*}\s*|\s*\[\s*|\s*\]\s*|\s*@\s*|\s*:\s*'''   #brackets

keywords_java={
    "abstract", "continue", "for", "new", "switch",
    "assert", "default", "goto", "package", "synchronized",
    "boolean", "do", "if", "private", "this",
    "break", "double", "implements", "protected", "throw",
    "byte", "else", "import", "public", "throws",
    "case", "enum", "instanceof", "return", "transient",
    "catch", "extends", "int", "short", "try", "null",
    "char", "final", "interface", "static", "void",
    "class", "finally", "long", "strictfp", "volatile","Override","Deprecated","SafeVarArgs","SuppressWarnings","FuntionalInterface","Inherited","Documented","Target","Retention","Repeatable"} #java non_primitive datatypes not added ex: "List"

keywords_UC = {
    "Use","case","name","Delete","Actor","Entry","Operator","conditions", "Flow" ,"events" ,"User","System" ,".", "Exit", "Quality","Partecipating","Actors" ,"Returns"
    }


def CodePreProcessor(filepath):
    dataset_txt = open(filepath, "r" , encoding='utf-8').read()
    # 1) All the interpunction was removed. 
    SourceCodeCleaned =re.split(chars_to_remove,dataset_txt)
    # 2) All numeric characters were removed. 
    numeric_chars_to_remove = r"[0-9]" 
    SourceCodeCleanedOfNumbers = [re.sub(numeric_chars_to_remove, "", x) for x in SourceCodeCleaned]
    #print(SourceCodeCleanedOfNumbers)
    #3) All sentences were tokenized with NLTK. 
    #4) The stop words corpus from NLTK was used to eliminate all stop words. 
    stop_words = set(stopwords.words('english'))
    words_tokenized=""

    #5) All remaining terms were stemmed using the Porter Stemming Algorithm
    porter_stemmer = PorterStemmer()

    #6) remove keywords in java

    for sentence in SourceCodeCleanedOfNumbers:
        #7) All words were lowercased. 
        NTLKTokenized=word_tokenize(sentence)
        for word in NTLKTokenized:  
            word = re.sub("\ufeff", "", word)
            if word not in stop_words and word != '' and word.lower() not in keywords_java:
                split_words = re.sub(r'(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])', ' ', word)
                split_words_tokenized=word_tokenize(split_words)
                for word_part in split_words_tokenized:
                    if len(word_part) == 1:
                        continue
                    word_lower=word_part.lower()
                    word_stem=porter_stemmer.stem(word_lower)
                    words_tokenized += word_stem + " "
    return words_tokenized

def UCPreProcessor(filepath):
    dataset_txt = open(filepath, "r", encoding='utf-8').read()
    # 2) All numeric characters were removed. 
    numeric_chars_to_remove = r"[0-9]" 
    UCCleanedOfNumbers = re.sub(numeric_chars_to_remove, "", dataset_txt).split()
    #3) All sentences were tokenized with NLTK. 
    #4) The stop words corpus from NLTK was used to eliminate all stop words. 
    stop_words = set(stopwords.words('english'))
    words_tokenized=""

    #5) All remaining terms were stemmed using the Porter Stemming Algorithm
    porter_stemmer = PorterStemmer()

    #6) remove keywords in java

    for sentence in UCCleanedOfNumbers:
        #7) All words were lowercased. 
        NTLKTokenized=word_tokenize(sentence)
        for word in NTLKTokenized:             
            if word not in stop_words and word != '' and len(word) != 1 and word not in keywords_UC:
                word_lower=word.lower()
                word_stem=porter_stemmer.stem(word_lower)
                words_tokenized += word_stem + " "
    return words_tokenized

