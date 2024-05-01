from flask import Flask, request, jsonify
from flask_cors import CORS
from MaintainabilityScore import MaintainabilityScore
import os
import re
import math
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

if __name__ == '__main__':
    app.run(debug=True)
