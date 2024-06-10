from PreprocessingFunctions import *
from DatasetRetrieval import *
from DLModel import *

class DLScript():

    def __init__(self, model_path: str, vocab: list, embedding, embedding_dim, UC_size, CC_size):
        self.model = DLModel(embedding, embedding_dim, UC_size, CC_size)
        self.model.load_state_dict(torch.load(model_path, map_location=torch.device('cpu')))
        self.model.eval()
        self.preprocessor_functions = PreProcessorFunctions()
        self.preprocessor_functions.vocabToIndex(vocab)
        self.preprocessor_functions.setVocab(self.preprocessor_functions.word_index)
        

        Language.build_library(
        # Store the library in the `build` directory
        "build/my-languages.so",
        # Include one or more languages
         ["GP GUI Base/electron-react-app/src/tree-sitter-java"],
         )
        JAVA = Language("build/my-languages.so", "java")

        self.parser = Parser()
        self.parser.set_language(JAVA)

    def UseCaseSourceFileScript(self, file_dir: str, summary_description: str) -> float:
        try:
            UC_processed = self.preprocessor_functions._PreProcessorFuncDeepLearning(summary_description, 'uc' , 'test')
            
            CC_file = ''
            with open(file_dir, 'r') as file:
                CC_file = file.read()
            
            feature_list = self.splitToFunctions(CC_file, UC_processed)
            self.preprocessor_functions.setUpUnknown(feature_list, 'test')
            self.preprocessor_functions.dataSetToIndex(feature_list)

            feature_obj = TracibilityLinkDataset(feature_list, [0]*len(feature_list))
            positive_labels = self.predict(feature_obj)
            positive_percentage = (positive_labels/len(feature_list)) * 100
            return positive_percentage
          
        except:
            return -1

        # with torch.no_grad():  # Disable gradient calculation
        #     output = self.model(torch.tensor(feature_list))
        #     print(output)
        


    def splitToFunctions(self, CC_file: str, UC_processed:list) -> list:
        
        feature_list = list()
        src_new = bytes(CC_file,"utf-8")
        tree_new = self.parser.parse(src_new)
        curr_node_new = tree_new.root_node
        queue_new=list()
        queue_new.append(curr_node_new)

        while(len(queue_new)):
            curr_node = queue_new.pop(0)
            counter=0
            for child in curr_node.children:
                queue_new.append(child)
                if(child.parent.type == "method_declaration" and child.type == "block"):
                    segment = CC_file[child.start_byte:child.end_byte]
                    method_name=CC_file[child.parent.children[2].start_byte:child.parent.children[2].end_byte]
                    function = self.preprocessor_functions._PreProcessorFuncDeepLearning(method_name+segment, 'cc' , 'test')
                    feature_list.append([function, UC_processed])
                print(counter)
                counter+=1

        return feature_list 
    
    def customCollate(self, batch: list):
        # batch --> tuple (x,y)
        # x --> list of 4 lists
        CC_size = 2000
        UC_size = 4000
        x_batch, y_batch = zip(*batch)
        
        CC,UC = zip(*x_batch)
        
        CC_padded = []
        UC_padded = []
        
        for i  in range(len(x_batch)):
            
            CC_padded.append( list(CC[i]) + [0] * (CC_size - len(CC[i])) )
            UC_padded.append( list(UC[i]) + [0] * (UC_size - len(UC[i])) )

        return  CC_padded, UC_padded, y_batch

    def predict(self, test_dataset, batch_size=2):
        
        positive_labels = 0
        test_dataloader = torch.utils.data.DataLoader(test_dataset, batch_size=batch_size, collate_fn=self.customCollate , shuffle=False)
        use_cuda = torch.cuda.is_available()    
        device = torch.device("cuda" if use_cuda else "cpu") 
        
        if use_cuda:
                self.model = self.model.cuda()

        with torch.no_grad():
            for  CC,UC, y_batch in tqdm(test_dataloader):
                
                UC_padded_tensor = torch.tensor(UC, device=device)
                CC_padded_tensor = torch.tensor(CC, device=device)

                output = self.model.forward(CC_padded_tensor, UC_padded_tensor)
                _,predicted=torch.max(output,dim=1)
              
                positive_labels += int(torch.sum(predicted == 1))
               
                # predictions.extend(predicted.tolist())
        return positive_labels
    
    def splitToFunctionsSecondEdition(self, CC_dir: str) -> list:
        try:
            CC_file = ''
            with open(CC_dir, 'r') as file:
                CC_file = file.read()

            functions_list = list()
            src_new = bytes(CC_file, "utf-8")
            tree_new = self.parser.parse(src_new)
            curr_node_new = tree_new.root_node
            queue_new = list()
            queue_new.append(curr_node_new)

            while (len(queue_new)):
                curr_node = queue_new.pop(0)
                for child in curr_node.children:
                    queue_new.append(child)
                    if (child.parent.type == "method_declaration" and child.type == "block"):
                        segment = CC_file[child.start_byte:child.end_byte]
                        method_name = CC_file[child.parent.children[2].start_byte:child.parent.children[2].end_byte]
                        function = self.preprocessor_functions._PreProcessorFuncDeepLearning(method_name + segment, 'cc',
                                                                                            'test')
                        functions_list.append(function)

            return functions_list
        except Exception as e:
            print("Error:", e)
            return []

    def UseCaseSourceFileScriptForTopFive(self, functions_list: list, UC_processed: list) -> float:
        try:
            feature_list = list()
            for function in functions_list:
                feature_list.append([function, UC_processed])

            self.preprocessor_functions.setUpUnknown(feature_list, 'test')
            self.preprocessor_functions.dataSetToIndex(feature_list)

            feature_obj = TracibilityLinkDataset(feature_list, [0]*len(feature_list))
            positive_labels = self.predict(feature_obj)
            positive_percentage = (positive_labels/len(feature_list)) * 100
            return positive_percentage
        except:
            return -1
        
    def TopFiveSourceFilesScript(self, directories_dict: dict, name_to_functions: dict, summary_description: str) -> dict:
        try:
            print(summary_description)
            # index_to_file = dict()
            results_dict = dict()
            file_to_percentages = dict()
            UC_processed = self.preprocessor_functions._PreProcessorFuncDeepLearning(summary_description, 'uc' , 'test')
            counter=0
            for index, file_info in enumerate(directories_dict.items()):
                if(len(results_dict)==5):
                    break
                file_name, file_dir = file_info
                # index_to_file[index] = file_name
                positive_percentage = self.UseCaseSourceFileScriptForTopFive(name_to_functions[file_name], UC_processed)
                if(positive_percentage>80 and positive_percentage<100):
                    results_dict[file_name] = positive_percentage
                print (counter)
                counter+=1
            
            return results_dict
        
        except:
            return -1
                

    def predict2(self, test_dataset, batch_size=5):
        
        positive_labels = 0
        predictions=[]
        test_dataloader = torch.utils.data.DataLoader(test_dataset, batch_size=batch_size, collate_fn=self.customCollate , shuffle=False)
        use_cuda = torch.cuda.is_available()    
        device = torch.device("cuda" if use_cuda else "cpu") 
        
        if use_cuda:
                self.model = self.model.cuda()

        with torch.no_grad():
            for  CC,UC, y_batch in tqdm(test_dataloader):
                
                UC_padded_tensor = torch.tensor(UC, device=device)
                CC_padded_tensor = torch.tensor(CC, device=device)

                output = self.model.forward(CC_padded_tensor, UC_padded_tensor)
               
                _,predicted=torch.max(output,dim=1)
                print(predicted)
                positive_labels += int(torch.sum(predicted == 1))
                predictions.extend(predicted.tolist())
     
        return positive_labels, predictions
    

        
    def BugLocalization(self,javaFileName:str, name_to_func_dict:dict,summary_description:str):
        try:
            feature_list = list()
            UC_processed = self.preprocessor_functions._PreProcessorFuncDeepLearning(summary_description, 'uc' , 'test')
            functions_list=name_to_func_dict[javaFileName]
            for function in functions_list:
                feature_list.append([function, UC_processed])

            self.preprocessor_functions.setUpUnknown(feature_list, 'test')
            self.preprocessor_functions.dataSetToIndex(feature_list)

            feature_obj = TracibilityLinkDataset(feature_list, [0]*len(feature_list))
            positive_labels,predictions = self.predict2(feature_obj)
            print("predectionsss",predictions)
            indecies=[]
            for i,prediction in enumerate(predictions):
                if prediction==1:
                    indecies.append(i)
            return indecies
        except Exception as e:
            print("Error:", e)
            return []