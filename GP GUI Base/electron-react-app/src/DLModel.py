from PreProcessor import *
from PreprocessingFunctions import *
from DatasetRetrieval import *

class TracibilityLinkDataset(Dataset):

    def __init__(self, x_train, y_train):
        
        self.tensor_x_train = x_train
        self.tensor_y_train = torch.tensor(y_train)


    # len and get item are very important --> used by dataloader
    def __len__(self):
        return len(self.tensor_y_train)

    def __getitem__(self, idx):
        return self.tensor_x_train[idx], self.tensor_y_train[idx]


class DLModel(nn.Module):

    def __init__(self,embedding_matrix : np.array, embedding_dim : tuple,UC_size: int, CC_size:int, hidden_size:int = 128, classes: int = 2):
        super(DLModel, self).__init__()


        self.embedding = nn.Embedding(num_embeddings = embedding_dim[0], embedding_dim = embedding_dim[1], _weight = torch.tensor(embedding_matrix))
        
        self.lstm_CC = nn.LSTM(embedding_dim[1],hidden_size,batch_first=True, bidirectional=False)
        
        self.linear_post_flatten = nn.Linear(CC_size*hidden_size+UC_size*hidden_size,hidden_size*3)
        self.linear_post_flatten2 = nn.Linear(hidden_size*3,hidden_size*2)
        self.linear_post_flatten3 = nn.Linear(hidden_size*2,hidden_size)

        self.linear = nn.Linear(hidden_size,classes)

        self.softmax = nn.Softmax(dim=1)
        self.relu = nn.PReLU()
    
    def forward(self, CC,UC ):

        CC_embedding  = self.embedding(CC)
        UC_embedding = self.embedding(UC)        
        
        UC_lstm, _ = self.lstm_CC (UC_embedding.float())
        CC_lstm, _ = self.lstm_CC (CC_embedding.float())

        UC_lstm_flatten  = UC_lstm.reshape(UC_lstm.shape[0],-1)
        CC_lstm_flatten  = CC_lstm.reshape(CC_lstm.shape[0],-1) # batch_no * (feature_vector * no_tokens)
        
        

        linear_output_one = self.linear_post_flatten(torch.cat([UC_lstm_flatten,CC_lstm_flatten],axis=1).float())
        linear_output_two = self.linear_post_flatten2(self.relu(linear_output_one))
        linear_output_three = self.linear_post_flatten3(self.relu(linear_output_two))
        linear_output = self.linear(self.relu(linear_output_three))
    
        return linear_output