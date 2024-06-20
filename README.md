# Intellitest -  Automated Traceability Link Generation and Bug Localization Framework 
## Table of Contents
1. [Project Overview](#project-overview)
2. [Features](#features)
3. [Technologies Used](#technologies-used)
4. [Project Document](#project-document)
5. [Installation](#installation)
6. [Usage](#usage)
8. [Project Poster](#project-poster)
9. [System Architecture](#system-architecture)
10. [Contact Us](#contact)

    
## Project Overview
The increasing complexity of software systems demands effective methods for understanding and maintaining source codes. In this context, traceability links between software artifacts, such as requirements, code, and documentation, play a crucial role in facilitating software comprehension, maintenance, and evolution. This project focuses on developing a comprehensive framework for automated trace link generation between use case documents and Java code files, leveraging machine learning techniques and natural language processing (NLP) tools.

The project comprises several modules, each serving a specific purpose in the traceability link generation process. The Data Collection Module retrieves and preprocesses relevant data, including use case documents, Java code files, and associated metadata. The Feature Extraction Module extracts features from both textual and code-based sources, capturing essential information for link prediction. Subsequently, the Model Prediction Module applies machine learning algorithms, such as Random Forest, to predict trace links based on the extracted features, as well as the Deep Learning Prediction Module for benchmarking the machine learning outputs. Additionally, the Bug Localization Module aims to locate buggy versions of code files by analyzing bug reports and version control data.

Throughout the development process, various software tools and technologies are employed, including Visual Studio Code, Python, Jupyter Notebook, TensorFlow, and TreeSitting. These tools facilitate tasks such as code editing, data analysis, machine learning model training, and parsing source code. Furthermore, libraries such as NumPy, Pandas, and Scikit-learn provide essential functionalities for data manipulation, numerical computing, and machine learning tasks.

The proposed framework contributes to the automation of traceability link generation, reducing manual effort, and improving software maintenance efficiency. By establishing trace links between use case documents and code files, developers can better understand system requirements, track changes, and ensure consistency between software artifacts. Overall, this project aims to enhance software comprehension and maintenance processes, ultimately leading to the development of more reliable and maintainable software systems.

## Features
- Automated trace link generation between use case documents and Java code files.
- Feature extraction from textual and code-based sources.
- Machine learning and deep learning models for link prediction.
- Bug localization by analyzing bug reports and version control data.

## Technologies Used
- **Programming Languages:** Python, JS, HTML , Css
- **Libraries and Frameworks:** TensorFlow, NumPy, Pandas, Scikit-learn, React, NLTK
- **Development Tools:** Visual Studio Code, Jupyter Notebook, TreeSetter

## Project Document 
- https://docs.google.com/document/d/1kU-c8wsEUrNs8FKT7EbB6aFeMTJcEd2E/edit


## Installation
### Prerequisites
- Python 3.8 or higher
- TensorFlow 2.4.1
- Required Python packages (listed in `requirements.txt`)

### Steps
1. **Clone the Repository:**
    ```sh
   [ git clone https://github.com/yourusername/traceability-link-generation.git](https://github.com/sohad-hossam/GP-codebase.git)
    ```

2. **Install Required Packages:**
    ```sh
    pip install -r requirements.txt
    ```

## Usage
1. **Run The Website:**
    ```sh
   npm start
   python src/app.py
    ```

2. **Run Feature Extraction Module:**
    ```sh
 
    ```

3. **Run Model Prediction Module:**
    ```sh
   
    ```

4. **Run Bug Localization Module:**
    ```sh
   =
    ```
## Project Poster
![image](https://github.com/sohad-hossam/GP-codebase/assets/84807559/86733e41-a4e7-400f-aae5-0bce86c63f5e)


## System Architecture

The architecture of IntelliTest is covered in this section. We will review each module to illustrate how the modules work together.


## Preprocessor Module
![image](https://github.com/sohad-hossam/GP-codebase/assets/84807559/5a01e852-a068-4a20-9e0b-77c2d0334abb)

### Functional Description

The Preprocessor module processes both source code files and use case (UC) documents to prepare them for further analysis by cleaning, tokenizing, and filtering content.

#### Cleaning and Tokenizing Source Code Files:
- Removing punctuation marks, numeric characters, and stopwords.
- Lowercasing all words.
- Stemming words using the Porter Stemming Algorithm.
- Filtering out Java keywords to focus on relevant content.

#### Cleaning and Tokenizing Use Case (UC) Documents:
- Removing numeric characters and stopwords.
- Lowercasing all words.
- Stemming words using the Porter Stemming Algorithm.
- Filtering out specific domain-related keywords.

#### Setting Up Documents and Tokens:
- Reading files from provided paths.
- Storing tokenized documents.
- Generating sets of unique tokens for both source code and UC documents.

### Modular Decomposition

The Preprocessor module is composed into the following functions:
- **CodePreProcessor:** Processes source code files by cleaning, tokenizing, and filtering out irrelevant information based on Java keywords.
- **UCPreProcessor:** Processes UC documents by cleaning, tokenizing, and filtering out irrelevant information based on specific domain-related keywords.
- **setup:** Sets up documents and tokens by iterating through files in provided directories, applying preprocessing functions, and collecting tokenized documents and unique tokens.

### Design Constraints

- **Language Constraint:** The preprocessing steps are tailored for Java source code, considering its syntax and common conventions. Key Words must differ in other languages; the preprocessor is adjusted to work with Java codes.

## Maintainability Score 
![image](https://github.com/sohad-hossam/GP-codebase/assets/84807559/b53df3c9-2060-4579-938f-37a800cbdb2f)


### Functional Description

The Maintainability Score Module calculates a maintainability score for source code files based on various metrics.

### Modular Decomposition

- **Parsing Source Code:** Utilizes a Tree-sitter parser for Java to parse the source code files.
- **Identifying Parameters:** Operands count, operators count, unique operands count, unique operators count.
- **Computing Metrics:** Program vocabulary, program length, calculated program length, volume, difficulty, effort, time required to program, number of delivered bugs.
- **Maintainability Score Equation:** 
  ```Maintainability Score = max(0, (100*(171 - 5.2*np.log(V) - 0.23*G - 16.2*np.log(SLOC))) / 171)```

### Design Constraints

- Dependent on Tree-sitter as a parsing technique. Compared to another technique called “javalang,” Tree-sitter output matches the needed parameters better.

## Features Extraction Module

### Functional Description

Integrates a total of 131 features mentioned in the “Predicting Query Quality for Applications of Text Retrieval to Software Engineering Tasks” and “Automatic Traceability Maintenance via Machine Learning Classification” papers to ensure the best results.

### Modular Decomposition

- **TF-IDF Vectorizer:** Processes source code files and UC documents.
- **Feature Computation:** Generates 131 features including IR-Based Features, Pre-retrieval Features, Post-retrieval Features, and Document Statistics Features.
- **Further Processing:** Normalizing features, feature selection, feature mapping, and handling data imbalance.

## Model Prediction Module
![image](https://github.com/sohad-hossam/GP-codebase/assets/84807559/1649cf92-25cd-43b7-ab49-ad77167e1e00)

### Functional Description

Feeds the data into a machine learning model (Random Forest) for predicting trace links, reaching an accuracy of 70% and F1-score of 0.7.

### Modular Decomposition

- **Feature Selection:** Identifying and retaining relevant features to improve model performance.
- **Feature Mapping:** Associating each data point with its corresponding features.
- **Data Imbalance:** Using BorderlineSMOTE for creating synthetic samples to balance class distribution.
- **Model Training and Prediction:** Training and predicting using RandomForestRegressor.

## Deep Learning Module

### Functional Description

Trains and evaluates a neural network model for traceability link prediction and benchmarking.

### Modular Decomposition

- **Data Retrieval and Preparation**
- **Data Preprocessing**
- **Model Definition and Training**
- **Model Evaluation**
- **Embedding and Vocabulary Management**

### Design Constraints

- Data dependency on a specific dataset in SQLite format.
- Computational resource requirements.
- Handling of unknown tokens.
- Embedding quality.
- Loss function and class imbalance handling.

##  Bug Localization Module
![image](https://github.com/sohad-hossam/GP-codebase/assets/84807559/758391f8-efc4-4f56-8afa-4f9f46b2f713)

### Functional Description

Identifies and locates specific code changes associated with resolved bug reports by processing Jira issues, extracting commit information, and analyzing code changes.

### Modular Decomposition

- **Bug Report Retrieval**
- **Commit Hash Retrieval**
- **Commit Data Extraction**
- **Code Parsing**
- **Tokenization and Preprocessing**
- **Word Embedding Training**
- **Data Indexing**

### Design Constraints

- Data availability and consistency.
- API rate limits.
- Computational resource requirements.
- Tokenization accuracy.
- Security of access to GitHub repository and database.

## Contact Us

For any questions or suggestions, please feel free to reach out to any of our team members:

### Team Members

**Member 1**
- **GitHub:** [github.com/yasmin-hashem24](https://github.com/yasmin-hashem24)
- **LinkedIn:** [linkedin.com/in/yasmin-hashem2024](https://www.linkedin.com/in/yasmin-hashem2024/)
- **Email:** [yasmin.hashem201@gmail.com](mailto:yasmin.hashem201@gmail.com)

**Member 2**
- **GitHub:** [github.com/sohad-hossam](https://github.com/sohad-hossam)
- **LinkedIn:** [linkedin.com/in/member2](https://linkedin.com/in/member2)
- **Email:** [sohad95husam@gmail.com](mailto:sohad95husam@gmail.com)

**Member 3**
- **GitHub:** [github.com/yasmiinezaki](https://github.com/yasmiinezaki)
- **LinkedIn:** [linkedin.com/in/yasmiinezaki](https://linkedin.com/in/yasmiinezaki)
- **Email:** [yasmeen.zaki01@gmail.com](mailto:yasmeen.zaki01@gmail.com)

**Member 4**
- **GitHub:** [github.com/bassabt-hisham](https://github.com/bassant-hisham).
- **LinkedIn:** [linkedin.com/in/bassant-hisham]([https://linkedin.com/in/member4](https://www.linkedin.com/in/bassant-hisham/))
- **Email:** [bassentkhafagi@gmail.com](mailto:bassentkhafagi@gmail.com)


