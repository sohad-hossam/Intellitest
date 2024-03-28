from PreProcessor import *
from FeatureExtraction import *

# Preprocessor & Setup
_PreProcessor = PreProcessor()
UC_documents, code_documents, UCTokens, CodeTokens = _PreProcessor.setup("./UC", "./CC")

intersection = set(UC_documents[2].split()).intersection(set(code_documents[0].split()))
print(intersection)
print(np.vectorize(lambda term: code_documents[0].split().count(term))(list(intersection)))
# Robustness Score

# Vector Space Model
# UCTokens.update(CodeTokens)

# (1) Run the original query q, and obtain the result list R
#R = FeatureExtraction(UCTokens).VectorSpaceModel(UC_documents, code_documents)

#print (len(UC_documents), len(code_documents))
# R = FeatureExtraction(UCTokens)._BM25(UC_documents, code_documents)

# (2) Take the top documents in R and consider them as ranked list L
#query = PreProcessor().UCPreProcessor("./UC/UC1.TXT")

"""
(3) For each document d in L, get a perturbed document d	 from d in the following way:
    (a) All terms t from the corpus that donot appear in document d, will not be included in d neither
    (b) All terms t from the corpus that appear in document d with frequency f,but do not appear in the query will appear in document d	 with the same frequency f
    (c) Each term t that appears in d with frequency f and appears also in the query q will appear also in d', but with a frequency f', which is a random
    number obtained from a Poisson distribution P(λ) with λ = f
"""
def RobustnessScore(UniqueTokens, R, query):
    L = [code for code, _ in sorted(R[0].items(), key=lambda item: item[1], reverse=True)][:50]
    tokens = set(query.split())
    sum_correlation = 0
    for i in range(100):
        for code in L:
            for token in tokens:
                if token in code:
                    _lambda = code.count(token)
                    code = code.replace(token, "")
                    code += " ".join([token] * np.random.poisson(_lambda))

        # (4) The new 50 documents obtained are ranked according to the query q, resulting in a second ranked list L, where each document corresponds to a document in L
        R = FeatureExtraction(UCTokens).VectorSpaceModel([query], L)
        L_ = [code for code, _ in sorted(R[0].items(), key=lambda item: item[1], reverse=True)]

        # (5) Compute the Spearman rank correlation between the positions of the 50 documents in L and the positions of their corresponding perturbed documents in L' and record the correlation obtained
        encoder = LabelEncoder()
        LRank = encoder.fit_transform(L)
        L_Rank = encoder.fit_transform(L_)
        correlation, _ = spearmanr(LRank, L_Rank)
        sum_correlation += correlation

    #(6) Repeat steps 8 to 8 100 times, and the final robustness score is the average Spearman correlation between the 100 r
    RobustnessScore = sum_correlation / 100
    return RobustnessScore

# First Rank Change

'''
(3) For each document d in L, get a perturbed document d from d in the following way:
    (a) All terms t from the corpus that do not appear in document d, will not be included in d neither
    (b) All terms t from the corpus that appear in document d with frequency f, but do not appear in the query will appear in document d' with the same frequency f.
    (c) Each term t that appears in d with frequency f and appears also in the query q will appear also in d', but with a frequency f, which is a random number obtained from a Poisson distribution P(λ) with λ = f
'''
def FirstRankChange(UniqueTokens, R, query):
    L = [code for code, _ in sorted(R[0].items(), key=lambda item: item[1], reverse=True)][:50]
    tokens = set(query.split())
    sum_top_ranked = 0
    for i in range(100):
        for code in L:
            for token in tokens:
                if token in code:
                    _lambda = code.count(token)
                    code = code.replace(token, "")
                    code += " ".join([token] * np.random.poisson(_lambda))
                else:
                    f = code.count(token)
                    code += " ".join([token] * f)

    # (4) The new 50 documents obtained are ranked according to the query q, resulting in a second ranked list L', where each document corresponds to a document in L
        R = FeatureExtraction(UCTokens).VectorSpaceModel([query], L)
        L_ = [code for code, _ in sorted(R[0].items(), key=lambda item: item[1], reverse=True)]

    # (5) Record a 1 if the top ranked document in L is also the top ranked document (after perturbation) in L,and record 0 otherwise
        if L[0] == L_[0]:
            sum_top_ranked += 1

    # (6) Repeat steps 8 to 8 100 times, and the final score is the sum of the values (0 or 1) obtained in step 8 for all the 100 runs
    FirstRankChange = sum_top_ranked/100
    return FirstRankChange

'''Clustering Tendency'''

'''
UC_Tokens -> unique terms in corpus
T -> # of unique terms in corpus

for docs di and dj, dik and djk are tf-idf value of term k in document

for common terms in both di and dj, compute ck as (djk+dik)/2
qk is the tf-idf of term k in query

for k term 1 to T
    get dik and djk
    if k in dik and k in djk:
        ck = (djk+dik)/2
    get qk

apply sim_query equation
'''

def SimQuery(vectorizer,UniqueTokens,d_i,d_j,query):
    T = len(UniqueTokens)
    tf_idf = vectorizer.fit_transform([d_i, d_j, query])

    '''
    tf_idf[0] -> d_i
    tf_idf[1] -> d_j
    tf_idf[2] -> query
    '''

    '''
    norm_di = sum(dik^2)
    norm_dj = sum(djk^2)
    norm_q = sum(qk^2)
    norm_c = sum(ck^2)

    term1 = sum(dik*djk)
    term2 = sum(ck*qk)

    term1 = term1 / sqrt(norm_di*norm_dj)
    term2 = term2 / sqrt(norm_c*norm_q)

    '''

    norm_di = 0; norm_dj = 0; norm_q = 0; norm_c = 0; term1 = 0; term2 = 0

    for k in range(T):
        dik = tf_idf[0, k]
        djk = tf_idf[1, k]
        qk = tf_idf[2, k]
        ck = 0

        if UniqueTokens[k] in d_i and UniqueTokens[k] in d_j:
            ck = (djk + dik) / 2

        term1 += dik * djk 
        term2 += ck * qk
        norm_di += dik**2
        norm_dj += djk**2
        norm_q += qk**2
        norm_c += ck**2

    term1 = term1 / np.sqrt(norm_di*norm_dj)
    term2 = term2 / np.sqrt(norm_c*norm_q)

    sim_query = term1 * term2
    return sim_query

def ClusterTendency(UniqueTokens,R,query):
    T = len(UniqueTokens)
    vectorizer = TfidfVectorizer(vocabulary=UniqueTokens)

    '''
    CT = Mean(sim_query(dmp,dnn|q)/sim_query(psp,dmp|q)) * (1/T)* sum(xi-yi)
    psp -> random doc not in L
    dmp -> doc in L with max similarity with psp
    dnn -> doc in L closest to dmp
    xi -> max tf-idf value of term i in L
    yi -> min tf-idf value of term i in L
    '''
    L = [(code, score) for code, score in sorted(R[0].items(), key=lambda item: item[1], reverse=True)][:50]
    not_L = [code for code, _ in sorted(R[0].items(), key=lambda item: item[1], reverse=False)][50:]

    # get sum(xi-yi)
    sum_xi_yi = 0
    for doc,_ in L:
        # Calculate tf-idf values for each token in UniqueTokens
        tfidf_matrix = vectorizer.fit_transform([doc])
        for token in UniqueTokens:
            min_tfidf = 100
            max_tfidf = 0
            if token in vectorizer.vocabulary_:
                tfidf_value = tfidf_matrix[0, vectorizer.vocabulary_[token]]
                # Update min and max tf-idf values
                min_tfidf = min(min_tfidf, tfidf_value)
                max_tfidf = max(max_tfidf, tfidf_value)
            sum_xi_yi += max_tfidf - min_tfidf
    
    mean_sim_query = 0
    for i in range(100):
        # get psp by random
        psp = random.choice(not_L)

        # get dmp by max similarity with psp
        sim_query_psp_dmp = 0
        for doc,_ in L:
            sim = SimQuery(vectorizer,UniqueTokens,psp,doc,query)
            if sim > sim_query_psp_dmp:
                sim_query_psp_dmp = sim
                dmp = doc

        # get dnn by doc in L closest to dmp by getting the closest value of vector space model to dmp in L
        # find dmp in L and get the doc before and after it
        for i,tup in enumerate(L):
            if tup[0] == dmp:
                if i-1 >= 0 and i+1 < len(L):
                    dnn = L[i-1][0] if abs(tup[1]-L[i-1][1]) > abs(tup[1]-L[i+1][1]) else L[i+1][0]
                elif i-1 >= 0:
                    dnn = L[i-1][0]
                else:
                    dnn = L[i+1][0]
        
        sim_query_dmp_dnn = SimQuery(vectorizer,UniqueTokens,dmp,dnn,query)
        mean_sim_query += sim_query_dmp_dnn/sim_query_psp_dmp
    mean_sim_query = mean_sim_query/100
    print(mean_sim_query)
    ClusteringTendency = mean_sim_query * (1/T) * sum_xi_yi
    print(sum_xi_yi)
    return ClusteringTendency


#Spatial Auto-correlation
def SpatialAutoCorrelation(UniqueTokens,R,query):
    #(2) Take the top 50 documents in R and consider them as ranked list L
    L = [(code,score) for code, score in sorted(R[0].items(), key=lambda item: item[1], reverse=True)][:50]
    #(3) For each document d in L, compute the cosine similarity between d and the rest of the documents in L,using tf−idf as the weight of the terms in the document vectors
    vectorizer = TfidfVectorizer(vocabulary=UniqueTokens)
    L_ = []
    for doc,score in L:
        rest_of_docs = L.copy()
        rest_of_docs.remove((doc,score))
        
        tfidf_doc = vectorizer.fit_transform([doc])
        tfidf_rest_of_docs = vectorizer.fit_transform([code for code,_ in rest_of_docs])

        cosine_similarities = cosine_similarity(tfidf_doc, tfidf_rest_of_docs)
        #(4) Among the documents in L, select the 5 documents that are most similar to d according to the cosine similarity
        similarity_dict = {}
        for j in range(cosine_similarities.shape[1]):
            similarity_dict[rest_of_docs[j]] = cosine_similarities[0][j]
        top_5_similar_docs = [tup for tup,minor_score in sorted(similarity_dict.items(), key=lambda item: item[1], reverse=True)][:5]
        #(5) Let s be the score of document d in L. Assign a new score to d, which is the average score of the 5 most similar documents to it according to the cosine similarity
        new_score = sum([score for _,score in top_5_similar_docs])/5
        L_.append((doc,new_score))

    #(6) The Pearson correlation between the original scores of the documents in L and the derived scores of those documents represents the index of spatial autocorrelation
    correlation, _ = pearsonr([score for _,score in L], [score for _,score in L_])
    return correlation

# Weighted Information Gain
def WeightedInformationGain(R,query):
    '''
    q,query
    t, a term in the query q
    D, set of all documents in corpus
    Dq, the set of documents in the result set to query q
    Dqk,thetopkdocuments in the result list
    k, the number of top documents to consider
    |q|, number of terms in the query q
    λ(t) = 1/√|q|
    Pr(t|D) is the probability of term t occurring in the entire collection D.
    Pr(t|d) is the probability of term t occurring in the specific document d.
    
    WIG(q) = (1/k)*sum_over_top_K_docs_in_L(sum_over_terms_in_query(λ(t)*log2(Pr(t|Dqk)/Pr(t|D))))
    
    '''
    L = [(code, score) for code, score in sorted(R[0].items(), key=lambda item: item[1], reverse=True)][:10]
    query_list = query.split()
    query_len = len(query_list)
    _lambda = 1/np.sqrt(query_len)
    WIG = 0
    for doc,_ in L:
        for term in query_list:
            Pr_t_Dqk = doc.count(term)/len(doc)
            Pr_t_D = sum([code.count(term) for code,_ in L])/len(L)
            WIG += _lambda * np.log2(Pr_t_Dqk/Pr_t_D)
    WIG = WIG/10
    return WIG

def NormalizedQueryCommitment(R,query):
    '''
    NCQ = sqrt((1/k)*sum_over_top_K_docs_in_L())
    '''
    pass

# print(R)