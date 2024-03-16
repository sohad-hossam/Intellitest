from imports import *


class FeatureExtraction:
    def __init__(self, UCTokens: set) -> None:
        self.tfidf_vectorizer = TfidfVectorizer(vocabulary=UCTokens)
        self.count_vectorizer = CountVectorizer(vocabulary=UCTokens)
        #self.vocab_index = {word: idx for idx, word in enumerate(self.count_vectorizer.get_feature_names_out())}
        # UC_count_matrix, code_count_matrix

    # Latent Semantic Analysis.
    def LSA(
        self,
        tfidf_matrix_uc: np.ndarray,
        tfidf_matrix_code: np.ndarray,
        train_or_test: str = 'train',
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
        idf_uc = self.tfidf_vectorizer.idf_
        
        feature_names_uc = self.tfidf_vectorizer.get_feature_names_out() 
        idf_uc_dict = {feature_names_uc[i]: idf_uc[i] for i in range(len(feature_names_uc))}  #this is the idf dictionary for the whole vocab that i acsess later doing whatever

        tf_uc_array = self.tfidf_matrix_uc .toarray().sum(axis=0)
        tf_uc_dict = {feature_names_uc[i]: tf_uc_array[i] for i in range(len(feature_names_uc))}


        df_uc_array = np.sum(self.tfidf_matrix_uc > 0, axis=0).A1
        df_uc_dict = {feature_names_uc[i]: df_uc_array[i] for i in range(len(feature_names_uc))}


        self.tfidf_matrix_code = self.tfidf_vectorizer.fit_transform(code_documents)
        idf_code = self.tfidf_vectorizer.idf_

        feature_names_code = self.tfidf_vectorizer.get_feature_names_out() 
        idf_code_dict = {feature_names_code[i]: idf_code[i] for i in range(len(feature_names_code))}

        tf_code_array = self.tfidf_matrix_code.toarray().sum(axis=0)
        tf_code_dict = {feature_names_code[i]: tf_code_array[i] for i in range(len(feature_names_code))}

        df_code_array = np.sum(self.tfidf_matrix_code > 0, axis=0).A1
        df_code_dict = {feature_names_code[i]: df_code_array[i] for i in range(len(feature_names_code))}


        return  self.tfidf_matrix_uc,self.tfidf_matrix_code,idf_uc_dict,tf_uc_dict,idf_code_dict,tf_code_dict,feature_names_uc,feature_names_code,df_uc_dict,df_code_dict



    def VectorSpaceModel(
        self, tfidf_matrix_uc: np.ndarray, tfidf_matrix_code: np.ndarray
    ) -> np.ndarray:

        # cosine similarity
        cosine_similarities = cosine_similarity(tfidf_matrix_uc, tfidf_matrix_code)

        # similarity_list = []
        # for i in range(cosine_similarities.shape[0]):
        #     similarity_dict = {}
        #     for j in range(cosine_similarities.shape[1]):
        #         similarity_dict[code_documents[j]] = cosine_similarities[i][j]
        #     similarity_list.append(similarity_dict)

        return cosine_similarities
    
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
    
    def DocumentStatistics(self, UC_documents: list, code_documents:list):
        num_terms_code =[]
        for document in code_documents:
            num_terms_code.append(len(document.split()))

        # 1st feature
        num_terms_code=np.array(num_terms_code)

        num_terms_UC =[]
        for document in UC_documents:
            num_terms_UC.append(len(document.split()))
        
        # 2nd feature
        num_terms_UC=np.array(num_terms_UC)

        num_unique_terms_code =[]
        for document in code_documents:
            num_unique_terms_code.append(len(set(document.split())))

        # 3rd feature
        num_unique_terms_code=np.array(num_unique_terms_code)

        num_unique_terms_UC =[]
        for document in UC_documents:
            num_unique_terms_UC.append(len(set(document.split())))

        # 4th feature
        num_unique_terms_UC=np.array(num_unique_terms_UC)

        # each column represent D1 intersect with all usecases, then column2= D2 intersect with all usescases....
        #if want to access the intesection between a specific usecase and all coressponding documents you will access the row index coressponding to this usecase 
        # 5th feature
        num_overlapping_terms =np.zeros((len(UC_documents),len(code_documents)))
        for uc_index,uc in enumerate(UC_documents):
            for code_index,code in enumerate(code_documents):
                num_overlapping_terms[uc_index][code_index] = 100*(len(set(uc.split()).intersection(set(code.split()))))/(num_unique_terms_UC[uc_index]+num_unique_terms_code[code_index])

        return num_terms_code, num_terms_UC, num_unique_terms_code, num_unique_terms_UC, num_overlapping_terms 
    


    def IDFPreProcessing(self,UC_documents: list,idf_code_dict:dict,code_documents: list,idf_uc_dict:dict):
        idf_uc_q=[]
        for Querey in UC_documents:
            tokens = Querey.split()
            idf_q=[]
            for token in tokens:
                idf_q.append(idf_code_dict[token]) 
            idf_uc_q.append(idf_q)

        idf_code_q=[]
        for Querey in code_documents:
            tokens = Querey.split()
            idf_q=[]
            for token in tokens:
                idf_q.append(idf_uc_dict[token])
            idf_code_q.append(idf_q)
        return idf_uc_q,idf_code_q


    
    def AvgIDF(self,idf_values:list):
        avgidf=[]
        for query in idf_values:
            avgidf.append(sum(query)/len(idf_values))    # |G| is len(idf_values)
        return  np.array(avgidf)

    def MaxIDF(self,idf_values:list):
        maxidf=[]
        for query in idf_values:
            maxidf.append(max(query))
        return np.array(maxidf)


    def DevIDF(self,idf_values:list):
        devidf=[]
        avgidf=self.AvgIDF(idf_values)
        for query_idf in idf_values:
            deviation = np.sqrt(sum([abs(idf - avg) for idf, avg in zip(query_idf, avgidf)]) / len(idf_values))
            devidf.append(deviation)
        return  np.array(devidf)
       
    def ICTFPreProcessing(self,UC_documents:list,tf_code_dict:dict,code_documents:list,tf_uc_dict:dict):
        ictf_uc_q=[]
        for Querey in UC_documents:
            tokens = Querey.split()
            ictf_q=[]
            for token in tokens:
                if token in tf_code_dict.keys():
                    ictf_q.append(np.log(len(code_documents)/(tf_code_dict[token]+1)))
                else :
                    ictf_q.append(0) 
            ictf_uc_q.append(ictf_q)

        ictf_code_q=[]
        for Querey in code_documents:
            tokens = Querey.split()
            ictf_q=[]
            for token in tokens:
                if token in tf_uc_dict.keys():
                  ictf_q.append(np.log(len(UC_documents)/(tf_uc_dict[token]+1)))
                else:
                    ictf_q.append(0) 
            ictf_code_q.append(ictf_q)
        return ictf_uc_q,ictf_code_q

    

    def AvgICTF(self,ictf_values:list):
        avgictf=[]
        for query in ictf_values:
            avgictf.append(sum(query) / len(query))
        return np.array(avgictf)

    def MaxICTF(self,ictf_values:list):
        maxictf=[]
        for query in ictf_values:
            maxictf.append(max(query))
        return np.array(maxictf)

    def DevICTF(self,ictf_values:list):
        devictf=[]
        avgictf=self.AvgICTF(ictf_values)
        for query_ictf in ictf_values:
            deviation = np.sqrt(sum([abs(ictf - avg)for ictf ,avg in zip(query_ictf,avgictf)]) / len(ictf_values))
            devictf.append(deviation) 
        return np.array(devictf)
    



    def VarPreProcessing(self,UC_documents:list,code_documents:list,idf_uc_dict:dict,idf_code_dict:dict):
        variance_uc = [] 

        for query in UC_documents:
            tokens = query.split()
            query_variances = []  
            for term in tokens:
                term_weights = []
                for doc in code_documents:
                    tf_term_doc = doc.count(term)
                    weight_term_doc = (1 / len(doc)) * np.log(1 + tf_term_doc) * idf_uc_dict[term]  #
                    term_weights.append(weight_term_doc)

                avg_weight_term = np.mean(term_weights)
                variance_term = np.mean([(weight - avg_weight_term) ** 2 for weight in term_weights])
                query_variances.append(variance_term) 
            variance_uc.append(query_variances)  

        variance_code = []

        for query in code_documents:
            tokens = query.split()
            query_variances = []  
            for term in tokens:
                term_weights = []
                for doc in UC_documents:
                    tf_term_doc = doc.count(term)
                    weight_term_doc = (1 / len(doc)) * np.log(1 + tf_term_doc) * idf_code_dict[term]  #
                    term_weights.append(weight_term_doc)

                avg_weight_term = np.mean(term_weights)
                variance_term = np.mean([(weight - avg_weight_term) ** 2 for weight in term_weights])
                query_variances.append(variance_term) 
            variance_code.append(query_variances)
        return variance_uc,variance_code
    
        
    def AvgVariance(self,variance_values:list):
        avgvariance=[]
        for query in variance_values:
            avgvariance.append(sum(query) / len(variance_values))
        return np.array(avgvariance)

    def MaxVariance(self,variance_values:list):
        maxvariance=[]
        for query in variance_values:
            maxvariance.append(max(query))
        return np.array(maxvariance)

    def SumVariance(self,variance_values:list):
        sumvariance=[]
        for query in variance_values:
            sumvariance.append(sum(query))
        return np.array(sumvariance)
    
    def PMIPreProcessing(self,code_documents:list,UC_documents:list):
        PMI_code = [] 

        for doc in code_documents:
            unique_terms = set(doc)
            
            term_co_occurrences_code = defaultdict(int)
            for term1 in unique_terms:
                for term2 in unique_terms:
                    if term1 != term2:
                        term_co_occurrences_code[(term1, term2)] += 1

            document_PMI = {} 
            for term_pair, co_occurrence_count in term_co_occurrences_code.items():
                term1, term2 = term_pair
                pt1_t2_D = co_occurrence_count / len(doc)
                pt1_D = sum(1 for term in doc if term == term1) / len(doc)
                pt2_D = sum(1 for term in doc if term == term2) / len(doc)
                pt_D = pt1_D * pt2_D

                if pt_D != 0:
                    document_PMI[term_pair] = np.log(pt1_t2_D / pt_D)
            PMI_code.append(document_PMI)

        PMI_uc = []

        for doc in UC_documents:
            unique_terms = set(doc)
            
            term_co_occurrences_uc = defaultdict(int)
            for term1 in unique_terms:
                for term2 in unique_terms:
                    if term1 != term2:
                        term_co_occurrences_uc[(term1, term2)] += 1

            document_PMI = {} 
            for term_pair, co_occurrence_count in term_co_occurrences_uc.items():
                term1, term2 = term_pair
                pt1_t2_D = co_occurrence_count / len(doc)
                pt1_D = sum(1 for term in doc if term == term1) / len(doc)
                pt2_D = sum(1 for term in doc if term == term2) / len(doc)
                pt_D = pt1_D * pt2_D

                if pt_D != 0:
                    document_PMI[term_pair] = np.log(pt1_t2_D / pt_D)
            PMI_uc.append(document_PMI)
        return PMI_uc,PMI_code
        

    def AvgPMI(self,PMI_values:list):
        avgpmi=[]
        for query in PMI_values:
            num_terms = len(query)
            if num_terms > 1:
                avgpmi.append(2 * sum(query.values()) * math.exp(math.lgamma(num_terms - 1) - math.lgamma(num_terms)))
            else:
                avgpmi.append(0) 
        return np.array(avgpmi)

    def MaxPMI(self,PMI_values:list):
        maxpmi=[]
        for query in PMI_values:
            if query:
                maxpmi.append(max(query.values()))
            else:
                maxpmi.append(0)
        return np.array(maxpmi)
    
    def QS(self,documents:list, otherdocuments:list):
        QSdocs = []
        total_documents = len(otherdocuments)
        
        for query in documents:
            term_occurrence = set() 
            tokens = query.split() 
            for term in tokens:
                for doc in otherdocuments:
                    if term in doc:
                        term_occurrence.add(doc)
            
            percentage = (len(term_occurrence) / total_documents) * 100
            QSdocs.append(percentage)
        return np.array(QSdocs)
    
    def EntropyPreProcessing(self,UC_documents:list,code_documents:list,df_uc_dict:dict,df_code_dict:dict):
        entropy_uc = [] 
        for query in UC_documents:
            tokens = query.split()
            query_entropy = []  
            for term in tokens:
                term_entropy =0
                for doc in code_documents:
                    tf_term_doc = doc.count(term)+1
                    tf_term_collection = df_code_dict[term] +1
                    term_entropy+=((tf_term_doc / tf_term_collection) * np.log((tf_term_doc / tf_term_collection) + 1))
                query_entropy.append(term_entropy)
            entropy_uc.append(query_entropy)  


        entropy_code = []
        for query in code_documents:
            tokens = query.split()
            query_entropy = []  
            for term in tokens:
                term_entropy =0
                for doc in UC_documents:
                    tf_term_doc = doc.count(term)+1
                    tf_term_collection = df_uc_dict[term] +1
                    term_entropy+=((tf_term_doc / tf_term_collection) * np.log((tf_term_doc / tf_term_collection) + 1))
                query_entropy.append(term_entropy)
            entropy_code.append(query_entropy)


        return entropy_uc,entropy_code
    
        
    def AvgEntropy(self,entropy_values:list):
        avgentropy=[]
        for query in entropy_values:
            avgentropy.append(sum(query) / len(entropy_values))
        return np.array(avgentropy)
        
    def MaxEntropy(self,entropy_values:list):
        maxentropy=[]
        for query in entropy_values:
            maxentropy.append(max(query))
        return np.array(maxentropy)
    def MedEntropy(self,entropy_values:list):
        medentropy=[]
        for query in entropy_values:
            medentropy.append(np.median(query))
        return np.array(medentropy)

        
    def DevEntropy(self,entropy_values:list):
        deventropy=[]
        avgentropy = self.AvgEntropy(entropy_values)
        for query_entropy in entropy_values:
            deviation = np.sqrt(sum([abs(entropy - avg) for entropy, avg in zip(query_entropy,avgentropy)]) / len(entropy_values))
            deventropy.append(deviation)
        return np.array(deventropy)
    
    def SCQPreProcessing(self,UC_documents:list,code_documents:list,tf_uc_dict:dict,tf_code_dict:dict,idf_uc_dict:dict,idf_code_dict:dict):
        SCQ_uc = []  
        for query in UC_documents:
            tokens = query.split()
            query_scq = []  
            for term in tokens:
                if term in tf_code_dict.keys() and term in idf_code_dict.keys():
                   query_scq.append((1 + np.log((tf_code_dict[term])+1) * idf_code_dict[term]))
                else:
                    query_scq.append(0)  
            SCQ_uc.append(query_scq)


        SCQ_code = []

        for query in code_documents:
            tokens = query.split()
            query_scq = []  
            for term in tokens:
                if term in tf_uc_dict.keys() and term in idf_uc_dict.keys():
                    query_scq.append((1 + np.log((tf_uc_dict[term])+1) * idf_uc_dict[term])+1)
                else:
                    query_scq.append(0)
            SCQ_code.append(query_scq)
        return SCQ_uc,SCQ_code
    
    def AvgSCQ(self,SCQ_values:list):
        avgscq=[]
        for query in SCQ_values:
            avgscq.append(sum(query) / len(SCQ_values))
        return np.array(avgscq)

    def MaxSCQ(self,SCQ_values:list):
        maxscq=[]
        for query in SCQ_values:
            maxscq.append(max(query))
        return np.array(maxscq)

    def SumSCQ(self,SCQ_values:list):
        sumscq=[]
        for query in SCQ_values:
            sumscq.append(sum(query))
        return np.array(sumscq)

    def _BM25(self, UC_documents, code_documents):
        k = 1.2;b = 0.75

        uc_avgdl = 0
        code_avgdl = 0
        
        uc_sizes = list()
        code_sizes = list()

        BM25_code = np.zeros((len(code_documents),len(UC_documents)))
        uc_sizes = np.array([len(doc.split()) for doc in UC_documents])
        code_sizes = np.array([len(doc.split()) for doc in code_documents])

        uc_avgdl = np.sum(uc_sizes)/len(uc_sizes)
        code_avgdl = np.sum(code_sizes)/len(code_sizes)

        for i, code_doc in enumerate(code_documents):
            code_tokens = code_doc.split()
            code_tokens = set(code_tokens)
            #idf = TfidfVectorizer(vocabulary=code_tokens).fit(UC_documents).idf_.reshape(1,-1)
            
            vectorizer_tf = CountVectorizer(vocabulary=code_tokens)
            tf = vectorizer_tf.fit_transform(UC_documents).toarray()
            transformer = TfidfTransformer()
            transformer.fit(tf)
            idf = transformer.idf_.reshape(1,-1)
            
            for j, uc_doc in enumerate(UC_documents):
                for cnt, token in enumerate(code_tokens):
                    BM25_code[i][j] += idf[0][cnt] * (uc_doc.count(token) * (k + 1)) / (uc_doc.count(token) + k * (1 - b + b * (uc_sizes[j] / uc_avgdl)))
        
        BM25_uc = np.zeros((len(UC_documents),len(code_documents)))

        for i, uc_doc in enumerate(UC_documents):
            uc_tokens = uc_doc.split()
            uc_tokens = set(uc_tokens)
            #idf = TfidfVectorizer(vocabulary=uc_tokens).fit(code_documents).idf_.reshape(1,-1)
            
            vectorizer_tf = CountVectorizer(vocabulary=uc_tokens)
            tf = vectorizer_tf.fit_transform(code_documents).toarray()
            transformer = TfidfTransformer()
            transformer.fit(tf)
            idf = transformer.idf_.reshape(1,-1)

            for j, code_doc in enumerate(code_documents):
                for cnt, token in enumerate(uc_tokens):
                    BM25_uc[i][j] += idf[0][cnt] * (code_doc.count(token) * (k + 1)) / (code_doc.count(token) + k * (1 - b + b * (code_sizes[j] / code_avgdl)))

        return np.append(BM25_code,BM25_uc.transpose(),axis=0)
