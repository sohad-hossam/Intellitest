import numpy as np
from imports import *

class FeatureExtraction:
    def __init__(self, UCTokens: set) -> None:
        self.count_vectorizer = CountVectorizer(vocabulary=UCTokens)
        self.vocab_index = {word: idx for idx, word in enumerate(self.count_vectorizer.get_feature_names_out())}
        # UC_count_matrix, code_count_matrix

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
    
    def CountVectorizerModel(self, UC_documents: list, code_documents: list, train_or_test: str):
        
        self.code_count_matrix = np.zeros((len(code_documents), len(code_documents[0])))
        self.UC_count_matrix = np.zeros((len(UC_documents),len(UC_documents[0])))
        
        if train_or_test == 'train':
            self.UC_count_matrix = self.count_vectorizer.fit_transform(UC_documents)
            self.code_count_matrix = self.count_vectorizer.fit_transform(code_documents)
            self.code_vocab_index = self.count_vectorizer.vocabulary_
        else:
            self.UC_count_matrix = self.count_vectorizer.transform(UC_documents)
            self.code_count_matrix = self.count_vectorizer.transform(code_documents)
            
        UC_words_count = self.UC_count_matrix.sum(axis=1)
        code_words_count = self.code_count_matrix.sum(axis=1)

        self.UC_count_matrix /= UC_words_count
        self.code_count_matrix /= code_words_count

        self.UC_count_matrix = self.UC_count_matrix.toarray()    
        self.code_count_matrix = self.code_count_matrix.toarray()
        return self.UC_count_matrix, self.code_count_matrix
        

    # Jensen-Shannon.
    def JensenShannon(self, UC_count_matrix, code_count_matrix) -> np.ndarray:
        
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
        #print(JS_matrix.shape) => (58,116)

        return JS_matrix