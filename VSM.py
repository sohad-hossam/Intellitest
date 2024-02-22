from imports import *
from PreProcessor import *
import os

CodeTokens = set()
UCTokens = set()

UC_documents = list()
for filename in os.listdir("./UC"):
    filepath = os.path.join("./UC", filename)
    tokens = UCPreProcessor(filepath)
    UC_documents.append(tokens)
    UCTokens.update(tokens.split())


code_documents = list()
for filename in os.listdir("./CC"):
    filepath = os.path.join("./CC", filename)
    tokens = CodePreProcessor(filepath)
    code_documents.append(tokens)
    CodeTokens.update(tokens.split())

UCTokens.update(CodeTokens)

# create the transform
vectorizer_uc = TfidfVectorizer(vocabulary=UCTokens)
tfidf_matrix_uc = vectorizer_uc.fit_transform(UC_documents)

# create the transform
vectorizer_code = TfidfVectorizer(vocabulary=UCTokens)
tfidf_matrix_code = vectorizer_code.fit_transform(code_documents)

# cosine similarity
cosine_similarities = cosine_similarity(tfidf_matrix_uc, tfidf_matrix_code)
# print(tfidf_matrix_uc.shape, tfidf_matrix_code.shape, cosine_similarities)