import React, { useState, useEffect } from "react";
import "./TraceLinks.css";
import "bootstrap/dist/css/bootstrap.min.css";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSpinner } from '@fortawesome/free-solid-svg-icons';

import { Header } from "../TopBar/TopBar";
import { PageTitle } from "../PageTitle/PageTitle";

function DropDowns() {
  const [step, setStep] = useState(0); 
  const [useCaseOptions, setUseCaseOptions] = useState([]);
  const [codeOptions, setCodeOptions] = useState([]);
  const [useCaseSelected, setUseCaseSelected] = useState(false);
  const [codeSelected, setCodeSelected] = useState(false);
  const [score, setScore] = useState(null);
  const [showScoreMessage, setShowScoreMessage] = useState(false);
  const [url, setUrl] = useState('');
  const [useCaseChoice, setUseCaseChoice1] = useState(null);
  const [invalidurl, setInvalidUrl] = useState(false);
  const [loading, setLoading] = useState(false);
  const [invalidtrace, setinvalidtrace] = useState(false);
  
  useEffect(() => {
    fetchUseCaseFiles();
    fetchCodeFiles();
  }, []);

  const fetchUseCaseFiles = async () => {
    try {
      const response = await fetch('http://localhost:5000/get-usecase-files?folder_path=GP GUI Base/electron-react-app/src/uploads/usecase_files');
      if (!response.ok) {
        throw new Error('Failed to fetch use case files');
      }
      const data = await response.json();
      const files = data.files || [];
      setUseCaseOptions(files);
    } catch (error) {
      console.error('Error fetching use case files:', error);
    }
  };

  const fetchCodeFiles = async () => {
    try {
      const response = await fetch('http://localhost:5000/get-java-files');
      if (!response.ok) {
        throw new Error('Failed to fetch code files');
      }
      const data = await response.json();
      const files = data.java_files || [];
      setCodeOptions(files);
    } catch (error) {
      console.error('Error fetching code files:', error);
    }
  };
  
  const handleCaseSelection = async (option) => {
    try {
        // Fetch the content of the selected use case file
        const response = await fetch('http://localhost:5000/get-usecase-content', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ usecase_file_name: option })
        });
        if (!response.ok) {
            throw new Error('Failed to fetch use case content');
        }
        const data = await response.json();
        const useCaseContent = data.usecase_content;

        // Update the state with the selected use case content and option
        setUseCaseSelected(useCaseContent);
      

        // Proceed with the necessary actions (handleProcessDocs and setStep)
        handleProcessDocs();
        setStep(3);
    } catch (error) {
        console.error('Error selecting use case:', error);
        // Handle error (e.g., show error message)
    }
};

  const handleCodeSelection = (option) => {
    setInvalidUrl(false);
    setScore(null);
    setCodeSelected(option);
    setStep(1);
  };
  
  const setUseCaseChoice = (choice) => {
    setUseCaseChoice1(choice);
    setStep(2);
  };

  const handleUrlSubmit = async () => {
    try {
      setInvalidUrl(false);
      setScore(null);
      const response = await fetch('http://localhost:5000/fetch-details', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ url })
      });

      if (!response.ok) {
        setInvalidUrl(true);
        setStep(2);
        setScore(null);
        throw new Error('Failed to fetch the URL');

      }

      const data = await response.json();
      setInvalidUrl(false);
      const useCaseContent = `${data.summary}\n${data.description}`;
      setUseCaseSelected(useCaseContent);
      setStep(3);
      handleProcessDocs();

    } catch (error) {
      console.error('Error fetching the URL:', error);
      setInvalidUrl(true);
    }
   
    
  };

  const handleProcessDocs = async () => {
    if (!useCaseSelected || !codeSelected) {
      return;
    }
  
    setLoading(true);
    setStep(3);
  
    try {
      const requestBody = {
        usecase: useCaseSelected,
        code: codeSelected
      };
  
      const response = await fetch('http://localhost:5000/compute-tracelinks', {
        method: "POST",
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestBody)
      });
  
      if (!response.ok) {
        setinvalidtrace(true);
        throw new Error('Failed to compute trace links');
      }
  
      const data = await response.json();
      setScore(data.trace_links);
    } catch (error) {
      console.error('Error computing trace links:', error);
    } finally {
      setLoading(false);
      setStep(4);
    }
  };
  
  

  return (
    <div className="mt-2">

      <div className="timeline mb-5">
        <div className={`step ${step >= 0 ? 'completed' : ''}`}></div>
        <div className={`circle ${step >= 0 ? 'completed' : ''}`}>1</div>
        <div className={`step ${step >= 1 ? 'completed' : ''}`}></div>
        <div className={`circle ${step >= 1 ? 'completed' : ''}`}>2</div>
        <div className={`step ${step >= 2 ? 'completed' : ''}`}></div>
        <div className={`circle ${step >= 2 ? 'completed' : ''}`}>3</div>
        <div className={`step ${step >= 3 ? 'completed' : ''}`}></div>
        <div className={`circle ${step >= 3 ? 'completed' : ''}`}>4</div>
        <div className={`step ${step >= 4 ? 'completed' : ''}`}></div>
      </div>
      <div className="fixe">
        <div className="row">
          <div className="col-md-3 move">
            <div className={`card ${step === 0 ? 'active' : ''} ${step >= 0 ? 'fixed-height' : ''}`}>
              <div className="card-body">
                <h5 className="card-title">Choose Code File</h5>
                <div className="code-options-container">
                  {codeOptions.map((option, index) => (
                    <div 
                      key={index} 
                      className={`code-option ${codeSelected === option ? 'selected' : ''}`}
                      onClick={() => handleCodeSelection(option)}
                    >
                      <p className="card-text">{option}</p>
                    </div>
                  ))}
                </div>
                {step > 0 && <span className="badge bg-success">Completed</span>}
              </div>
            </div>
          </div>
          <div className="col-md-3">
            <div className={`card ${step === 1 ? 'active' : ''} ${step >= 1 ? 'fixed-height' : ''}`}>
              <div className="card-body">
                <h5 className="card-title">Choose Use Case Option</h5>
                {step > 0 && (
                  <div className="text-center mt-5">
                    <p>We offer two options for handling use case files: you can either select an existing use case file from the project documents, or provide us with the URL of the required use case.</p>
                    <div className="button-container mt-5">
                      <button className="optionButton" onClick={() => setUseCaseChoice(0)}>Choose Use Case</button>
                      <button className="optionButton" onClick={() => setUseCaseChoice(1)}>Upload URL</button>
                    </div>
                  </div>
                )}
                {step > 1 && <span className="badge bg-success">Completed</span>}
              </div>
            </div>
          </div>
          <div className="col-md-3">
            <div className={`card ${step === 2 ? 'active' : ''} ${step >= 2 ? 'fixed-height' : ''}`}>
              <div className="card-body">
                <h5 className="card-title">Choose Use Case File</h5>
                {useCaseChoice === 0 ? (
                  <div className="code-options-container">
                    {useCaseOptions.map((option, index) => (
                      <div 
                        key={index} 
                        className={`use-option  ${useCaseSelected === option ? 'selected' : ''}`}
                        onClick={() => handleCaseSelection(option)}
                      >
                        <p className="card-text">{option}</p>
                      </div>
                    ))}
                  </div>
                ) : useCaseChoice === 1 ? (
                  <div className="form-group  text-center">
                    
                    <input 
                      type="text" 
                      className="form-control" 
                      id="urlInput" 
                      value={url} 
                      onChange={(e) => setUrl(e.target.value)} 
                      placeholder="Enter URL" 
                    />
                    <button  className="optionButton" onClick={handleUrlSubmit}>Submit</button> 
                    {invalidurl && (
                  <div className="tracelink-message-low  text-center">
                   Invalid Url . Please enter a valid issue Url
                  </div>
                )}
                  </div>
                ) : null}
                {step > 2 && <span className="badge bg-success">Completed</span>}
              </div>
            </div>
          </div>
          <div className="col-md-3 moveer">
  <div className={`card ${step === 3 ? 'active' : ''} ${step >= 3 ? 'fixed-height' : ''}`}>
    <div className="card-body">
      <h5 className="card-title">Process Documents</h5>
      {loading ? (
        <div className="text-center mt-5">
          <FontAwesomeIcon icon={faSpinner} spin />
          <p>Files are being processed...</p>
        </div>
      ) : (
        <>
          {score != null && score <= 50 && (
            <div className="tracelink-message-low text-center  mt-5">
              Trace Links Exist between the documents by {score.toFixed(2)}% indicating low correlation
            </div>
          )}
          {score != null && score > 50 && (
            <div className="tracelink-message text-center  mt-5">
              Trace Links Exist between the documents by {score.toFixed(2)}% indicating high correlation
            </div>
          )}
          {invalidtrace==true && (
                  <div className="tracelink-message-low  text-center mt-5">
                  Unable to compute traceLinks between the corresponding files.
                  </div>
                )}
          {step > 3 && <span className="badge bg-success">Completed</span>}
        </>
      )}
    </div>
  </div>
</div>
        </div>
      </div>
    </div>
  );
}

export function TraceLinks() {
  const visibleHyperlinks = [
    "Home",
    "About Us",
    "Maintainability Scores",
    "Trace Links",
    "FilesDisplay",
  ];

  return (
    <div className="App">
      <Header visibleHyperlinks={visibleHyperlinks} activeLink="Trace Links" />
      <DropDowns />
    </div>
  );
}
