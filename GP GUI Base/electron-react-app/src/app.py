from flask import Flask, request, jsonify,send_file,Response
from flask_cors import CORS
from MaintainabilityScore import MaintainabilityScore
from MLScript import TraceLinks
import os
import zipfile
from concurrent.futures import ThreadPoolExecutor
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

def process_zip_file(zip_file_path):
    try:
        extract_zip(zip_file_path, UPLOAD_FOLDER)
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

        # Save the uploaded zip file temporarily
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
        usecase_file = request.form.get('usecase_file')
        code_file = request.form.get('code_file')

        if not usecase_file or not code_file:
            return jsonify({'error': 'Use case file and code file are required.'}), 400

        score = TraceLinks(code_file, usecase_file)
        trace_links = score.computeTraceLinks()

        return jsonify({'trace_links': trace_links}), 200

    except Exception as e:
        app.logger.error(f"An error occurred: {e}")
        return jsonify({'error': 'An unexpected error occurred.'}), 500

if __name__ == '__main__':
    app.run(debug=True)
