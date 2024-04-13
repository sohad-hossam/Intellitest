
# issue : issue_id, summary, description
# change_set_link(issue_id): commit hash
# change_set(commit_hash): filename

import pandas as pd
import sqlite3

con = sqlite3.connect("Dataset/railo_dataset/railo.sqlite3")
cur = con.cursor()

issue_df = pd.read_sql_query("SELECT issue_id, summary, description FROM issue", con)
change_set_link_df = pd.read_sql_query("SELECT issue_id, commit_hash FROM change_set_link", con)
change_set_df = pd.read_sql_query("SELECT commit_hash,file_path FROM code_change", con)

merged_df = pd.merge(change_set_link_df, change_set_df, on='commit_hash')
merged_df = merged_df.drop(columns=['commit_hash'])

merged_df.to_csv('Dataset/railo_dataset/railo.csv', index=False)

for i, row in issue_df.iterrows():
    issue_id = row['issue_id']
    summary = row['summary']
    description = row['description']
    with open(f'Dataset/railo_dataset/UC/{issue_id}.txt', 'w') as f:
        f.write(f"{summary}\n{description}")

con.close()