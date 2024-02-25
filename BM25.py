from PreProcessor import *
import numpy as np
import os

# calculate the BM25_code score
# BM25_code score = sum of the tf-idf scores of the query terms in the document

# BM25_code = sum of (idf(D, t) * (tf(D,t)* (k1 + 1)) / (tf(D, t) + k1 * (1 - b + b * (|D| / avgdl)))
# where:
# t is a term in the query
# k1 and b are hyperparameters k1 element [1.2, 2.0] and b element [0.75, 1.0]
# |D| is the length of the document in words
# avgdl is the average document length in the collection


# build a matrix where [i,j] is the BM25_code score of the j-th document for the i-th query
# here the query is the code file and the documents are the UC files
# and vise versa

k = 1.2;b = 0.75

uc_preprocessed_string_list = list()
unique_uc_tokens = list()
uc_sizes = list()
uc_avgdl = 0

code_preprocessed_string_list = list()
code_sizes = list()
code_avgdl = 0

# initialize the total matrix of BM25_code scores
BM25_code = np.zeros((116,58)) 
code_file_count = 0

# preprocess all uc documents -> append file strings to list  -> get all unique tokens -> calculate uc_avgdl and sizes
for filename in os.listdir("./UC"):
    filepath = os.path.join("./UC", filename)
    
    uc_tokens = UCPreProcessor(filepath)
    uc_preprocessed_string_list.append(uc_tokens)

    uc_tokens = uc_tokens.split()
    len_of_uc_preprocessed_string = len(uc_tokens)
    uc_sizes.append(len_of_uc_preprocessed_string)
    uc_avgdl += len_of_uc_preprocessed_string

    unique_uc_tokens.append(set(uc_tokens))

uc_avgdl = uc_avgdl / len(uc_sizes)

# preprocess all code documents -> append file strings to list -> calculate code_avgdl and sizes -> get unique tokens for single file to use in calculating BM25_code
# ->  get tf and idf  -> calculate BM25_code score for each UC file

for filename in os.listdir("./CC"):
    filepath = os.path.join("./CC", filename)

    code_tokens = CodePreProcessor(filepath)
    code_preprocessed_string_list.append(code_tokens)

    code_tokens = code_tokens.split()
    len_of_code_preprocessed_string = len(code_tokens)
    code_sizes.append(len_of_code_preprocessed_string)
    code_avgdl += len_of_code_preprocessed_string

    # get all t in query i.e. java file and get tf
    code_tokens = set(code_tokens)
    vectorizer_tf = CountVectorizer(vocabulary=code_tokens)
    tf = vectorizer_tf.fit_transform(uc_preprocessed_string_list).toarray()
    transformer = TfidfTransformer()
    transformer.fit(tf)
    idf = transformer.idf_.reshape(1,-1)

    for i in range(len(uc_preprocessed_string_list)):
        for j in range(len(code_tokens)):
            BM25_code[code_file_count][i] += idf[0][j] * (tf[i][j] * (k + 1)) / (tf[i][j] + k * (1 - b + b * (uc_sizes[i] / uc_avgdl)))
    

code_avgdl = code_avgdl / len(code_sizes)

BM25_uc = np.zeros((58,116))

code_file_count = 0

for uc_tokens in unique_uc_tokens:
    vectorizer_tf = CountVectorizer(vocabulary=uc_tokens)
    tf = vectorizer_tf.fit_transform(code_preprocessed_string_list).toarray()
    transformer = TfidfTransformer()
    transformer.fit(tf)
    idf = transformer.idf_.reshape(1,-1)

    for i in range(len(code_preprocessed_string_list)):
        for j in range(len(uc_tokens)):
            BM25_uc[code_file_count][i] += idf[0][j] * (tf[i][j] * (k + 1)) / (tf[i][j] + k * (1 - b + b * (code_sizes[i] / code_avgdl)))
    code_file_count += 1

# transpose the BM25_uc matrix and append it to the BM25_code matrix so that the final matrix is 232x58
BM25_uc = BM25_uc.transpose()
BM25_code = np.append(BM25_code, BM25_uc, axis=0)

# print the BM25_code matrix

print(BM25_code.shape)
