import React, { useState, useEffect } from "react";
import "./MaintainabilityScores.css";
import "bootstrap/dist/css/bootstrap.min.css";
import { Header } from "../TopBar/TopBar";
import { PageTitle } from "../PageTitle/PageTitle";

const ProgressBar = ({ label, score }) => {
  return (
    <div className="progress-bar-container mt-3">
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
  const [scores, setScores] = useState({}); // Initialize scores state with null
  const [loading, setLoading] = useState(true);

  const itemsPerPage = 6;
  const totalPages = Math.ceil((scores ? Object.keys(scores) : []).length / itemsPerPage);
  const [currentPage, setCurrentPage] = useState(1);

  const handlePageChange = (page) => {
    setCurrentPage(page);
  };

  useEffect(() => {
    fetchMaintainabilityScores(); 
  }, []); 
  useEffect(() => {
    console.log("Scores updated:", scores);
  }, [scores]);
  
  const fetchMaintainabilityScores = async () => {
    try {
      const response = await fetch("http://localhost:5000/compute-score", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ file_path: "Dataset/teiid_dataset/train_CC/9.java" }),
      });
      const data = await response.json();
  
      if (response.ok) {
        setScores(data); 
      } else {
        console.error("Error fetching maintainability scores:", data.error);
      }
      
      
    } catch (error) {
      console.error("Error fetching maintainability scores:", error);
    } finally {
      setLoading(false);
    }
  };
  
  

  const renderScores = () => {
   
    if (loading) {
      return <div>Loading...</div>; // Display loading indicator while loading
    }
    if (!scores || Object.keys(scores).length === 0) {
   
      return <div>No scores available.</div>; // Or any message for no scores
    }
  
    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    const slicedScores = Object.entries(scores).slice(startIndex, endIndex);
  
    return slicedScores.map(([key, value], index) => (
      <ProgressBar key={index} label={key} score={value} />
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
          <div className="col-md-9">{renderScores()}</div>
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
