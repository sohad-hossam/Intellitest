from imports import *


class FeatureExtraction:
    def __init__(self, Tokens: set) -> None:
        self.tfidf_vectorizer = TfidfVectorizer(vocabulary=Tokens)
        self.count_vectorizer = CountVectorizer(vocabulary=Tokens)
        self.count_vocab_index = {}

    # Latent Semantic Analysis.
    def LSA(self,tfidf_matrix_uc: np.ndarray,tfidf_matrix_code: np.ndarray,train_or_test: str = 'train') -> np.ndarray:
        num_components = min(tfidf_matrix_uc.shape[0], tfidf_matrix_code.shape[1])
        num_components = min(num_components, 100)
        LSA_model = TruncatedSVD(n_components=num_components)

        if train_or_test == "train":
            LSA_data_useCases = LSA_model.fit_transform(tfidf_matrix_uc)
            LSA_data_codes = LSA_model.fit_transform(tfidf_matrix_code)
            LSA_similraity_matrix = cosine_similarity(LSA_data_useCases, LSA_data_codes)
            return LSA_similraity_matrix
        else:
            LSA_data_useCases = LSA_model.transform(tfidf_matrix_uc)
            LSA_data_codes = LSA_model.transform(tfidf_matrix_code)
            LSA_similraity_matrix = cosine_similarity(LSA_data_useCases, LSA_data_codes)
            return LSA_similraity_matrix

    def LDA(self,UC_documents:list,code_documents:list,TotalTokens:set):


        id2word = corpora.Dictionary([list(TotalTokens)])
        corpus_uc = [id2word.doc2bow(doc.split()) for doc in UC_documents]
        corpus_code = [id2word.doc2bow(doc.split()) for doc in code_documents]

        num_topics = 58
        lda_model_uc = LdaMulticore(corpus=corpus_uc, id2word=id2word, num_topics=num_topics)
        lda_model_code = LdaMulticore(corpus=corpus_code, id2word=id2word, num_topics=num_topics)

        DocumentTopicDisUC = lda_model_uc[corpus_uc]
        DocumentTopicDisCode = lda_model_code[corpus_code]

        DocumentTopicDisUC_dense = gensim.matutils.corpus2dense(DocumentTopicDisUC, num_terms=num_topics).T
        DocumentTopicDisCode_dense = gensim.matutils.corpus2dense(DocumentTopicDisCode, num_terms=num_topics).T

        cosine_similarities = cosine_similarity(DocumentTopicDisUC_dense, DocumentTopicDisCode_dense)

        return DocumentTopicDisUC_dense, DocumentTopicDisCode_dense, cosine_similarities


    def CountVectorizerModel(self, UC_documents: list, code_documents: list, train_or_test: str = "train"):

        self.code_count_matrix = np.zeros((len(code_documents), len(code_documents[0])))
        self.UC_count_matrix = np.zeros((len(UC_documents), len(UC_documents[0])))

        if train_or_test == "train":
            self.UC_count_matrix = self.count_vectorizer.fit_transform(UC_documents)
            self.code_count_matrix = self.count_vectorizer.fit_transform(code_documents)
        else:
            self.UC_count_matrix = self.count_vectorizer.transform(UC_documents)
            self.code_count_matrix = self.count_vectorizer.transform(code_documents)

        self.count_vocab_index = self.count_vectorizer.vocabulary_

        UC_total_number_of_occurences_in_corpus = np.sum(self.UC_count_matrix, axis=0)
        code_total_number_of_occurences_in_corpus = np.sum(self.code_count_matrix, axis=0)
        
        # dict of word: # of occurences in the corpus
        tf_uc_dict = {word: UC_total_number_of_occurences_in_corpus[0,i] for word,i in self.count_vocab_index.items()}
        tf_code_dict = {word: code_total_number_of_occurences_in_corpus[0,i] for word,i in self.count_vocab_index.items()}

        UC_words_count = self.UC_count_matrix.sum(axis=1)
        code_words_count = self.code_count_matrix.sum(axis=1)

        self.UC_count_matrix /= UC_words_count
        self.code_count_matrix /= code_words_count

        self.UC_count_matrix = self.UC_count_matrix.toarray()
        self.code_count_matrix = self.code_count_matrix.toarray()       

        return self.UC_count_matrix, self.code_count_matrix,tf_uc_dict,tf_code_dict

    # Jensen-Shannon.
    def JensenShannon(self,UC_count_matrix, code_count_matrix) -> np.ndarray:

        # UC_count_matrix_repeated = np.repeat(
        #     UC_count_matrix, code_count_matrix.shape[0], axis=0
        # )
        # code_count_matrix_repeated = np.tile(
        #     code_count_matrix, (UC_count_matrix.shape[0], 1)
        # )

        # JS_matrix = pow(
        #     jensenshannon(UC_count_matrix_repeated, code_count_matrix_repeated, axis=1),
        #     2,
        # )
        # JS_matrix = JS_matrix.reshape(
        #     UC_count_matrix.shape[0], code_count_matrix.shape[0]
        # )

        # ------------------------------loop approach---------------------------#
        # loop-approach
        JS_matrix = np.zeros((len(UC_count_matrix), len(code_count_matrix)))
        for i, UC_count_vector in enumerate(list(UC_count_matrix)):
            for j, code_count_vector in enumerate(list(code_count_matrix)):
                JS_matrix[i][j] = pow(jensenshannon(UC_count_vector, code_count_vector), 2)
        # print(JS_matrix.shape) #=> (58,116)

        return JS_matrix
    
    def TFIDFVectorizer(self, UC_documents: list, code_documents: list, train_or_test: str="train") :

        # create the transform
        if train_or_test == "train":
            self.tfidf_matrix_uc = self.tfidf_vectorizer.fit_transform(UC_documents)
            self.tfidf_matrix_code = self.tfidf_vectorizer.fit_transform(code_documents)

        else:
            self.tfidf_matrix_uc = self.tfidf_vectorizer.transform(UC_documents)
            self.tfidf_matrix_code = self.tfidf_vectorizer.transform(code_documents)

        
        idf_uc = self.tfidf_vectorizer.idf_ 
        
        feature_names_uc = self.tfidf_vectorizer.get_feature_names_out() 
        idf_uc_dict = {feature_names_uc[i]: idf_uc[i] for i in range(len(feature_names_uc))}  #this is the idf dictionary for the whole vocab that i acsess later doing whatever

        # tf_uc_array = self.tfidf_matrix_uc .toarray().sum(axis=0)
        # tf_uc_dict = {feature_names_uc[i]: tf_uc_array[i] for i in range(len(feature_names_uc))}

        df_uc_array = np.sum(self.tfidf_matrix_uc > 0, axis=0).A1
        df_uc_dict = {feature_names_uc[i]: df_uc_array[i] for i in range(len(feature_names_uc))}


        
        idf_code = self.tfidf_vectorizer.idf_

        feature_names_code = self.tfidf_vectorizer.get_feature_names_out() 
        idf_code_dict = {feature_names_code[i]: idf_code[i] for i in range(len(feature_names_code))}

        # tf_code_array = self.tfidf_matrix_code.toarray().sum(axis=0)
        # tf_code_dict = {feature_names_code[i]: tf_code_array[i] for i in range(len(feature_names_code))}

        df_code_array = np.sum(self.tfidf_matrix_code > 0, axis=0).A1
        df_code_dict = {feature_names_code[i]: df_code_array[i] for i in range(len(feature_names_code))}

        self.tf_idf_vocab_index = self.tfidf_vectorizer.vocabulary_

        return  self.tfidf_matrix_uc,self.tfidf_matrix_code,idf_uc_dict,idf_code_dict,feature_names_uc,feature_names_code,df_uc_dict,df_code_dict

    def VectorSpaceModel(self, tfidf_matrix_uc: np.ndarray, tfidf_matrix_code: np.ndarray) -> np.ndarray:
        # cosine similarity
        cosine_similarities = cosine_similarity(tfidf_matrix_uc, tfidf_matrix_code)
        return cosine_similarities
    
    def _getOverlap(self, query_term_list: np.ndarray, total_query_set: set) -> int:
        query_term_set = set(np.argpartition(query_term_list, -10)[-10:])
        return len(query_term_set & total_query_set)

    def _SubqueryOverlapTerms(self, query_term: str, code_idx: int, feature_extraction_method:Callable,feature_extraction_type:str,*args) -> np.ndarray:
        
        query_term_jensen_shannon = None
        if feature_extraction_type == "JS" or feature_extraction_type == "VSM":
            word_index = self.count_vocab_index.get(query_term, self.count_vocab_index.get('__unk__'))
            term_vector = np.zeros((1, args[0].shape[1])) #1*
            term_vector[:,word_index]=args[1][code_idx, word_index]
            query_term_jensen_shannon = feature_extraction_method(args[0], term_vector)
        elif feature_extraction_type == "BM":
            # args[0] ->documents:list args[1]->idf_dict:dict args[2] -> count_matrix:np.array
            query_term_jensen_shannon = feature_extraction_method(list(query_term),args[0],args[1],args[2])
        elif feature_extraction_type == "SM":
            # args[0] -> documents:list args[1]->count_matrix:np.array args[2]->tf_dict:dict args[3] -> JM_or_DP:bool
            query_term_jensen_shannon = feature_extraction_method(list(query_term),args[0],args[1],args[2],args[3])
       
        query_term_len = query_term_jensen_shannon.shape[0] * query_term_jensen_shannon.shape[1]

        query_term_jensen_shannon = query_term_jensen_shannon.reshape(query_term_len)
        return query_term_jensen_shannon

    def _SubqueryOverlapCode(self, code_idx, code, total_query_list: np.ndarray,feature_extraction_method:Callable,feature_extraction_type:str,*args):

        code_idx = int(code_idx)
        code_words = code.split(" ")
        code_words = np.ascontiguousarray(code_words[0:-1])
        vSubqueryOverlap = np.vectorize(lambda query_term: self._SubqueryOverlapTerms(query_term, code_idx,feature_extraction_method,feature_extraction_type,*args), otypes=[np.ndarray])
        
        query_term_jensen_shannon = vSubqueryOverlap(code_words)
        total_query_set = set(np.argpartition(total_query_list[:, code_idx], -10)[-10:]) #should be edited
    
        vGetOverlap = np.vectorize(lambda query_term_jensen_shannon: self._getOverlap(query_term_jensen_shannon, total_query_set), otypes=[np.int16])
        query_term_overlap = vGetOverlap(query_term_jensen_shannon)
        
        overall_queries_score = np.std(query_term_overlap)
        return overall_queries_score
    
    def SubqueryOverlap(self, query:list, feature_extraction_method:Callable,feature_extraction_type:str="JS",*args) -> np.ndarray:
    
        ### Run the original query q, and obtain the result list R 
        total_score_query_feature_extraction = None
        if feature_extraction_type == "BM" or feature_extraction_type == "SM":
            total_score_query_feature_extraction = feature_extraction_method(query,*args)
        else:
            total_score_query_feature_extraction = feature_extraction_method(*args)

        query_tuples =  np.ascontiguousarray(list(enumerate(query)))
        
        ### Run each individual query term qt in the original query as a separate query and obtain the result list Rt.
        vSubqueryOverlapCode = np.vectorize(lambda code_idx, code_doc: self._SubqueryOverlapCode(code_idx, code_doc, total_score_query_feature_extraction,feature_extraction_method,feature_extraction_type,*args), otypes=[np.float16])

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
                if token in idf_code_dict.keys():
                    idf_q.append(idf_code_dict[token])
                else:
                    idf_q.append(idf_code_dict['__unk__']) 
            idf_uc_q.append(idf_q)

        idf_code_q=[]
        for Querey in code_documents:
            tokens = Querey.split()
            idf_q=[]
            for token in tokens:
                if token in idf_uc_dict.keys():
                    idf_q.append(idf_uc_dict[token])
                else:
                    idf_q.append(idf_uc_dict['__unk__'])
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
                     ictf_q.append(np.log(len(code_documents)/(tf_code_dict['__unk__']+1)))
            ictf_uc_q.append(ictf_q)

        ictf_code_q=[]
        for Querey in code_documents:
            tokens = Querey.split()
            ictf_q=[]
            for token in tokens:
                if token in tf_uc_dict.keys():
                  ictf_q.append(np.log(len(UC_documents)/(tf_uc_dict[token]+1)))
                else:
                    ictf_q.append(np.log(len(UC_documents)/(tf_uc_dict['__unk__']+1)))  
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
                    if term in idf_uc_dict.keys():
                        weight_term_doc = (1 / len(doc)) * np.log(1 + tf_term_doc) * idf_uc_dict[term]
                    else:
                        weight_term_doc = (1 / len(doc)) * np.log(1 + tf_term_doc) * idf_uc_dict['<unk']  
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
                    if term in idf_code_dict.keys():
                        weight_term_doc = (1 / len(doc)) * np.log(1 + tf_term_doc) * idf_code_dict[term]  
                    else:
                        weight_term_doc = (1 / len(doc)) * np.log(1 + tf_term_doc) * idf_code_dict['<unk']
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
    
    def _simplifiedClarityScorePerQuery(self,query:str,query_tf_matrix:np.array,doc_dict:dict)->np.float16:
        # lw 3ndna dict ndelo el term ydena el tf bt3o fy el document  ndivide by length el query * log ( nfs el value el fat / m7tgen dict tany w dh ashel shwya ndelo el term ydelna el freq bt3to fy kol el documents(uc/code) / a3tked 3dad el docuemtns)  
        query_terms = set(query.split())
        # PoQ
        query_tf = np.vectorize(lambda term: query_tf_matrix[self.count_vocab_index.get(term, self.count_vocab_index.get('__unk__'))])(list(query_terms)) # array of tf for all tokens in the query
        query_tf = query_tf / len(query.split())
        
        doc_tf = np.vectorize(lambda term: doc_dict.get(term, doc_dict.get('__unk__')))(list(query_terms))
        doc_tf = doc_tf / np.sum(list(doc_dict.values()))

        mask = doc_tf != 0
        score = np.zeros_like(query_tf)
        score[mask] = query_tf[mask] / doc_tf[mask]

        log_score = np.zeros_like(score)
        log_score[score != 0] = np.log(score[score != 0])
        
        return np.sum(query_tf * log_score)



    def simplifiedClarityScore(self,queries,query_tf_matrix,doc_dict):
        return np.vectorize(lambda i: self._simplifiedClarityScorePerQuery(queries[i],query_tf_matrix[i][:],doc_dict))(range(len(queries))).reshape(-1,1)

    def _CoherenceScorePerTerm(self,doc_tfidf_matrix:np.array)->np.float16:
        cosineSimilarity = cosine_similarity(doc_tfidf_matrix)
        # return a list of indicies of documents that contain the term based on the value in the doc_tfidf_matrix
        indices = np.where(doc_tfidf_matrix.toarray() > 0)
        if len(indices[0]) < 2:
            return 0
        # get all possible pairs of indices to access cosine similarity matrix and get the sum of all values returned
        return np.sum([cosineSimilarity[i,j] for i,j in zip(indices[0],indices[1])])/(len(indices[0])*(len(indices[0]) - 1))

    def _CoherenceScorePerQuery(self,query:str,doc_tfidf_matrix:np.array)->np.float16:
        query_terms = set(query.split())
        return 1/len(query_terms) * np.sum(np.vectorize(lambda term: self._CoherenceScorePerTerm(doc_tfidf_matrix[:,self.count_vocab_index.get(term, self.count_vocab_index.get('__unk__'))]))(list(query_terms)))

    def CoherenceScore(self,queries:list,doc_tfidf_matrix:np.array):
        return np.vectorize(lambda i: self._CoherenceScorePerQuery(queries[i],doc_tfidf_matrix))(range(len(queries))).reshape(-1,1)


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
    #  for query in UC_documents:
    #         tokens = query.split()
    #         query_variances = []  
    #         for term in tokens:
    #             term_weights = []
    #             for doc in code_documents:
    #                 tf_term_doc = doc.count(term)
    #                 weight_term_doc = (1 / len(doc)) * np.log(1 + tf_term_doc) * idf_uc_dict[term]  #
    #                 term_weights.append(weight_term_doc)

    #             avg_weight_term = np.mean(term_weights)
    #             variance_term = np.mean([(weight - avg_weight_term) ** 2 for weight in term_weights])
    #             query_variances.append(variance_term) 
    #         variance_uc.append(query_variances)  

    # def _EntropyPreProcessingPerDocument(self, token:str, document: str, idf_dict:dict ,df_dict:dict):
    #     tf_term = document.count(token)+1
    #     if token in df_dict.keys():
    #        tf_term_collection = df_dict[token] +1
    #     else:
    #        tf_term_collection = df_dict['__unk__'] +1
    #     term_entropy = ((tf_term / tf_term_collection) * np.log((tf_term / tf_term_collection) + 1))
      
    #     weight_term_doc = (1 / len(document)) * np.log(1 + tf_term-1) * idf_dict[token]  
    #     return term_entropy,weight_term_doc
    
    # def _EntropyPreProcessingPerToken(self, token:str, documents: np.ndarray, idf_dict:dict ,df_dict:dict):
    #     entropy_per_document_vectorizer = np.vectorize(lambda doc: self._EntropyPreProcessingPerDocument(token, doc,idf_dict ,df_dict))
    #     entropy_variance = entropy_per_document_vectorizer(documents)

    #     entropy_per_doc = entropy_variance[:][:][0]
    #     term_weights=entropy_variance[:][:][1]
    #     avg_weight_term = np.mean(term_weights)
    #     variance_term = np.mean([(weight - avg_weight_term) ** 2 for weight in term_weights])
    #     return np.sum(entropy_per_doc),variance_term

    # def EntropyPreProcessing(self, UC_documents:list, code_documents:list,idf_uc_dict:dict, idf_code_dict:dict ,df_uc_dict:dict,df_code_dict:dict):

    #     # UC_documents = np.array(UC_documents)
    #     # code_documents = np.array(code_documents)

    #     # entropy_per_token_vectorizer_code = np.vectorize(lambda token: self._EntropyPreProcessingPerToken(token, code_documents,idf_uc_dict, df_code_dict))
    #     # entropy_per_token_vectorizer_UC = np.vectorize(lambda token: self._EntropyPreProcessingPerToken(token, UC_documents,idf_code_dict, df_uc_dict))

    #     # entropy_code_vectorizer = np.vectorize(lambda query: self._EntropyPreProcessingPerQuery(query, UC_documents,idf_code_dict ,df_uc_dict))
    #     variance_uc=[]
    #     entropy_uc=[]
    #     variance_code=[]
    #     entropy_code=[]
    #     for query in UC_documents:
    #         tokens = query.split()
    #         # entrpoy_variance=entropy_per_token_vectorizer_code(tokens)

    #         variance_uc.append(entrpoy_variance[:][:][1])
    #         entropy_uc.append(entrpoy_variance[:][:][0])

    #     for query in code_documents:
    #         tokens = query.split()
    #         entrpoy_variance=entropy_per_token_vectorizer_UC(tokens)
    #         variance_code.append(entrpoy_variance[:][:][1])
    #         entropy_code.append(entrpoy_variance[:][:][0])

    #     return entropy_uc,entropy_code,variance_uc,variance_code
    



    def EntropyPreProcessing(self,UC_documents:list, code_documents:list,idf_uc_dict:dict,idf_cc_dict:dict, df_uc_dict:dict,df_code_dict:dict):
        entropy_uc = [] 
        variance_uc = []
       
        for query in UC_documents:
            tokens = query.split()
            query_entropy = []  
            query_variances = []  
            for term in tokens:
                term_entropy =0
                term_weights = []
                for doc in code_documents:
                    tf_term_doc = doc.count(term)+1
                    if term in df_code_dict.keys():
                       tf_term_collection = df_code_dict[term]+1
                    else:
                        tf_term_collection = df_code_dict['__unk__']+1
                    term_entropy += ((tf_term_doc / tf_term_collection) * np.log((tf_term_doc / tf_term_collection)))
                    if term in idf_uc_dict.keys():
                        weight_term_doc = (1 / len(doc)) * np.log(1 + tf_term_doc) * idf_uc_dict[term]  
                    else:
                        weight_term_doc = (1 / len(doc)) * np.log(1 + tf_term_doc) * idf_uc_dict['__unk__']
                    term_weights.append(weight_term_doc)
                term_weights_array = np.array(term_weights)
                avg_weight_term = term_weights_array.mean()
                variance_term = np.sqrt(np.mean([(weight - avg_weight_term) ** 2 for weight in term_weights]))
                query_variances.append(variance_term) 
                query_entropy.append(term_entropy)
            entropy_uc.append(query_entropy)  
            variance_uc.append(query_variances)

        entropy_code = []
        variance_code=[]
        for query in code_documents:
            tokens = query.split()
            query_entropy = []  
            query_variances = []  
            for term in tokens:
                term_entropy =0
                term_weights = []
                for doc in UC_documents:
                    tf_term_doc = doc.count(term)+1
                    if term in df_uc_dict.keys():
                        tf_term_collection = df_uc_dict[term]+1
                    else:
                        tf_term_collection = df_uc_dict['__unk__']+1
                  
                    term_entropy+=((tf_term_doc / tf_term_collection) * np.log(tf_term_doc / tf_term_collection))
                    if term in idf_cc_dict.keys():
                        weight_term_doc = (1 / len(doc)) * np.log(1 + tf_term_doc) * idf_cc_dict[term]  
                    else:
                        weight_term_doc = (1 / len(doc)) * np.log(1 + tf_term_doc) * idf_cc_dict['__unk__']
                    term_weights.append(weight_term_doc)

                avg_weight_term = np.mean(term_weights)
                variance_term = np.sqrt(np.mean([(weight - avg_weight_term) ** 2 for weight in term_weights]))
                query_variances.append(variance_term) 

                query_entropy.append(term_entropy)
            entropy_code.append(query_entropy)
            variance_code.append(query_variances)


        return entropy_uc,entropy_code,variance_uc,variance_code
   
    
        
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

    '''
        given query Q -> words: q1, q2, q3, q4, q5 the score of a document D is calculated as follows:
        for each word in query
        idf* tf(qi,D)* k1+1 / tf(qi,D) + k1 * (1 - b + b * |D| / avgdl)
        |D| length of the document
        avgdl average length of the documents
        idf of query term qi in all documents D
    '''
    
    def _BM25PerToken(self,tokens:list,idf:dict,tf:np.array,doc_len:int,avgdl:int) -> np.float16:
        
        return np.sum(np.vectorize(lambda token: idf.get(token, idf.get('__unk__')) * (tf[self.count_vocab_index.get(token, self.count_vocab_index.get('__unk__'))] * (1.2 + 1)) / (tf[self.count_vocab_index.get(token, self.count_vocab_index.get('__unk__'))] + 1.2 * (1 - 0.75 + 0.75 * (doc_len / avgdl))) , otypes=[np.float16])(tokens))

    def _BM25PerQuery(self,queries:list,idf_dict:dict,tf:np.array,doc_len:int,avgdl:int):

        return np.vectorize(lambda query: self._BM25PerToken(query.split(" ")[0:-1],idf_dict,tf,doc_len,avgdl))(queries)

    def BM25(self,queries:list,documents:list,idf_dict:dict,count_matrix:np.array) -> np.ndarray:

        avgdl = np.mean([len(doc.split()) for doc in documents])

        return np.array([self._BM25PerQuery(queries, idf_dict, count_matrix[i], len(doc.split()), avgdl) for i,doc in enumerate(documents)])
        #BM25_UC = np.array([self._BM25PerQuery(UC_documents, idf_code_dict, code_count_matrix[i], len(doc.split()), code_avgdl) for i,doc in enumerate(code_documents)])

    def SmoothingMethods(self,queries:list,documents:list,count_matrix:np.array,tf_dict:dict,JM_or_DP:bool)->np.ndarray:
        # JM -> (1-lambda) * P(w|d) + lambda * P(w|C) , P(w|d) = c(w;d) / |d| , P(w|C) = c(w;C) / |C|
        # DP -> (c(w;d) + mu * P(w|C)) / (|d| + mu)
        # |C|
        total_terms_in_corpus = np.sum([len(doc.split()) for doc in documents])
        # Per Document
        return np.array([self._SmoothingMethodsPerQuery(queries,doc,count_matrix[i],total_terms_in_corpus,tf_dict,JM_or_DP) for i,doc in enumerate(documents)])
    
    # Per Query
    def _SmoothingMethodsPerQuery(self,queries:list,doc:str,tf_matrix:np.array,total_terms_in_corpus:int,tf_corpus:dict,JM_or_DP:bool)->np.ndarray:
        return np.vectorize(lambda query: self._SmoothingMethodsPerToken(query.split(),doc,tf_matrix,total_terms_in_corpus,tf_corpus,JM_or_DP))(queries)
    
    def _SmoothingMethodsPerToken(self,tokens:list,doc:str,tf_matrix:np.array,total_terms_in_corpus:int,tf_corpus:dict,JM_or_DP:bool)->np.float16:
        _lambda = 0.7;mu = 2000
        # |d|
        doc_len = len(doc.split())
        # P(w|d) = c(w;d) / |d| , P(w|C) = c(w;C) / |C|
        # c(w;d)
        term_count_in_document = np.array([tf_matrix[self.count_vocab_index.get(token,self.count_vocab_index.get('__unk__'))] for token in tokens])
        # c(w;C)
        term_count_in_corpus = np.array([tf_corpus.get(token,tf_corpus.get('__unk__')) for token in tokens])
        # P(w|d)
        P_w_d = term_count_in_document / doc_len
        # P(w|C)
        P_w_C = term_count_in_corpus / total_terms_in_corpus
        if JM_or_DP:
            # JM -> (1-lambda) * P(w|d) + lambda * P(w|C)
            return np.sum((1 - _lambda) * P_w_d + _lambda * P_w_C)
        else:
            # DP -> (c(w;d) + mu * P(w|C)) / (|d| + mu)
            return np.sum((term_count_in_document + mu * P_w_C) / (doc_len + mu))

    def _EditDocuments(self,query:str,doc:str)->str:
        intersection = set(query.split()).intersection(set(doc.split()))
        for term in intersection:
            _lambda = doc.count(term) / len(doc)
            doc.replace(term,"")
            doc += " ".join([term] * np.random.poisson(_lambda))
        return doc
    
    def _RobustnessScorePerQuery(self,query:str,documents:list,feature_extraction_type,Results,train_or_test:str="train")->np.ndarray:
        top20 = np.argsort(Results)[::-1][:20] # arrange in descending order
        L = np.array(documents)[top20]
        new_L = np.vectorize(lambda doc: self._EditDocuments(query,doc))(L)

        query_count_matrix, doc_count_matrix,_,tf_doc_dict = self.CountVectorizerModel([query], new_L, train_or_test)
        tfidf_matrix_query, tfidf_matrix_doc,_,idf_doc_dict,_,_ ,_,_= self.TFIDFVectorizer([query], new_L,train_or_test)

        new_Results = None
        if feature_extraction_type == "BM":
            new_Results = self.BM25([query],new_L,idf_doc_dict,doc_count_matrix)
        elif feature_extraction_type == "JM":
            new_Results = self.SmoothingMethods([query],new_L,doc_count_matrix,tf_doc_dict,JM_or_DP=True)
        elif feature_extraction_type == "DP":
            new_Results = self.SmoothingMethods([query],new_L,doc_count_matrix,tf_doc_dict,JM_or_DP=False)
        elif feature_extraction_type == "JS" :
            new_Results = self.JensenShannon(query_count_matrix, doc_count_matrix)
        else: # VSM
            new_Results = self.VectorSpaceModel(tfidf_matrix_query, tfidf_matrix_doc)

        new_top20 = np.argsort(new_Results)[::-1][:20] # arrange in descending order
        
        # (5) Compute the Spearman rank correlation between the positions of the 50 documents in L and the positions of their corresponding perturbed documents in new_L
        _,new_L_ranks  = np.unique(new_L, return_inverse=True)
        _,new_L_top20_ranks = np.unique(new_L[new_top20].flatten(), return_inverse=True)

        return spearmanr(new_L_ranks,new_L_top20_ranks).correlation if (np.std(new_L_ranks) != 0 and np.std(new_L_top20_ranks) != 0) else 0 ,  1 if new_L[0] == new_L[new_top20].flatten()[0] else 0



    def RobustnessScore(self,queries:list,documents:list,train_or_test:str = "train",feature_extraction_type:str="JS")->np.array:
        Results = None
        query_count_matrix, doc_count_matrix,_,tf_doc_dict = self.CountVectorizerModel(queries, documents,train_or_test)

        if feature_extraction_type == "BM":
            _, _,_,idf_doc_dict,_,_ ,_,_= self.TFIDFVectorizer(queries, documents,train_or_test)
            Results = self.BM25(queries,documents,idf_doc_dict,doc_count_matrix)
        elif feature_extraction_type == "JM":
            Results = self.SmoothingMethods(queries,documents,doc_count_matrix,tf_doc_dict,JM_or_DP=True)
        elif feature_extraction_type == "DP":
            Results = self.SmoothingMethods(queries,documents,doc_count_matrix,tf_doc_dict,JM_or_DP=False)
        elif feature_extraction_type == "JS" :
            Results = self.JensenShannon(query_count_matrix, doc_count_matrix)
        else: # VSM
            tfidf_matrix_query, tfidf_matrix_doc,_,_,_,_ ,_,_= self.TFIDFVectorizer(queries, documents,train_or_test)
            Results = self.VectorSpaceModel(tfidf_matrix_query, tfidf_matrix_doc)

        if Results.shape[0] != len(documents):
            Results = Results.T

        return np.vectorize(lambda i: self._RobustnessScorePerQuery(queries[i],documents,feature_extraction_type,Results[:,i]))(range(len(queries)))



    # Clustering Tendency
    def _SimQuery(self,d_i:int,d_j:int,query_tf_idf_vector:np.array,doc_tf_idf_matrix:np.array,c:np.array)->np.float16:

        term1 = cosine_similarity(doc_tf_idf_matrix[d_i,:],doc_tf_idf_matrix[d_j,:])
        term2 = cosine_similarity(query_tf_idf_vector,c.A)
        
        return term1 * term2

    def _getNN(self,d_mp:int,top20:np.array,Results:np.array)->np.int16:
        top20 = top20.tolist()
        if top20.index(d_mp) == 0:
            return top20[top20.index(d_mp) + 1]
        elif top20.index(d_mp) == 19:
            return top20[top20.index(d_mp) - 1]
        elif abs(Results[top20[top20.index(d_mp) - 1]] - Results[d_mp]) < abs(Results[top20[top20.index(d_mp) + 1]] - Results[d_mp]) :
            return top20[top20.index(d_mp) - 1]
        else:
            return top20[top20.index(d_mp) + 1]
    
    def _getTermOne(self,i:int,top20:np.array,top20_excluded:np.array,cosineSimilarity:np.array,doc_tf_idf_matrix:np.array,query_tf_idf_matrix,Results:np.array)->np.float16:
        p_sp = np.random.choice(top20_excluded)
        d_mp = top20[np.argmax([cosineSimilarity[p_sp, col] for col in top20])] # most similar document to p_sp in terms of cosine similarity
        d_nn = self._getNN(d_mp,top20,Results)

        c = (doc_tf_idf_matrix[d_mp,:].todense()  + doc_tf_idf_matrix[d_nn,:].todense() ) / 2        

        num = self._SimQuery(d_mp,d_nn,query_tf_idf_matrix,doc_tf_idf_matrix,c)
        den = self._SimQuery(p_sp,d_mp,query_tf_idf_matrix,doc_tf_idf_matrix,c)

        return num/den if den != 0 else 0


    def _ClusteringTendencyPerQuery(self,Results:np.array,query_tf_idf_matrix:np.array,doc_tf_idf_matrix:np.array)->np.float16:
        """
            Results: (D,1) , query_tf_idf_matrix: (D,1) , doc_tf_idf_matrix: (D,1)
        """
        Ordered_Results_Index = np.argsort(Results)[::-1] # arrange in descending order 
        top20 = Ordered_Results_Index[:20]
        top20_excluded = Ordered_Results_Index[20:]
        cosineSimilarity = cosine_similarity(doc_tf_idf_matrix)
        
        term_1 = np.mean(np.vectorize(lambda i :self._getTermOne(i,top20,top20_excluded,cosineSimilarity,doc_tf_idf_matrix,query_tf_idf_matrix,Results))(np.arange(100)))
        
        #x_i and y_i
        x_i = np.max(doc_tf_idf_matrix[top20, :], axis=0)
        y_i = np.min(doc_tf_idf_matrix[top20, :], axis=0)
        term_2 = np.mean(x_i - y_i)

        return term_1 * term_2
    
    def ClusteringTendency(self,Results:np.array,query_tf_idf_matrix:np.array,doc_tf_idf_matrix:np.array)->np.ndarray:
        return np.vectorize(lambda i: self._ClusteringTendencyPerQuery(Results[:,i],query_tf_idf_matrix[i,:],doc_tf_idf_matrix))(range(Results.shape[1])).reshape(-1,1)
    
    # Spatial Auto correlation
    def _SpatialAutoCorrelationPerQuery(self,Results:np.array,doc_tf_idf_matrix:np.array)->np.ndarray:
        top20 = np.argpartition(Results, -20)[-20:]
        L = Results[top20]
        cosineSimilarity = cosine_similarity(doc_tf_idf_matrix[top20,:])
        L_new = np.vectorize(lambda i : np.mean(L[np.argsort(cosineSimilarity[i,:])[-6:-1]]))(range(20)) # remove the document itself
        return pearsonr(L,L_new)[0] if np.std(L) != 0 and np.std(L_new) != 0 else 0

    def SpatialAutoCorrelation(self,Results:np.array,doc_tf_idf_matrix:np.array)->np.ndarray:
        return np.vectorize(lambda i: self._SpatialAutoCorrelationPerQuery(Results[:,i],doc_tf_idf_matrix))(range(Results.shape[1])).reshape(-1,1)
    
    # Weighted Information Gain
    
    def _WeightedInformationGainPerToken(self,token:str,document:str,tf_dict:dict,total_corpus_size:int,_lambda:np.float16)->np.float16:

        term_count = document.count(token); doc_len = len(document); tf = tf_dict.get(token, tf_dict.get('__unk__'))
        if term_count == 0 or doc_len == 0 or tf == 0 or total_corpus_size == 0:
            return 0

        ratio =  (term_count / doc_len)/ (tf / total_corpus_size)
        if ratio <= 0:
            return 0
        return _lambda * np.log2(ratio)
    
    def _WeightedInformationGainPerDocument(self,query:str,document:str,tf_dict:dict,total_corpus_size:int,_lambda:np.float16)->np.float16:
        # per token
        return np.sum(np.vectorize(lambda token:self._WeightedInformationGainPerToken(token,document,tf_dict,total_corpus_size,_lambda))(query.split()))

    def _WeightedInformationGainPerQuery(self,query:str,documents:list,Results:np.array,tf_dict:dict,total_corpus_size:int)->np.float16:
        top10 = np.argpartition(Results, -10)[-10:]
        L = np.array(documents)[top10]
        _lambda = 1/np.sqrt(len(query.split()))
        # per document
        return (1/10)*np.sum(np.vectorize(lambda doc: self._WeightedInformationGainPerDocument(query,doc,tf_dict,total_corpus_size,_lambda))(L))
    
    def WeightedInformationGain(self,queries:list,documents:list,Results:np.array,tf_dict:dict,total_corpus_size:int)->np.ndarray:
        # per query
        return np.vectorize(lambda i,query: self._WeightedInformationGainPerQuery(query,documents,Results[:,i],tf_dict,total_corpus_size))(range(len(queries)),queries).reshape(-1,1)

    # Normalized Query Commitment
    
    def _NormalizedQueryCommitmentPerQuery(self,Results:np.array)->np.float16:
        top10 = np.argpartition(Results, -10)[-10:]
        Score_Dq = np.sum(Results)
        mu = (1/10)*np.sum(Results[top10])
        # per document
        return np.sqrt((1/10)*np.sum([pow(Results[i]-mu,2) for i in top10]))/Score_Dq
    
    def NormalizedQueryCommitment(self,Results:np.array)->np.ndarray:
        # per query
        return np.vectorize(lambda i,: self._NormalizedQueryCommitmentPerQuery(Results[:,i]))(range(Results.shape[1])).reshape(-1,1)


    