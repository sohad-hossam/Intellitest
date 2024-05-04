from joblib import load


class TraceLinks():
    def __init__(self, code_file: str, usecase_file: str) -> None:
        self.code_file = code_file
        self.usecase_file = usecase_file

    def featuremap(self, code_file: str, usecase_file: str) -> None:
        
        pass

    def computeTraceLinks(self) -> int:
      
        data = self.featuremap(self.code_file, self.usecase_file)
        model = load('pickles/teiid_model.pkl')
        predictions = model.predict(data)
        return predictions

