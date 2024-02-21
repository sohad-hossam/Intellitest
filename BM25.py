from PreProcessor import *
import numpy as np
import os



# calculate the BM25+ score
# BM25+ score = sum of the tf-idf scores of the query terms in the document

# BM25+ = sum of (idf(D, t) * ((tf(D,t)* (k1 + 1)) / (tf(D, t) + k1 * (1 - b + b * (|D| / avgdl)))+sigma)
# where:
# t is a term in the query
# k1 and b are hyperparameters k1 element [1.2, 2.0] and b element [0.75, 1.0]
# |D| is the length of the document in words
# avgdl is the average document length in the collection
# sigma = 1 to act as a lower bound for the BM25+ score

# the equation for one document D and one query Q is:
# BM25+ = sum of (idf(D, t) * ((tf(D,t)* (k1 + 1)) / (tf(D, t) + k1 * (1 - b + b * (|D| / avgdl)))+sigma)

# build a matrix where [i,j] is the BM25+ score of the j-th document for the i-th query

k = 1.2;b = 0.75

uc_preprocessed_string_list = list()
uc_sizes = list()
avgdl = 0

# preprocess all uc documents -> append file strings to list -> calculate avgdl and sizes
for filename in os.listdir("./UC"):
    filepath = os.path.join("./UC", filename)

    uc_tokens = UCPreProcessor(filepath)
    uc_preprocessed_string_list.append(uc_tokens)
    len_of_uc_preprocessed_string = len(uc_tokens.split())
    uc_sizes.append(len_of_uc_preprocessed_string)
    avgdl += len_of_uc_preprocessed_string

avgdl = avgdl / len(uc_sizes)

# initialize the total matrix of BM25+ scores
BM25 = np.zeros((116,58))
code_file_count = 0

for filename in os.listdir("./CC"):
    filepath = os.path.join("./CC", filename)
    # get all t in query i.e. java file and get tf
    code_tokens = set(CodePreProcessor(filepath).split())
    vectorizer_tf = CountVectorizer(vocabulary=code_tokens)
    
    tf = vectorizer_tf.fit_transform(uc_preprocessed_string_list).toarray()
    transformer = TfidfTransformer()
    transformer.fit(tf)
    idf = transformer.idf_.reshape(1,-1)

    for i in range(len(uc_preprocessed_string_list)):
        for j in range(0,len(code_tokens)):
            BM25[code_file_count][i] += idf[0][j] * (tf[i][j] * (k + 1)) / (tf[i][j] + k * (1 - b + b * (uc_sizes[i] / avgdl)))
    
    code_file_count += 1

print(BM25)