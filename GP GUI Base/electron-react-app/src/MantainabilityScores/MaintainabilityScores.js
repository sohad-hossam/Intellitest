import React, { useState, useEffect } from "react";
import "./MaintainabilityScores.css";
import "bootstrap/dist/css/bootstrap.min.css";
import { Header } from "../TopBar/TopBar";
import { PageTitle } from "../PageTitle/PageTitle";

const ProgressBar = ({ label, score, onClick }) => {
  return (
    <div className="progress-bar-container mt-3" onClick={onClick}>
      <div className="row align-items-center">
        <div className="col-md-3">
          <div className="progress-label">{label}</div>
        </div>
        <div className="col-md-9">
          <div className="progress">
            <div
              className="progress-bar"
              role="progressbar"
              style={{ width: `${score}%` }}
              aria-valuenow={score}
              aria-valuemin="0"
              aria-valuemax="100"
            ></div>
          </div>
        </div>
      </div>
    </div>
  );
};

export function MaintainabilityScore() {
  const visibleHyperlinks = [
    "Home",
    "About Us",
    "Maintainability Scores",
    "Trace Links",
  ];
  const [scores, setScores] = useState({});
  const [loading, setLoading] = useState(true);
  const [selectedFile, setSelectedFile] = useState(null);
  const [selectedScoreKey, setSelectedScoreKey] = useState(null); // Add local state for selected score key

  const itemsPerPage = 6;
  const totalPages = Math.ceil((scores ? Object.keys(scores) : []).length / itemsPerPage);
  const [currentPage, setCurrentPage] = useState(1);

  const handlePageChange = (page) => {
    setCurrentPage(page);
  };

  const handleFileChange = (event) => {
    setSelectedFile(event.target.files[0]);
  };

  useEffect(() => {
    if (selectedFile) {
      fetchMaintainabilityScores();
    }
  }, [selectedFile]);

  useEffect(() => {
    console.log("Scores updated:", scores);
  }, [scores]);

  const fetchMaintainabilityScores = async () => {
    setLoading(true);
    try {
      const formData = new FormData();
      formData.append("file", selectedFile);

      const response = await fetch("http://localhost:5000/compute-score", {
        method: "POST",
        body: formData,
      });
      const data = await response.json();

      if (response.ok) {
        setScores((prevScores) => ({
          ...prevScores,
          ...data,
        }));
      } else {
        console.error("Error fetching maintainability scores:", data.error);
      }
    } catch (error) {
      console.error("Error fetching maintainability scores:", error);
    } finally {
      setLoading(false);
    }
  };

  const toggleCardVisibility = (key) => {
    setSelectedScoreKey(key === selectedScoreKey ? null : key); // Toggle visibility
  };

  const renderScores = () => {
    if (loading) {
      return <div>Loading...</div>;
    }
    if (!scores || Object.keys(scores).length === 0) {
      return <div>No scores available.</div>;
    }
    console.log("scores content", scores);
    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    const slicedScores = Object.entries(scores).slice(startIndex, endIndex);

    return slicedScores.map(([key, value], index) => (
      <div key={index}>
        <ProgressBar
          label={key}
          score={value.maintainability_score}
          onClick={() => toggleCardVisibility(key)} 
        />
        {selectedScoreKey === key && ( 
            <div className="items-card-container">
          <div className="items-card mt-3">
           <h4 className="card-title">{key} - Maintainability Score : {Math.round(value.maintainability_score)}%</h4>
            <div className="m-3">
              Program vocabulary: {value.halstead_volume_results.n} <br />
              Program length: {value.halstead_volume_results.N} <br />
              Calculated program length: {value.halstead_volume_results.N_hat} <br />
              Volume: {value.halstead_volume_results.V} <br />
              Difficulty: {value.halstead_volume_results.D} <br />
              Effort: {value.halstead_volume_results.E} <br />
              Time required to program: {value.halstead_volume_results.T} <br />
              Number of delivered bugs: {value.halstead_volume_results.B} <br />
              SLOC: {value.sloc_and_comment_lines_results.SLOC} <br />
              Comment Lines Ratio: {value.sloc_and_comment_lines_results.comment_lines_ratio} <br />
              Cyclomatic Complexity: {value.cyclomatic_complexity}
            </div>
          </div>
          </div>
        )}
      </div>
    ));
  };

  return (
    <div className="App">
      <Header
        visibleHyperlinks={visibleHyperlinks}
        activeLink="Maintainability Scores"
      />
      <PageTitle
        title={"Maintainability Scores"}
        activeLink="Maintainability Scores"
      />
      <div className="container mt-5">
        <div className="row justify-content-center">
          <div className="col-md-9">
        
            <div className="file-input">
              <input type="file" onChange={handleFileChange} />
              <label className="file-input-label">Choose File</label>
            </div>

            {renderScores()}
          </div>
        </div>
        <nav aria-label="Page navigation example">
          <ul className="pagination justify-content-center mt-3 ">
            {[...Array(totalPages).keys()].map((page) => (
              <li
                key={page}
                className={`page-item ${
                  currentPage === page + 1 ? "active" : ""
                }`}
              >
                <button
                  className={`page-link ${
                    currentPage === page + 1 ? "btn-primary" : ""
                  }`}
                  onClick={() => handlePageChange(page + 1)}
                >
                  {page + 1}
                </button>
              </li>
            ))}
          </ul>
        </nav>
      </div>
    </div>
  );
}
