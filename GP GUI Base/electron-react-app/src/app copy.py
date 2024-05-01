from flask import Flask, request, jsonify

from MaintainabilityScore import MaintainabilityScore
import os

app = Flask(__name__)

@app.route('/compute-score', methods=['POST'])
def compute_score():
    # Get file path from request data
    file_path = request.json.get('file_path')

    # Validate file path
    if not file_path:
        return jsonify({'error': 'File path is required.'}), 400

    # Check if file exists
    if not os.path.isfile(file_path):
        return jsonify({'error': 'File not found.'}), 404

    try:
        # Create instance of MaintainabilityScore class
        score = MaintainabilityScore(file_path)

        # Compute maintainability score
        maintainability_score = score.computeMaintainabilityScore()

        return jsonify({'maintainability_score': maintainability_score}), 200

    except Exception as e:
        # Log the error for debugging purposes
        app.logger.error(f"An error occurred: {e}")
        return jsonify({'error': 'An unexpected error occurred.'}), 500

if __name__ == '__main__':
    app.run(debug=True)
