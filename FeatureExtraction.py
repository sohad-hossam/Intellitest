from imports import *


class FeatureExtraction:
    def __init__(self, UCTokens: set) -> None:
        self.tfidf_vectorizer = TfidfVectorizer(vocabulary=UCTokens)
        self.count_vectorizer = CountVectorizer(vocabulary=UCTokens)
        # self.vocab_index = {word: idx for idx, word in enumerate(self.count_vectorizer.get_feature_names_out())}
        # UC_count_matrix, code_count_matrix

    # Latent Semantic Analysis.
    def LSA(
        self,
        tfidf_matrix_uc: np.ndarray,
        tfidf_matrix_code: np.ndarray,
        train_or_test: str,
    ) -> np.ndarray:
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

    def CountVectorizerModel(
        self, UC_documents: list, code_documents: list, train_or_test: str
    ):

        self.code_count_matrix = np.zeros((len(code_documents), len(code_documents[0])))
        self.UC_count_matrix = np.zeros((len(UC_documents), len(UC_documents[0])))

        if train_or_test == "train":
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

        UC_count_matrix_repeated = np.repeat(
            UC_count_matrix, code_count_matrix.shape[0], axis=0
        )
        code_count_matrix_repeated = np.tile(
            code_count_matrix, (UC_count_matrix.shape[0], 1)
        )

        JS_matrix = pow(
            jensenshannon(UC_count_matrix_repeated, code_count_matrix_repeated, axis=1),
            2,
        )
        JS_matrix = JS_matrix.reshape(
            UC_count_matrix.shape[0], code_count_matrix.shape[0]
        )

        # ------------------------------loop approach---------------------------#
        # loop-approach
        # JS_matrix = np.zeros((UC_count_matrix.shape[0], code_count_matrix.shape[0]))
        # for i, UC_count_vector in enumerate(UC_count_matrix.toarray()):
        #     for j, code_count_vector in enumerate(code_count_matrix.toarray()):
        #         JS_matrix[i][j] = pow(jensenshannon(UC_count_vector, code_count_vector), 2)
        # print(JS_matrix.shape) => (58,116)

        return JS_matrix
    
    def TFIDFVectorizer(self, UC_documents: list, code_documents: list
    ) :

        # create the transform
        self.tfidf_matrix_uc = self.tfidf_vectorizer.fit_transform(UC_documents)
        self.tfidf_matrix_code = self.tfidf_vectorizer.fit_transform(code_documents)
        return  self.tfidf_matrix_uc,self.tfidf_matrix_code

    def VectorSpaceModel(
        self, tfidf_matrix_uc: np.ndarray, tfidf_matrix_code: np.ndarray , code_documents
    ) -> np.ndarray:

        # cosine similarity
        cosine_similarities = cosine_similarity(tfidf_matrix_uc, tfidf_matrix_code)

        similarity_list = []
        for i in range(cosine_similarities.shape[0]):
            similarity_dict = {}
            for j in range(cosine_similarities.shape[1]):
                similarity_dict[code_documents[j]] = cosine_similarities[i][j]
            similarity_list.append(similarity_dict)

        return similarity_list
    
    def _getOverlap(self, query_term_list: np.ndarray, total_query_set: set) -> int:
        query_term_set = set(np.argpartition(query_term_list, -10)[-10:])
        return len(query_term_set & total_query_set)

    def _SubqueryOverlapTerms(self, query_term: str, code_idx: int, UC_count_matrix: np.ndarray, code_count_matrix: np.ndarray, feature_extraction: object,feature_extraction_method:Callable) -> np.ndarray:
        
        word_index = feature_extraction.code_vocab_index[query_term]
        term_vector = np.zeros((1, UC_count_matrix.shape[1])) #1*
        term_vector[:,word_index]=code_count_matrix[code_idx, word_index]

        query_term_jensen_shannon = feature_extraction_method(UC_count_matrix, term_vector)
        query_term_len = query_term_jensen_shannon.shape[0] * query_term_jensen_shannon.shape[1]

        query_term_jensen_shannon = query_term_jensen_shannon.reshape(query_term_len)
        return query_term_jensen_shannon

    def _SubqueryOverlapCode(self, code_idx, code, UC_count_matrix: np.ndarray, code_count_matrix: np.ndarray, feature_extraction: object, total_query_list: np.ndarray,feature_extraction_method:Callable):

        code_idx = int(code_idx)
        code_words = code.split(" ")
        code_words = np.ascontiguousarray(code_words[0:-1])
        vSubqueryOverlap = np.vectorize(lambda query_term: self._SubqueryOverlapTerms(query_term, code_idx, UC_count_matrix, code_count_matrix, feature_extraction,feature_extraction_method), otypes=[np.ndarray])
        
        query_term_jensen_shannon = vSubqueryOverlap(code_words)
        total_query_set = set(np.argpartition(total_query_list[:, code_idx], -10)[-10:]) #should be edited
    
        vGetOverlap = np.vectorize(lambda query_term_jensen_shannon: self._getOverlap(query_term_jensen_shannon, total_query_set), otypes=[np.int16])
        query_term_overlap = vGetOverlap(query_term_jensen_shannon)
        
        overall_queries_score = np.std(query_term_overlap)
        return overall_queries_score
    
    def SubqueryOverlap(self, feature_extraction:object, query:list, feature_extraction_method:Callable, document_count_matrix:np.ndarray, query_count_matrix:np.ndarray) -> np.ndarray:
    
        ### Run the original query q, and obtain the result list R 
        total_score_query_feature_extraction = feature_extraction_method(document_count_matrix, query_count_matrix)

        query_tuples =  np.ascontiguousarray(list(enumerate(query)))
        
        ### Run each individual query term qt in the original query as a separate query and obtain the result list Rt.
        vSubqueryOverlapCode = np.vectorize(lambda code_idx, code_doc: self._SubqueryOverlapCode(code_idx, code_doc, document_count_matrix, query_count_matrix, feature_extraction, total_score_query_feature_extraction,feature_extraction_method), otypes=[np.float16])

        overall_queries_score = vSubqueryOverlapCode(query_tuples[:,0], query_tuples[:,1])
        return overall_queries_score
