import numpy as np
from imports import *

class FeatureExtraction:
    def __init__(self, UCTokens: set) -> None:
        self.count_vectorizer = CountVectorizer(vocabulary=UCTokens)

    # Latent Semantic Analysis.
    def LSA(self, tfidf_matrix_uc: np.ndarray, tfidf_matrix_code: np.ndarray, train_or_test: str) -> np.ndarray:
        num_components = min(tfidf_matrix_uc.shape[0], tfidf_matrix_code.shape[1])
        num_components = min(num_components, 100)
        LSA_model = TruncatedSVD(n_components=num_components)
        
        if train_or_test == "train":
            LSA_data_useCases = LSA_model.fit_transform(tfidf_matrix_uc)
            LSA_data_codes = LSA_model.fit_transform(tfidf_matrix_code) 
            LSA_similraity_matrix = cosine_similarity(LSA_data_useCases, LSA_data_codes)
            # print(LSA_similraity_matrix)
            return LSA_similraity_matrix
        else:
            LSA_data_useCases = LSA_model.transform(tfidf_matrix_uc)
            LSA_data_codes = LSA_model.transform(tfidf_matrix_code) 
            LSA_similraity_matrix = cosine_similarity(LSA_data_useCases, LSA_data_codes)
            # print(LSA_similraity_matrix)
            return LSA_similraity_matrix
        
        

    # Jensen-Shannon.
    def JensenShannon(self, UC_documents: list, code_documents: list, train_or_test: str) -> np.ndarray:
        
        code_count_matrix = np.zeros((code_documents.shape[0], code_documents.shape[1]))
        UC_count_matrix = np.zeros((UC_documents.shape[0], UC_documents.shape[1]))
        
        if train_or_test == 'train':
            UC_count_matrix = self.count_vectorizer.fit_transform(UC_documents)
            code_count_matrix = self.count_vectorizer.fit_transform(code_documents)
        else:
            UC_count_matrix = self.count_vectorizer.transform(UC_documents)
            code_count_matrix = self.count_vectorizer.transform(code_documents)
            
        UC_words_count = UC_count_matrix.sum(axis=1)
        code_words_count = code_count_matrix.sum(axis=1)

        UC_count_matrix /= UC_words_count
        code_count_matrix /= code_words_count

        UC_count_matrix = UC_count_matrix.toarray()    
        code_count_matrix = code_count_matrix.toarray()
        UC_count_matrix_repeated = np.repeat(UC_count_matrix, code_count_matrix.shape[0], axis=0)
        code_count_matrix_repeated = np.tile(code_count_matrix, (UC_count_matrix.shape[0],1))

        JS_matrix = pow(jensenshannon(UC_count_matrix_repeated, code_count_matrix_repeated, axis=1), 2)
        JS_matrix = JS_matrix.reshape(UC_count_matrix.shape[0], code_count_matrix.shape[0])
        #------------------------------loop approach---------------------------#
        # loop-approach
        # JS_matrix = np.zeros((UC_count_matrix.shape[0], code_count_matrix.shape[0]))
        # for i, UC_count_vector in enumerate(UC_count_matrix.toarray()):
        #     for j, code_count_vector in enumerate(code_count_matrix.toarray()):
        #         JS_matrix[i][j] = pow(jensenshannon(UC_count_vector, code_count_vector), 2)
        # print(JS_matrix[0])
        return JS_matrix