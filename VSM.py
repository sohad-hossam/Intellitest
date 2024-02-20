from imports import *
from PreProcessor import *
import os

CodeTokens = set()
UCTokens = set()

documents = list()
for filename in os.listdir("./UC"):
    filepath = os.path.join("./UC", filename)
    tokens = UCPreProcessor(filepath)
    documents.append(tokens)
    UCTokens.update(tokens.split())

# create the transform
vectorizer_uc = TfidfVectorizer(vocabulary=UCTokens)
tfidf_matrix_uc = vectorizer_uc.fit_transform(documents)

for filename in os.listdir("./CC"):
    filepath = os.path.join("./CC", filename)
    tokens = CodePreProcessor(filepath)
    documents.append(tokens)

# create the transform
vectorizer_code = TfidfVectorizer(vocabulary=UCTokens)
tfidf_matrix_code = vectorizer_code.fit_transform(documents)

# cosine similarity
cosine_similarities = cosine_similarity(tfidf_matrix_uc, tfidf_matrix_code)
print(cosine_similarities)