from flask import Flask, request, jsonify, send_file, Response
from flask_cors import CORS
from MaintainabilityScore import MaintainabilityScore
from MLScript import TraceLinks
import os
import zipfile
import pandas as pd
import sqlite3
from sklearn.model_selection import train_test_split
from concurrent.futures import ThreadPoolExecutor
import requests 
from bs4 import BeautifulSoup
import json

app = Flask(__name__)
CORS(app)

@app.route('/compute-score', methods=['POST'])
def compute_score():
    try:
        if 'file' not in request.files:
            return jsonify({'error': 'No file part in the request.'}), 400

        uploaded_file = request.files['file']

        if uploaded_file.filename == '':
            return jsonify({'error': 'No file selected for uploading.'}), 400

        file_path = os.path.join('GP GUI Base/electron-react-app/src/uploads', uploaded_file.filename)
        uploaded_file.save(file_path)
        score = MaintainabilityScore(file_path)

        maintainability_score = score.computeMaintainabilityScore()
        halstead_volume_results = score.computeHalsteadVolume()
        sloc_and_comment_lines_results = score.computeSLOCAndCommentLines()
        cyclomatic_complexity = score.computeCyclomaticComplexity()

        os.remove(file_path)
        file_name = uploaded_file.filename

        return jsonify({
                file_name: {
                    'maintainability_score': maintainability_score,
                    'halstead_volume_results': {
                        'n': halstead_volume_results[0],
                        'N': halstead_volume_results[1],
                        'N_hat': halstead_volume_results[2],
                        'V': halstead_volume_results[3],
                        'D': halstead_volume_results[4],
                        'E': halstead_volume_results[5],
                        'T': halstead_volume_results[6],
                        'B': halstead_volume_results[7]
                    },
                    'sloc_and_comment_lines_results': {
                        'SLOC': sloc_and_comment_lines_results[0],
                        'comment_lines_ratio': sloc_and_comment_lines_results[1]
                    },
                    'cyclomatic_complexity': cyclomatic_complexity
                }
            }), 200
    except Exception as e:
        app.logger.error(f"An error occurred: {e}")
        return jsonify({'error': 'An unexpected error occurred.'}), 500


UPLOAD_FOLDER = 'GP GUI Base/electron-react-app/src/uploads'
executor = ThreadPoolExecutor()

def extract_zip(zip_file, destination):
    with zipfile.ZipFile(zip_file, 'r') as zip_ref:
        zip_ref.extractall(destination)

def find_sqlite_file(directory):
    for root, _, files in os.walk(directory):
        for file in files:
            if file.endswith(".sqlite3"):
                return os.path.join(root, file)
    return None

def process_sqlite_file(sqlite_file_path):
    con = sqlite3.connect(sqlite_file_path)
    cur = con.cursor()

    issue_df = pd.read_sql_query("SELECT issue_id, summary, description FROM issue", con)
    change_set_link_df = pd.read_sql_query("SELECT issue_id, commit_hash FROM change_set_link", con)
    change_set_df = pd.read_sql_query("SELECT commit_hash, file_path FROM code_change", con)

    merged_df = pd.merge(change_set_link_df, change_set_df, on='commit_hash')
    merged_df = merged_df.drop(columns=['commit_hash'])

    merged_df.to_csv(os.path.join(UPLOAD_FOLDER, 'teiid.csv'), index=False)

    train, test = train_test_split(merged_df, test_size=0.01)

    unique_cc = test['file_path'].unique()
    unique_uc = test['issue_id'].unique()

    train = train[~train['issue_id'].isin(unique_uc)]
    train = train[~train['file_path'].isin(unique_cc)]

    train_unique_cc = train['file_path'].unique()
    train_unique_uc = train['issue_id'].unique()

    os.makedirs(os.path.join(UPLOAD_FOLDER, 'usecase_files'), exist_ok=True)  # Create usecase_files folder

    for i, row in issue_df.iterrows():
        issue_id = row['issue_id']
        if issue_id not in merged_df['issue_id'].values:
            continue
        summary = row['summary']
        description = row['description']
        if row['issue_id'] in train_unique_uc:
            with open(os.path.join(UPLOAD_FOLDER, 'usecase_files', f'{issue_id}.txt'), 'w', encoding='utf-8') as f:
                f.write(f"{summary}\n{description}")


def extract_java_files(directory):
    java_files = {}
    for root, _, files in os.walk(directory):
        for file in files:
            if file.endswith(".java"):
                java_files[file] = os.path.join(root, file)
    return java_files



def process_zip_file(zip_file_path):
    try:
        extract_zip(zip_file_path, UPLOAD_FOLDER)
        sqlite_file_path = find_sqlite_file(UPLOAD_FOLDER)
        if sqlite_file_path:
            process_sqlite_file(sqlite_file_path)
        
        java_files = extract_java_files(UPLOAD_FOLDER)
        with open(os.path.join(UPLOAD_FOLDER, 'java_files_dict.json'), 'w') as f:
            json.dump(java_files, f)

        os.remove(zip_file_path)
        app.logger.info(f"Zip folder processed successfully: {zip_file_path}")
    except Exception as e:
        app.logger.error(f"Error processing zip folder: {e}")


@app.route('/upload-folder', methods=['POST'])
def upload_folder():
    try:
        if 'file' not in request.files:
            return jsonify({'error': 'No file part in the request.'}), 400

        uploaded_file = request.files['file']

        if uploaded_file.filename == '':
            return jsonify({'error': 'No file selected for uploading.'}), 400

        temp_file_path = os.path.join(UPLOAD_FOLDER, 'temp.zip')
        uploaded_file.save(temp_file_path)
        executor.submit(process_zip_file, temp_file_path)

        return jsonify({'message': 'Folder upload started successfully.'}), 200

    except Exception as e:
        app.logger.error(f"An error occurred: {e}")
        return jsonify({'error': 'An unexpected error occurred.'}), 500
    
@app.route('/get-folder-structure', methods=['GET'])
def get_folder_structure():
    try:
        directory_path = request.args.get('directory_path')
        if not directory_path or not os.path.isdir(directory_path):
            return jsonify({'error': 'Invalid directory path.'}), 400

        folder_structure = generate_folder_structure(directory_path)
        return jsonify(folder_structure), 200

    except Exception as e:
        app.logger.error(f"An error occurred: {e}")
        return jsonify({'error': 'An unexpected error occurred.'}), 500

def generate_folder_structure(directory):
    folder_structure = {'name': os.path.basename(directory), 'type': 'folder', 'children': []}
    for item in os.listdir(directory):
        item_path = os.path.join(directory, item)
        if os.path.isdir(item_path):
            folder_structure['children'].append(generate_folder_structure(item_path))
        else:
            folder_structure['children'].append({'name': item, 'type': 'file'})
    return folder_structure

@app.route('/get-file-content', methods=['GET'])
def get_file_content():
    try:
        file_path = request.args.get('file_path')
        if not file_path or not os.path.isfile(file_path):
            return jsonify({'error': 'Invalid file path or file does not exist.'}), 400

        with open(file_path, 'r') as file:
            content = file.read()

        return Response(content, mimetype='text/plain'), 200

    except Exception as e:
        app.logger.error(f"An error occurred: {e}")
        return jsonify({'error': 'An unexpected error occurred.'}), 500
    
@app.route('/get-usecase-files', methods=['GET'])
def get_usecase_files():
    try:
        folder_path = request.args.get('folder_path')
        if not folder_path or not os.path.isdir(folder_path):
            return jsonify({'error': 'Invalid folder path or folder does not exist.'}), 400

        # List all files in the directory
        files = os.listdir(folder_path)

        return jsonify({'files': files}), 200

    except Exception as e:
        app.logger.error(f"An error occurred: {e}")
        return jsonify({'error': 'An unexpected error occurred.'}), 500
    


@app.route('/compute-tracelinks', methods=['POST'])
def computetracelinks():
    try:
        print("computing trace links")
        usecase_file = request.args.get('usecase')
        code_file = request.args.get('code')
        print("usecasefile",usecase_file)
        print("codefile",code_file)
        if not usecase_file or not code_file:
            return jsonify({'error': 'Use case file and code file are required.'}), 400

        score = TraceLinks(code_file, usecase_file)
        trace_links = score.computeTraceLinks()
        print("tracelinks val at end point",trace_links)
        return jsonify({'trace_links': trace_links}), 200

    except Exception as e:
        app.logger.error(f"An error occurred: {e}")
        return jsonify({'error': 'An unexpected error occurred.'}), 500
    
@app.route('/fetch-details', methods=['POST'])
def fetch_details():
    try:
        data = request.json
        if 'url' not in data:
            return jsonify({'error': 'No URL provided.'}), 400

        url = data['url']
        page = requests.get(url)
        if page.status_code == 200:
            soup = BeautifulSoup(page.content, 'html.parser')
            body_jira = soup.find('body', id='jira')
            if body_jira:
                page_div = body_jira.find('div', id='page')
                if page_div:
                    main_content = page_div.find('main', id='main')
                    if main_content:
                        header = main_content.find('header', id='stalker')
                        if header:
                            summary_value = header.find('h2', id='summary-val')
                        else:
                            return jsonify({'error': 'Header not found inside main content.'}), 404
                        
                        issue_body_content = main_content.find('div', class_='issue-body-content')
                        if issue_body_content:
                            user_content_block = issue_body_content.find('div', class_='user-content-block')
                            if user_content_block:
                                p = user_content_block.find('p')
                            else:
                                return jsonify({'error': 'User content block not found inside issue body content.'}), 404
                            
                            issuedetails = issue_body_content.find('ul', id='issuedetails')
                            if issuedetails:
                                type_val = issuedetails.find('span', id='type-val')
                                if type_val:
                                    priority_val = issuedetails.find('span', id='priority-val')
                                    if priority_val:
                                        resolution_val = issuedetails.find('span', id='resolution-val')
                                        if resolution_val:
                                            fixfor_val = issuedetails.find('span', id='fixfor-val')
                                            fixfor_val = fixfor_val.find('a')
                                            if fixfor_val:
                                                versions_val = issuedetails.find('span', id='versions-val')
                                                if versions_val:
                                                    sprint_val = issue_body_content.find('div', id='customfield_12310940-val')
                                                    if sprint_val:
                                                    
                                                        return jsonify({'summary': summary_value.text, 'description': p.text,
                                                                        'details': {
                                                                            'type': type_val.text.strip(),
                                                                            'priority': priority_val.text.strip(),
                                                                            'resolution': resolution_val.text.strip(),
                                                                            'fixfor': fixfor_val.text,
                                                                            'versions': versions_val.text.strip(),
                                                                            'sprint': sprint_val.text.strip()
                                                                        }}), 200
                                                    else:
                                                        return jsonify({'error': 'Sprint value not found inside issue details.'}), 404
                                                else:
                                                    return jsonify({'error': 'Versions value not found inside issue details.'}), 404
                                            else:
                                                return jsonify({'error': 'Fixfor value not found inside issue details.'}), 404
                                        else:
                                            return jsonify({'error': 'Resolution value not found inside issue details.'}), 404
                                    else:
                                        return jsonify({'error': 'Priority value not found inside issue details.'}), 404
                                else:
                                    return jsonify({'error': 'Type value not found inside issue details.'}), 404
                            else:
                                return jsonify({'error': 'Issue details not found inside issue body content.'}), 404
                        else:
                            return jsonify({'error': 'Issue body content not found inside main content.'}), 404
                    else:
                        return jsonify({'error': 'Main content not found inside page.'}), 404
                else:
                    return jsonify({'error': 'Page div not found inside body with id "jira".'}), 404
            else:
                return jsonify({'error': 'Body with id "jira" not found.'}), 404
        else:
            return jsonify({'error': 'Failed to fetch URL.'}), 500

    except Exception as e:
        app.logger.error(f"An error occurred: {e}")
        return jsonify({'error': 'An unexpected error occurred.'}), 500

@app.route('/get-java-files', methods=['GET'])
def get_java_files():
    try:
        java_files_dict_path = os.path.join(UPLOAD_FOLDER, 'java_files_dict.json')
        if not os.path.exists(java_files_dict_path):
            return jsonify({'error': 'Java files dictionary not found.'}), 404

        with open(java_files_dict_path, 'r') as f:
            java_files_dict = json.load(f)

        java_files = list(java_files_dict.keys())

        return jsonify({'java_files': java_files}), 200

    except Exception as e:
        app.logger.error(f"An error occurred: {e}")
        return jsonify({'error': 'An unexpected error occurred.'}), 500
if __name__ == '__main__':
    app.run(debug=True)


