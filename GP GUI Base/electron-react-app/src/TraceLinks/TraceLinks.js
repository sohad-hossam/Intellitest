import React, { useState, useEffect } from "react";
import "./TraceLinks.css";
import "bootstrap/dist/css/bootstrap.min.css";
import { Header } from "../TopBar/TopBar";
import { PageTitle } from "../PageTitle/PageTitle";


function DropDowns() {
  const [useCaseOptions, setUseCaseOptions] = useState([]);
  const [codeOptions, setCodeOptions] = useState([]);
  const [useCaseSelected, setUseCaseSelected] = useState(false);
  const [codeSelected, setCodeSelected] = useState(false);
  const [score, setScore] = useState(null);
  const [showScoreMessage, setShowScoreMessage] = useState(false); // Flag to show score message


  useEffect(() => {
    fetchUseCaseFiles();
    fetchCodeFiles();
  }, []);

  useEffect(() => {
    // Show score message when score is set
    if (score !== null) {
      setShowScoreMessage(true);
      console.log("Score is set", score);
    }
  }, [score]);
  const fetchUseCaseFiles = async () => {
    try {
      const response = await fetch('http://localhost:5000/get-usecase-files?folder_path=GP GUI Base/electron-react-app/src/uploads/teiid_dataset/test_UC');
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
      const response = await fetch('http://localhost:5000/get-usecase-files?folder_path=GP GUI Base/electron-react-app/src/uploads/teiid_dataset/test_CC');
      if (!response.ok) {
        throw new Error('Failed to fetch code files');
      }
      const data = await response.json();
      const files = data.files || [];
      setCodeOptions(files);
    } catch (error) {
      console.error('Error fetching code files:', error);
    }
  };

  const handleCaseSelection = (event) => {
    setUseCaseSelected(event.target.value !== "");
  };

  const handleCodeSelection = (event) => {
    setCodeSelected(event.target.value !== "");
  };

  const handleProcessDocs = async () => {
    if (!useCaseSelected || !codeSelected) {
      const warningMessage = document.querySelector(".warning-message");
      if (warningMessage) {
        warningMessage.style.display = "block";
      }
    } else {
      const warningMessage = document.querySelector(".warning-message");
      if (warningMessage) {
        warningMessage.style.display = "none";
      }

      const selectedUseCase = document.getElementById("exampleFormControlSelect1").value;
      const selectedCode = document.getElementById("exampleFormControlSelect2").value;

      try {
  
        const response = await fetch(`http://localhost:5000/compute-tracelinks?usecase=${selectedUseCase}&code=${selectedCode}`, {
          method: "POST",
        });
        if (!response.ok) {
          throw new Error('Failed to compute trace links');
        }
        const data = await response.json();
        setScore(data.trace_links);
      } catch (error) {
        console.error('Error computing trace links:', error);
      }
    }
  };

  return (
    <div className="dropDowns mt-5">
      <div className="row justify-content-center align-items-center">
        <div className="col-md-4 d-flex justify-content-center">
          <div className="form-group">
            <label className="drops-label" htmlFor="exampleFormControlSelect1">
              Use Case Document
            </label>
            <select
              className="form-control drops-label"
              id="exampleFormControlSelect1"
              onChange={handleCaseSelection}
            >
              <option value="" selected hidden>
                Use Case
              </option>
              {useCaseOptions.map((option, index) => (
                <option key={index}>{option}</option>
              ))}
            </select>
          </div>
        </div>
        <div className="col-md-4 d-flex justify-content-center">
          <div className="form-group">
            <label className="drops-label" htmlFor="exampleFormControlSelect2">
              Code Document
            </label>
            <select
              className="form-control drops-label"
              id="exampleFormControlSelect2"
              onChange={handleCodeSelection}
            >
              <option value="" selected hidden>
                Code
              </option>
              {codeOptions.map((option, index) => (
                <option key={index}>{option}</option>
              ))}
            </select>
          </div>
        </div>
      </div>

      <div className="row justify-content-center mt-5">
        <div className="col-md-4 d-flex justify-content-center">
          <button
            type="button"
            className="process-btn"
            onClick={handleProcessDocs}
          >
            Process Documents
          </button>
        </div>
      </div>

      <div className="row justify-content-center mt-3">
        <div className="col-md-4">
          <div className="warning-message text-danger">
            Please select a Use Case and a Code Document to process.
          </div>
        </div>
      </div>

      <div className="row justify-content-center mt-5">
        <div className="col-md-4">
        {score!=null &&score>0.5 && (
            <div className="tracelink-message">
              Trace Links Exist between the documents by {score*100}% indicating high correlation
            </div>
          )}
            {score!=null &&score<=0.5 && (
            <div className="tracelink-message-low">
              Trace Links Exist between the documents by {score*100}% indicating low correlation
            </div>
          )}
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
  ];

  return (
    <div className="App">
      <Header visibleHyperlinks={visibleHyperlinks} activeLink="Trace Links" />
      <PageTitle title={"Trace Links"} activeLink="Trace Links" />
      <DropDowns />
    </div>
  );
}
