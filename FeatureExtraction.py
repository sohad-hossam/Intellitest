import numpy as np
from imports import *

class FeatureExtraction:
    # Latent Semantic Analysis.
    def LSA(self, tfidf_matrix_uc: np.ndarray, tfidf_matrix_code: np.ndarray, train_or_test: str) -> np.ndarray:
        num_components = min(tfidf_matrix_uc.shape[0], tfidf_matrix_code.shape[1])
        num_components = min(num_components, 100)
        LSA_model = TruncatedSVD(n_components=num_components)
        
        if train_or_test == "train":
            LSA_data_useCases = LSA_model.fit_transform(tfidf_matrix_uc)
            LSA_data_codes = LSA_model.fit_transform(tfidf_matrix_code) 
        else:
            LSA_data_useCases = LSA_model.transform(tfidf_matrix_uc)
            LSA_data_codes = LSA_model.transform(tfidf_matrix_code) 
        
        LSA_similraity_matrix = cosine_similarity(LSA_data_useCases, LSA_data_codes)
        print(LSA_similraity_matrix)
        return LSA_similraity_matrix

