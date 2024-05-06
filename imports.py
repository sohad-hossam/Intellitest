from treelib import Node, Tree
from tree_sitter import Language, Parser
import math
import re
import math
from nltk.corpus import stopwords
from nltk.corpus import words
import nltk
from scipy.stats import spearmanr, pearsonr #download_folder = C:/nltk_data 

from nltk.stem import PorterStemmer
from nltk.tokenize import word_tokenize
from sklearn.feature_extraction.text import TfidfVectorizer, CountVectorizer, TfidfTransformer
from sklearn.metrics.pairwise import cosine_similarity
from scipy.spatial.distance import jensenshannon

from sklearn.preprocessing import LabelEncoder,normalize,MinMaxScaler
from sklearn.decomposition import TruncatedSVD
import numpy as np
from numpy import ndarray
import random

from typing import Callable

import pandas as pd
from statistics import stdev
# from dask.distributed import Client, progress

from collections import defaultdict
from scipy.stats import rankdata

import os
# from gensim import corpora
# from gensim.models import LdaMulticore
# import gensim

from imblearn.over_sampling import SMOTE

from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestRegressor 
import time
import pickle
 
import torch
from torch.utils.data import Dataset
from torch.utils.data import DataLoader
from torch.utils.data import random_split
from torch import nn
from tqdm import tqdm
from torch.nn.utils.rnn import pad_sequence