from joblib import load


class TraceLinks():
    def __init__(self, code_file: str, usecase_file: str) -> None:
        self.code_file = code_file
        self.usecase_file = usecase_file
        ############### Currently the test files only ##################
        self.CC_to_index = load('pickles/CCindex_test.pkl')
        self.UC_to_index = load('pickles/UCindex_testpkl')
        self.feature_map = load('/pickles/features_links_selected_test.pkl')

    def featureMap(self, code_file: str, usecase_file: str) -> None:  
        CC_index = self.CC_to_index[code_file]
        UC_index = self.CC_to_index[usecase_file]
        
        return self.feature_map[CC_index][UC_index]
        

    def computeTraceLinks(self) -> int:
      
        data = self.featureMap(self.code_file, self.usecase_file)
        model = load('pickles/teiid_model.pkl')
        predictions = model.predict(data)[0]
        return predictions

