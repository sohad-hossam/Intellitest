@app.route('/get-top-five', methods=['GET'])
def get_top_five():
  
    try:
        embedding_matrix = np.load('GP GUI Base/electron-react-app/src/Script pickles/embedding_matrix.pkl', allow_pickle=True)
        word2vec_vocab = np.load('GP GUI Base/electron-react-app/src/Script pickles/word2vec_vocab.pkl', allow_pickle=True)
        dl_obj = DLScript('GP GUI Base/electron-react-app/src/Script pickles/LSTM_3projects_3Linearlayers_10epochs.pth', word2vec_vocab, embedding_matrix, embedding_matrix.shape, 4000, 2000)

        java_files_dict_path = os.path.join(UPLOAD_FOLDER, 'java_files_dict.json')
        if not os.path.exists(java_files_dict_path):
            return jsonify({'error': 'Java files dictionary not found.'}), 404
        with open(java_files_dict_path, 'r') as f:
            directories_dict = json.load(f)

        function_names_dict_path = os.path.join(UPLOAD_FOLDER, 'name_to_functions_dict.json')
        if not os.path.exists(function_names_dict_path):
            return jsonify({'error': 'Function names dictionary not found.'}), 404
        with open(function_names_dict_path, 'r') as f:
            name_to_functions = json.load(f)


        data = request.json
        summary_description = data.get('summary_description')
        top_five_dict= dl_obj.TopFiveSourceFilesScript(directories_dict, name_to_functions, summary_description)  
        if isinstance(top_five_dict, set):
            top_five_dict = list(top_five_dict)
        return jsonify({'top_five_dict': top_five_dict}), 200
    except Exception as e:
        app.logger.error(f"An error occurred: {e}")
        return jsonify({'error': 'An unexpected error occurred.'}), 500