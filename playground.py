
# issue : issue_id, summary, description
# change_set_link(issue_id): commit hash
# change_set(commit_hash): filename

from imports import *
from FeatureExtraction import *
from PreProcessor import *

# _PreProcessor = PreProcessor()
# UC_documents, UCTokens= _PreProcessor.setupUC("./Dataset/railo_dataset/UC")
# code_documents, CodeTokens = _PreProcessor.setupCC("./Dataset/railo_dataset/railo/") 
# UCTokens.update(CodeTokens)


import pandas as pd
import sqlite3
import shutil
import os

con = sqlite3.connect("Dataset/railo_dataset/railo.sqlite3")
cur = con.cursor()

issue_df = pd.read_sql_query("SELECT issue_id, summary, description FROM issue", con)
change_set_link_df = pd.read_sql_query("SELECT issue_id, commit_hash FROM change_set_link", con)
change_set_df = pd.read_sql_query("SELECT commit_hash,file_path FROM code_change", con)

merged_df = pd.merge(change_set_link_df, change_set_df, on='commit_hash')
merged_df = merged_df.drop(columns=['commit_hash'])


merged_df.to_csv('Dataset/railo_dataset/railo.csv', index=False)

# for i, row in issue_df.iterrows():
#     issue_id = row['issue_id']
#     if issue_id not in merged_df['issue_id'].values:
#         continue
#     summary = row['summary']
#     description = row['description']
#     with open(f'Dataset/railo_dataset/UC/{issue_id}.txt', 'w') as f:
#         f.write(f"{summary}\n{description}")

test = set()
for i, row in merged_df.iterrows():
    file_path = row['file_path']
    if file_path in test:
        continue
    test.add(file_path)
    if os.path.exists('./Dataset/railo_dataset/railo/'+file_path) and os.path.isfile('./Dataset/railo_dataset/railo/'+file_path):
        if ('./Dataset/railo_dataset/railo/'+file_path).endswith('.java'):
            shutil.copy('./Dataset/railo_dataset/railo/'+file_path,f'./Dataset/railo_dataset/CC/{i}.java')
            
con.close()