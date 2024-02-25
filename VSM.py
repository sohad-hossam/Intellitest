from PreProcessor import *
import os
import pickle

cache_file = "vsm_cache.pkl"


def VectorSpaceModel():
    if os.path.exists(cache_file):
        # If the cache file exists, load the result from it
        with open(cache_file, "rb") as f:
            similarity_list = pickle.load(f)
    else:

        _PreProcessor = PreProcessor()
        UC_documents, code_documents, UCTokens, CodeTokens = _PreProcessor.setup(
            "./UC", "./CC"
        )

        UCTokens.update(CodeTokens)

        # create the transform
        vectorizer = TfidfVectorizer(vocabulary=UCTokens)
        tfidf_matrix_uc = vectorizer.fit_transform(UC_documents)
        tfidf_matrix_code = vectorizer.fit_transform(code_documents)

        # cosine similarity
        cosine_similarities = cosine_similarity(tfidf_matrix_uc, tfidf_matrix_code)

        similarity_list = []
        for i in range(cosine_similarities.shape[0]):
            similarity_dict = {}
            for j in range(cosine_similarities.shape[1]):
                similarity_dict[code_documents[j]] = cosine_similarities[i][j]
            similarity_list.append(similarity_dict)

        with open(cache_file, "wb") as f:
            pickle.dump(similarity_list, f)
    return similarity_list
