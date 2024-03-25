from imports import *
from FeatureExtraction import *
from VSM import *

_PreProcessor = PreProcessor()
UC_documents, code_documents, UCTokens, CodeTokens = _PreProcessor.setup(
            "./UC", "./CC"
        )
UCTokens.update(CodeTokens)


DataSet = pd.read_csv("eTour.csv", names=['UC', 'CC'])
temp = set()
for row in DataSet.itertuples(index=False, name=None):
    temp.add((row[0].lower(), row[1].lower()))

DataSet['Labels'] = np.ones(len(DataSet), np.uint16)

for filename_UC in os.listdir("./UC"):
    for filename_CC in os.listdir("./CC"):
        if (filename_UC.lower(), filename_CC.lower()) not in temp:
            DataSet.loc[len(DataSet)] = [filename_UC, filename_CC, 0]

DataSet.to_csv('eTourModified.csv', index = False)             

DataSet = pd.read_csv("eTourModified.csv")

# print(DataSet)
##### incorrect data / duplicated data / NA 
# DataSet.dropna(inplace=True)
# print(DataSet.duplicated().to_string())

###### TO DO: Add the rest of the negative class

for row in DataSet.index:
    UC = DataSet.loc[row, 'UC'].lower()
    CC = DataSet.loc[row, 'CC'].lower()
    if UC in _PreProcessor.UC_to_index:
        DataSet.loc[row, 'UC'] = _PreProcessor.UC_to_index[UC]
    if CC in _PreProcessor.CC_to_index:
        DataSet.loc[row, 'CC'] = _PreProcessor.CC_to_index[CC]

# print(DataSet.to_string())

featureExtraction = FeatureExtraction(UCTokens)

###### used for all ######
tfidf_matrix_uc, tfidf_matrix_code,idf_uc_dict,idf_code_dict,feature_names_uc,feature_names_code ,df_uc_dict,df_code_dict= featureExtraction.TFIDFVectorizer(UC_documents, code_documents)
UC_count_matrix, code_count_matrix = featureExtraction.CountVectorizerModel(UC_documents, code_documents, 'train')
idf_uc_q,idf_code_q= featureExtraction.IDFPreProcessing(UC_documents,idf_code_dict,code_documents,idf_uc_dict)
# ictf_uc_q,ictf_code_q=featureExtraction.ICTFPreProcessing(UC_documents,tf_code_dict,code_documents,tf_uc_dict)
# entropy_uc,entropy_code=featureExtraction.EntropyPreProcessing(UC_documents,code_documents,df_uc_dict,df_code_dict)
# variance_uc,variance_code= featureExtraction.VarPreProcessing(UC_documents,code_documents,idf_uc_dict,idf_code_dict)
# PMI_uc,PMI_code=featureExtraction.PMIPreProcessing(code_documents,UC_documents)
# SCQ_uc,SCQ_code = featureExtraction.SCQPreProcessing(UC_documents,code_documents,tf_uc_dict,tf_code_dict,idf_uc_dict,idf_code_dict)


# the values of the count matrices are normalized

#-------------------------14 IR based features--------------------------#

# # 1) Vector space model
# cosine_similarities_feature = featureExtraction.VectorSpaceModel(tfidf_matrix_uc, tfidf_matrix_code)
# print("cosine_similarities_feature", cosine_similarities_feature.shape)
    
# cosine_similarity_UC = rankdata(-cosine_similarities_feature, method='dense', axis=1)
# print("cosine_similarity_UC", cosine_similarity_UC.shape)
# cosine_similarity_CC = rankdata(-cosine_similarities_feature, method='dense', axis=0)
# print("cosine_similarity_CC", cosine_similarity_CC.shape)

# # 2) Latent semantic analysis
# LSA_similraities_feature = featureExtraction.LSA(tfidf_matrix_uc, tfidf_matrix_code)
# print('LSA similarity', LSA_similraities_feature.shape)

# LSA_similraities_UC = rankdata(-LSA_similraities_feature, method='dense', axis=1)
# print("LSA_similraities_UC", LSA_similraities_UC.shape)
# LSA_similraities_CC = rankdata(-LSA_similraities_feature, method='dense', axis=0)
# print("LSA_similraities_CC", LSA_similraities_CC.shape)

# # 3) Latent Dirichlet Allocation
# DocumentTopicDisUC_dense, DocumentTopicDisCode_dense, cosine_similarities_LDA= featureExtraction.LDA(UC_documents,code_documents,UCTokens)
# print('LDA similarity', cosine_similarities_LDA.shape)

# LDA_similraities_UC = rankdata(-cosine_similarities_LDA, method='dense', axis=1)
# print("LDA_similraities_UC", LDA_similraities_UC.shape)
# LDA_similraities_CC = rankdata(-cosine_similarities_LDA, method='dense', axis=0)
# print("LDA_similraities_CC", LDA_similraities_CC.shape)

# # 4) Jensen-Shannon(JS)
# JS_features = featureExtraction.JensenShannon(UC_count_matrix, code_count_matrix)
# print('JS', JS_features.shape)

# JS_UC = rankdata(-JS_features, method='dense', axis=1)
# print('JS_UC', JS_UC.shape)
# JS_CC = rankdata(-JS_features, method='dense', axis=0)
# print('JS_CC', JS_CC.shape)

# # 5)  Okapi BM25
BM25_feature = featureExtraction.BM25(UC_documents,code_documents,idf_uc_dict,UC_count_matrix,idf_code_dict,code_count_matrix)
print("BM25_feature", BM25_feature.shape)

# # 6) Language Model with Dirichlet


# # ------------------------pre-retrieval (21 metrics)--------------------------#
# # 1) IDF Features
# avg_idf_uc= featureExtraction.AvgIDF(idf_uc_q)
# avg_idf_code= featureExtraction.AvgIDF(idf_code_q)
# print('avg_idf_uc_shape', avg_idf_uc.shape) 
# print('avg_idf_code_shape', avg_idf_code.shape) 
# max_idf_uc= featureExtraction.MaxIDF(idf_uc_q)
# max_idf_code= featureExtraction.MaxIDF(idf_code_q)
# print('max_idf_uc_shape', max_idf_uc.shape) 
# print('max_idf_code_shape', max_idf_code.shape) 

# dev_idf_uc= featureExtraction.DevIDF(idf_uc_q)
# dev_idf_code= featureExtraction.DevIDF(idf_code_q)

# print('dev_idf_uc_shape', dev_idf_uc.shape) 
# print('dev_idf_code_shape', dev_idf_code.shape) 

# # 2) ICTF Features
# avg_ictf_uc= featureExtraction.AvgICTF(ictf_uc_q)
# avg_ictf_code= featureExtraction.AvgICTF(ictf_code_q)

# print('avg_ictf_uc_shape', avg_ictf_uc.shape)
# print('avg_ictf_code_shape', avg_ictf_code.shape)

# max_ictf_uc= featureExtraction.MaxICTF(ictf_uc_q)
# max_ictf_code= featureExtraction.MaxICTF(ictf_code_q)

# print('max_ictf_uc_shape', max_ictf_uc.shape)
# print('max_ictf_code_shape', max_ictf_code.shape)

# dev_ictf_uc= featureExtraction.DevICTF(ictf_uc_q)
# dev_ictf_code= featureExtraction.DevICTF(ictf_code_q)

# print('dev_ictf_uc_shape', dev_ictf_uc.shape)
# print('dev_ictf_code_shape', dev_ictf_code.shape)
# # 3) Entropy Features

# avg_entropy_uc= featureExtraction.AvgEntropy(entropy_uc)
# avg_entropy_code= featureExtraction.AvgEntropy(entropy_code)

# print('avg_entropy_uc_shape', avg_entropy_uc.shape)
# print('avg_entropy_code_shape', avg_entropy_code.shape)

# max_entropy_uc= featureExtraction.MaxEntropy(entropy_uc)
# max_entropy_code= featureExtraction.MaxEntropy(entropy_code)

# print('max_entropy_uc_shape', max_entropy_uc.shape)
# print('max_entropy_code_shape', max_entropy_code.shape)

# med_entropy_uc= featureExtraction.MedEntropy(entropy_uc)
# med_entropy_code= featureExtraction.MedEntropy(entropy_code)

# print('med_entropy_uc_shape', med_entropy_uc.shape)
# print('med_entropy_code_shape', med_entropy_code.shape)

# dev_entropy_uc= featureExtraction.DevEntropy(entropy_uc)
# dev_entropy_code= featureExtraction.DevEntropy(entropy_code)

# print('dev_entropy_uc_shape', dev_entropy_uc.shape)
# print('dev_entropy_code_shape', dev_entropy_code.shape)
      

# # 4) Variance Features

# avg_variance_uc= featureExtraction.AvgVariance(variance_uc) 
# avg_variance_code= featureExtraction.AvgVariance(variance_code)

# print('avg_variance_uc_shape', avg_variance_uc.shape)
# print('avg_variance_code_shape', avg_variance_code.shape)

# max_variance_uc= featureExtraction.MaxVariance(variance_uc)
# max_variance_code= featureExtraction.MaxVariance(variance_code)

# print('max_variance_uc_shape', max_variance_uc.shape)
# print('max_variance_code_shape', max_variance_code.shape)

# sum_variance_uc= featureExtraction.SumVariance(variance_uc)
# sum_variance_code= featureExtraction.SumVariance(variance_code)

# print('sum_variance_uc_shape', sum_variance_uc.shape)
# print('sum_variance_code_shape', sum_variance_code.shape)
# # 5) SCQ Features
# avg_scq_uc= featureExtraction.AvgSCQ(SCQ_uc)
# avg_scq_code= featureExtraction.AvgSCQ(SCQ_code)

# print('avg_scq_uc_shape', avg_scq_uc.shape)
# print('avg_scq_code_shape', avg_scq_code.shape)

# max_scq_uc= featureExtraction.MaxSCQ(SCQ_uc)
# max_scq_code= featureExtraction.MaxSCQ(SCQ_code)

# print('max_scq_uc_shape', max_scq_uc.shape)
# print('max_scq_code_shape', max_scq_code.shape)

# sum_sqc_uc= featureExtraction.SumSCQ(SCQ_uc)
# sum_sqc_code= featureExtraction.SumSCQ(SCQ_code)

# print('sum_sqc_uc_shape', sum_sqc_uc.shape)
# print('sum_sqc_code_shape', sum_sqc_code.shape)

# # 6) PMI Features

# avg_pmi_uc= featureExtraction.AvgPMI(PMI_uc)
# avg_pmi_code= featureExtraction.AvgPMI(PMI_code)

# print('avg_pmi_uc_shape', avg_pmi_uc.shape)
# print('avg_pmi_code_shape', avg_pmi_code.shape)

# max_pmi_uc= featureExtraction.MaxPMI(PMI_uc)
# max_pmi_code= featureExtraction.MaxPMI(PMI_code)

# print('max_pmi_uc_shape', max_pmi_uc.shape)
# print('max_pmi_code_shape', max_pmi_code.shape)
# # 7) QS Features

# qs_uc= featureExtraction.QS(UC_documents,code_documents)
# print('qs_uc_shape', qs_uc.shape)

# qs_code = featureExtraction.QS(code_documents,UC_documents)
# print('qs_code_shape', qs_code.shape)



# #------------------------post-retrieval (7 metrics)--------------------------#
# # 1.1) Subquery overlap using jensenShannon
# code_queries_score_JensenShannon = featureExtraction.SubqueryOverlap(featureExtraction, code_documents, featureExtraction.JensenShannon, UC_count_matrix, code_count_matrix)
# UC_queries_score_JensenShannon = featureExtraction.SubqueryOverlap(featureExtraction, UC_documents, featureExtraction.JensenShannon, code_count_matrix, UC_count_matrix)

# print('code_queries_score_JensenShannon', code_queries_score_JensenShannon.shape)
# print('UC_queries_score_JensenShannon', UC_queries_score_JensenShannon.shape)

# # 1.2) Subquery overlap using VSM
# code_queries_score_VSM = featureExtraction.SubqueryOverlap(featureExtraction, code_documents, featureExtraction.VectorSpaceModel, UC_count_matrix, code_count_matrix)
# UC_queries_score_VSM = featureExtraction.SubqueryOverlap(featureExtraction, UC_documents, featureExtraction.VectorSpaceModel, code_count_matrix, UC_count_matrix)

# print('code_queries_score_VSM', code_queries_score_VSM.shape)
# print('UC_queries_score_VSM', UC_queries_score_VSM.shape)

# #------------------------Document Statistics Features--------------------------#
# num_terms_code, num_terms_UC, num_unique_terms_code, num_unique_terms_UC, num_overlapping_terms = featureExtraction.DocumentStatistics(UC_documents, code_documents)


# ###### Remove hardcoded sizing with the size captured from the data set.
# avg_idf_uc_reshaped = np.tile(avg_idf_uc, (116,1))
# avg_idf_code_reshaped = np.tile(avg_idf_code, (58,1))

# max_idf_uc_reshaped = np.tile(max_idf_uc, (116,1))
# max_idf_code_reshaped = np.tile(max_idf_code, (58,1))

# dev_idf_uc_reshaped = np.tile(dev_idf_uc, (116,1))
# dev_idf_code_reshaped = np.tile(dev_idf_code, (58,1))

# avg_ictf_uc_reshaped = np.tile(avg_ictf_uc, (116, 1))
# avg_ictf_code_reshaped = np.tile(avg_ictf_code, (58, 1))

# max_ictf_uc_reshaped = np.tile(max_ictf_uc, (116, 1))
# max_ictf_code_reshaped = np.tile(max_ictf_code, (58, 1))

# dev_ictf_uc_reshaped = np.tile(dev_ictf_uc, (116, 1))
# dev_ictf_code_reshaped = np.tile(dev_ictf_code, (58, 1))

# avg_entropy_uc_reshaped = np.tile(avg_entropy_uc, (116, 1))
# avg_entropy_code_reshaped = np.tile(avg_entropy_code, (58, 1))

# max_entropy_uc_reshaped = np.tile(max_entropy_uc, (116, 1)) # ------> gets removed, 2nd round
# max_entropy_code_reshaped = np.tile(max_entropy_code, (58, 1)) # ------> gets removed, 2nd round

# med_entropy_uc_reshaped = np.tile(med_entropy_uc, (116, 1))
# med_entropy_code_reshaped = np.tile(med_entropy_code, (58, 1))

# dev_entropy_uc_reshaped = np.tile(dev_entropy_uc, (116, 1))
# dev_entropy_code_reshaped = np.tile(dev_entropy_code, (58, 1)) # -------> gets removed

# avg_variance_uc_reshaped = np.tile(avg_variance_uc, (116, 1))
# avg_variance_code_reshaped = np.tile(avg_variance_code, (58, 1))

# max_variance_uc_reshaped = np.tile(max_variance_uc, (116, 1))
# max_variance_code_reshaped = np.tile(max_variance_code, (58, 1))

# sum_variance_uc_reshaped = np.tile(sum_variance_uc, (116, 1)) # --------> gets removed
# sum_variance_code_reshaped = np.tile(sum_variance_code, (58, 1)) # -------> gets removed

# avg_scq_uc_reshaped = np.tile(avg_scq_uc, (116, 1))
# avg_scq_code_reshaped = np.tile(avg_scq_code, (58, 1)) # -------> gets removed

# max_scq_uc_reshaped = np.tile(max_scq_uc, (116, 1))
# max_scq_code_reshaped = np.tile(max_scq_code, (58, 1))

# sum_sqc_uc_reshaped = np.tile(sum_sqc_uc, (116, 1)) # --------> gets removed, 2nd round
# sum_sqc_code_reshaped = np.tile(sum_sqc_code, (58, 1)) # --------> gets removed, 2nd round

# avg_pmi_uc_reshaped = np.tile(avg_pmi_uc, (116, 1)) # --------> gets removed 
# avg_pmi_code_reshaped = np.tile(avg_pmi_code, (58, 1)) # --------> gets removed 

# max_pmi_uc_reshaped = np.tile(max_pmi_uc, (116, 1))
# max_pmi_code_reshaped = np.tile(max_pmi_code, (58, 1))

# qs_uc_reshaped = np.tile(qs_uc, (116, 1)) # --------> gets removed, 2nd round
# qs_code_reshaped = np.tile(qs_code, (58, 1)) # --------> gets removed, 2nd round

# feature_matrix = np.stack((cosine_similarity_UC, cosine_similarity_CC, LSA_similraities_UC, LSA_similraities_CC,
#                            JS_UC, JS_CC,LDA_similraities_UC,LDA_similraities_CC, avg_idf_uc_reshaped.T, avg_idf_code_reshaped, max_idf_uc_reshaped.T,
#                            max_idf_code_reshaped, dev_idf_uc_reshaped.T, dev_idf_code_reshaped, avg_ictf_uc_reshaped.T,
#                            avg_ictf_code_reshaped, max_ictf_uc_reshaped.T, max_ictf_code_reshaped, dev_ictf_uc_reshaped.T,
#                            dev_ictf_code_reshaped, avg_entropy_uc_reshaped.T, avg_entropy_code_reshaped, max_entropy_uc_reshaped.T,
#                            max_entropy_code_reshaped, med_entropy_uc_reshaped.T, med_entropy_code_reshaped, dev_entropy_uc_reshaped.T, 
#                            dev_entropy_code_reshaped, avg_variance_uc_reshaped.T, avg_variance_code_reshaped, max_variance_uc_reshaped.T,
#                            max_variance_code_reshaped, sum_variance_uc_reshaped.T, sum_variance_code_reshaped, avg_scq_uc_reshaped.T,
#                            avg_scq_code_reshaped, max_scq_uc_reshaped.T, max_scq_code_reshaped, sum_sqc_uc_reshaped.T, sum_sqc_code_reshaped,
#                            avg_pmi_uc_reshaped.T, avg_pmi_code_reshaped, max_pmi_uc_reshaped.T, max_pmi_code_reshaped, qs_uc_reshaped.T, 
#                            qs_code_reshaped), axis=2)

# print(feature_matrix.shape)
