from treelib import Node, Tree
from tree_sitter import Language, Parser
import math
import re
import math
from nltk.corpus import stopwords
import nltk
#nltk.download() #download_folder = C:/nltk_data 
from nltk.stem import PorterStemmer
from nltk.tokenize import word_tokenize
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity