@app.route('/get-folder-structure', methods=['GET'])
def get_folder_structure():
    try:
        directory_path = request.args.get('directory_path')
        print("directory path",directory_path)
        if not directory_path or not os.path.isdir(directory_path):
            return jsonify({'error': 'Invalid directory path.'}), 400

        folder_structure = generate_folder_structure(directory_path)
        return jsonify(folder_structure), 200

    except Exception as e:
        app.logger.error(f"An error occurred: {e}")
        return jsonify({'error': 'An unexpected error occurred.'}), 500