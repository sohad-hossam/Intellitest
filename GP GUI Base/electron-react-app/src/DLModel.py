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

  def __init__(self,embedding_matrix : np.array, embedding_dim : tuple,UC_size: int, CC_size:int, 
               hidden_size:int = 128, classes: int = 2,stride_size = 2,kernal_size=3):
      super(DLModel, self).__init__()

      self.embedding = nn.Embedding(num_embeddings = embedding_dim[0], embedding_dim = embedding_dim[1], _weight = torch.tensor(embedding_matrix))
      
      self.lstm = nn.LSTM(embedding_dim[1],hidden_size,batch_first=True, bidirectional=False)

      self.conv_layer = nn.Conv1d(CC_size + UC_size, hidden_size, kernal_size, stride=stride_size)
      self.pooling_layer = nn.MaxPool1d(kernal_size,stride = stride_size)
    
      conv_size_l = math.floor((((UC_size + CC_size) - kernal_size) / stride_size ) + 1)
      conv_size_w = math.floor(((hidden_size - kernal_size) / stride_size ) + 1)
      pooling_size = math.floor(((conv_size_w - kernal_size) / stride_size ) + 1)
        
      self.linear = nn.Linear(hidden_size * pooling_size ,classes)
    
  def forward(self, CC,UC ):
    
    CC_embedding  = self.embedding(CC)
    UC_embedding = self.embedding(UC)    
    
    UC_lstm,_ = self.lstm (UC_embedding.float())
    CC_lstm,_ = self.lstm (CC_embedding.float())

    UC_CC = torch.cat([UC_lstm,CC_lstm],axis=1)
    conv = self.conv_layer (UC_CC)
    
    pooling = self.pooling_layer (conv)    

    pooling_flatten = pooling.reshape(pooling.shape[0],-1)

    linear_output = self.linear(pooling_flatten)
    
    

    return linear_output