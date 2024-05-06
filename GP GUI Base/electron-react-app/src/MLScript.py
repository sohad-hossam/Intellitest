from joblib import load


class TraceLinks():
    def __init__(self, code_file: str, usecase_file: str) -> None:
        self.code_file = code_file.lower()
        self.usecase_file = usecase_file.lower()
        ############### Currently the test files only ##################
        self.CC_to_index = load('pickles/CCindex_test.pkl')
        self.UC_to_index = load('pickles/UCindex_test.pkl')
        self.feature_map = load('GP GUI Base/electron-react-app/src/pickles/features_links_selected_test.pkl')
       
    def featureMap(self, code_file: str, usecase_file: str) -> None:  
        CC_index = -1
        if self.CC_to_index.get("./dataset/teiid_dataset/test_cc/" + self.code_file) != None:
            CC_index = self.CC_to_index["./dataset/teiid_dataset/test_cc/" + code_file]
        else:
            CC_index = self.CC_to_index["./dataset/teiid_dataset/train_cc/" + code_file]

        UC_index = self.UC_to_index[usecase_file]
        print("at feature mapping")
        return self.feature_map[CC_index][UC_index]
        

    def computeTraceLinks(self) -> int:
      
        data = self.featureMap(self.code_file, self.usecase_file)
        model = load('pickles/teiid_model.pkl')
        predictions = model.predict(data.reshape(1, -1))[0]
        print("computing predection",predictions)
        return predictions

