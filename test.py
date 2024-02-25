

UniqueTokens = list(UCTokens)
T = len(UniqueTokens)
vectorizer = TfidfVectorizer(vocabulary=UniqueTokens)
# d_i = code_documents[10]
# d_j = code_documents[13]
# query = UC_documents[4]

# sim_query = SimQuery(UniqueTokens,d_i,d_j,query)
# print(sim_query)

'''
CT = Mean(sim_query(dmp,dnn|q)/sim_query(psp,dmp|q)) * (1/T)* sum(xi-yi)
psp -> random doc not in L
dmp -> doc in L with max similarity with psp
dnn -> doc in L closest to dmp
xi -> max tf-idf value of term i in L
yi -> min tf-idf value of term i in L
'''

# get corpus - L
# L = tuple(code,score)
L = [(code, score) for code, score in sorted(R[0].items(), key=lambda item: item[1], reverse=True)][:50]
not_L = [code for code, _ in sorted(R[0].items(), key=lambda item: item[1], reverse=False)][50:]

mean_sim_query = 0


# Iterate over each document in L
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
    ClusteringTendency = mean_sim_query * (1/T) * sum_xi_yi
print(ClusteringTendency)