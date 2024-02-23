from PreProcessor import *
import os
from collections import Counter

#Unigram Language Model using Jelinek Mercer Smoothing

# Calculate the term frequency in the document
# Calculate the term frequency in the collection

uc_document_length = []
uc_term_frequency_in_document = []
uc_tokens_list = []
uc_term_document_frequency_in_corpus = Counter()
_lambda = 0.25
mu = 0.5

for filename in os.listdir("./UC"):
    filepath = os.path.join("./UC", filename)
    uc_tokens = UCPreProcessor(filepath).split()
    uc_tokens_list.append(set(uc_tokens))
    uc_document_length.append(len(uc_tokens))
    frequencies = Counter(uc_tokens)
    
    uc_term_frequency_in_document.append(frequencies)
    uc_term_document_frequency_in_corpus += frequencies

uc_total_terms_in_corpus = sum(uc_document_length)

code_document_length = []
code_term_frequency_in_document = []
code_tokens_list = []
code_term_document_frequency_in_corpus = Counter()

JM_score = np.zeros((116,58))
DP_score = np.zeros((116,58))
code_file_count = 0

for filename in os.listdir("./CC"):
    
    filepath = os.path.join("./CC", filename)
    code_tokens = CodePreProcessor(filepath).split()
    code_document_length.append(len(code_tokens))
    frequencies = Counter(code_tokens)
    
    code_term_frequency_in_document.append(frequencies)
    code_term_document_frequency_in_corpus += frequencies
    
    code_tokens = set(code_tokens)

    
    for i,doc in enumerate(uc_term_frequency_in_document):
        for j,term in enumerate(code_tokens):
            term_count_in_document = 0 if (uc_term_frequency_in_document[i] or {}).get(term) is None else (uc_term_frequency_in_document[i] or {}).get(term)
            corpus_frequency = 0 if uc_term_document_frequency_in_corpus.get(term) is None else uc_term_document_frequency_in_corpus.get(term) / uc_total_terms_in_corpus
            JM_score[code_file_count][i] += ((1 - _lambda) * (term_count_in_document)) + (_lambda * uc_document_length[i] * corpus_frequency)

            # Bayesian smoothing using Dirichlet priors. 

            # pµ(w|d) = c(w;d)+µ*p(w|C) / sum(c(w;d))+µ
            # the document term frequency c(w;d)
            DP_score[code_file_count][i] += (term_count_in_document + mu * corpus_frequency) / (uc_document_length[i] + mu)
    code_file_count += 1

code_total_terms_in_corpus = sum(code_document_length)

JM_uc = np.zeros((58,116))
DP_uc = np.zeros((58,116))

code_file_count = 0

for uc in uc_term_frequency_in_document:
    for i,doc in enumerate(code_term_frequency_in_document):
        for j,term in enumerate(uc):
            term_count_in_document = 0 if (code_term_frequency_in_document[i] or {}).get(term) is None else (code_term_frequency_in_document[i] or {}).get(term)
            corpus_frequency = 0 if code_term_document_frequency_in_corpus.get(term) is None else code_term_document_frequency_in_corpus.get(term) / code_total_terms_in_corpus
            JM_uc[code_file_count][i] += ((1 - _lambda) * (term_count_in_document)) + (_lambda * code_document_length[i] * corpus_frequency)
            DP_uc[code_file_count][i] += (term_count_in_document + mu * corpus_frequency) / (code_document_length[i] + mu)
    code_file_count += 1

# transpose the matrix and append

JM_uc = JM_uc.transpose()
DP_uc = DP_uc.transpose()
JM_score = np.append(JM_score, JM_uc, axis=0)
DP_score = np.append(DP_score, DP_uc, axis=0)

print(JM_score.shape)
print(DP_score.shape)