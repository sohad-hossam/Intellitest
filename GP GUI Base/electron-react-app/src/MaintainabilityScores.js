import React, { useState } from 'react';
import './MaintainabilityScores.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Header } from './TopBar/TopBar';
import { PageTitle } from './PageTitle/PageTitle';

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
  const visibleHyperlinks = ['Home', 'About Us', 'Maintainability Scores', 'Trace Links'];
  const scores = {
    "FileYasmina.java": 30,
    "FileYasmina1.java": 80,
    "FileYasmina2.java": 50,
    "FileYasmina3.java": 10,
    "FileYasmina4.java": 100,
    "FileYasmina5.java": 50,
    "FileYasmina7.java": 30,
    "FileYasmina8.java": 80,
    "FileYasmina9.java": 50,
    "FileYasmina30.java": 10,
    "FileYasmina94.java": 100,
    "FileYasmina12.java": 50,
  };

  const itemsPerPage = 6;
  const totalPages = Math.ceil(Object.keys(scores).length / itemsPerPage);
  const [currentPage, setCurrentPage] = useState(1);

  const handlePageChange = (page) => {
    setCurrentPage(page);
  };

  const renderScores = () => {
    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    const slicedScores = Object.entries(scores).slice(startIndex, endIndex);

    return slicedScores.map(([key, value], index) => (
      <ProgressBar key={index} label={key} score={value} />
    ));
  };

  return (
    <div className="App">
      <Header visibleHyperlinks={visibleHyperlinks} activeLink="Maintainability Scores" />
      <PageTitle title={"Maintainability Scores"} />
      <div class="mt-5"></div>
      <div class="mt-5"></div>
      <div className="container mt-5">
        <div className="row justify-content-center">
          <div className="col-md-9">
            {renderScores()}
          </div>
        </div>
        <nav aria-label="Page navigation example">
          <ul className="pagination justify-content-center mt-3 ">
            {[...Array(totalPages).keys()].map((page) => (
              <li key={page} className={`page-item ${currentPage === page + 1 ? 'active' : ''}`}>
              <button className={`page-link ${currentPage === page + 1 ? 'btn-primary' : ''}`} onClick={() => handlePageChange(page + 1)}>{page + 1}</button>
            </li>
            ))}
          </ul>
        </nav>
      </div>
    </div>
  );
}
