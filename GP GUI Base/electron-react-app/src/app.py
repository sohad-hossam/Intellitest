from flask import Flask, request, jsonify

from flask_cors import CORS

from MaintainabilityScore import MaintainabilityScore
import os
app = Flask(__name__)
CORS(app)  
@app.route('/compute-score', methods=['POST'])
def compute_score():
   
    file_path = request.json.get('file_path')

    if not file_path:
        return jsonify({'error': 'File path is required.'}), 400
    if not os.path.isfile(file_path):
        return jsonify({'error': 'File not found.'}), 404

    try:

        score = MaintainabilityScore(file_path)

        maintainability_score = score.computeMaintainabilityScore()

        return jsonify({'maintainability_score': maintainability_score}), 200

    except Exception as e:
        app.logger.error(f"An error occurred: {e}")
        return jsonify({'error': 'An unexpected error occurred.'}), 500

if __name__ == '__main__':
    app.run(debug=True)
