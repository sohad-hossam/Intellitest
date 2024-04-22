
# issue : issue_id, summary, description
# change_set_link(issue_id): commit hash
# change_set(commit_hash): filename

from imports import *
from FeatureExtraction import *
from PreProcessor import *
import pandas as pd
import sqlite3
import shutil
import os

con = sqlite3.connect("Dataset/teiid_dataset/teiid.sqlite3")
cur = con.cursor()

issue_df = pd.read_sql_query("SELECT issue_id, summary, description FROM issue", con)
change_set_link_df = pd.read_sql_query("SELECT issue_id, commit_hash FROM change_set_link", con)
change_set_df = pd.read_sql_query("SELECT commit_hash,file_path FROM code_change", con)

merged_df = pd.merge(change_set_link_df, change_set_df, on='commit_hash')
merged_df = merged_df.drop(columns=['commit_hash'])


merged_df.to_csv('Dataset/teiid_dataset/teiid.csv', index=False)

train,test = train_test_split(merged_df, test_size = 0.01)


unique_cc = test['file_path'].unique()
unique_uc = test['issue_id'].unique()

train = train[~train['issue_id'].isin(unique_uc)]
train = train[~train['file_path'].isin(unique_cc)]

train_unique_cc = train['file_path'].unique()
train_unique_uc = train['issue_id'].unique()


for i, row in issue_df.iterrows():
    issue_id = row['issue_id']
    if issue_id not in merged_df['issue_id'].values:
        continue
    summary = row['summary']
    description = row['description']
    if row['issue_id'] in train_unique_uc:
        with open(f'Dataset/teiid_dataset/train_UC/{issue_id}.txt', 'w',encoding='utf-8') as f:
            f.write(f"{summary}\n{description}")
    elif row['issue_id'] in unique_uc:
        with open(f'Dataset/teiid_dataset/test_UC/{issue_id}.txt', 'w',encoding='utf-8') as f:
            f.write(f"{summary}\n{description}")
        

_test = set()
for i, row in merged_df.iterrows():
    file_path = row['file_path']
    if row['file_path'] in train_unique_cc:
        train.loc[train['file_path'] == row['file_path'], 'file_path'] = f'./dataset/teiid_dataset/train_cc/{i}.java'
    elif row['file_path'] in unique_cc:
        test.loc[test['file_path'] == row['file_path'], 'file_path'] = f'./dataset/teiid_dataset/test_cc/{i}.java'
    if file_path in _test:
        continue
    _test.add(file_path)
    if os.path.exists('./Dataset/teiid_dataset/teiid/'+file_path) and os.path.isfile('./Dataset/teiid_dataset/teiid/'+file_path):
        if ('./Dataset/teiid_dataset/teiid/'+file_path).endswith('.java'):
            if row['file_path'] in train_unique_cc:
                shutil.copy('./Dataset/teiid_dataset/teiid/'+file_path,f'./Dataset/teiid_dataset/train_CC/{i}.java')
            elif row['file_path'] in unique_cc:
                shutil.copy('./Dataset/teiid_dataset/teiid/'+file_path,f'./Dataset/teiid_dataset/test_CC/{i}.java')

train.to_csv('Dataset/teiid_dataset/train.csv',index=False)
test.to_csv('Dataset/teiid_dataset/test.csv',index=False)


# print(train)
# print(test)

con.close()


# df = pd.read_csv('Dataset/teiid_dataset/teiid.csv')

# train,test = train_test_split(df, test_size = 0.01)


# unique_cc = test['file_path'].unique()
# unique_uc = test['issue_id'].unique()
# print(len(unique_cc),len(unique_uc))

# train = train[~train['issue_id'].isin(unique_uc)]
# train = train[~train['file_path'].isin(unique_cc)]

# train_unique_cc = train['file_path'].unique()
# train_unique_uc = train['issue_id'].unique()

# print(len(train_unique_cc),len(train_unique_uc))

# train.to_csv('Dataset/teiid_dataset/train.csv',index=False)
# test.to_csv('Dataset/teiid_dataset/test.csv',index=False)
# print(train.shape)
# print(test.shape)
