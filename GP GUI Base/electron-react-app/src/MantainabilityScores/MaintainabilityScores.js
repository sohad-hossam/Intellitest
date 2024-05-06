import React, { useState, useEffect } from "react";
import "./MaintainabilityScores.css";
import "bootstrap/dist/css/bootstrap.min.css";
import { Header } from "../TopBar/TopBar";
import { PageTitle } from "../PageTitle/PageTitle";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faFolder, faFileCsv, faFileAlt, faFileCode  } from '@fortawesome/free-solid-svg-icons';
const RenderFolderStructure = ({ folder, directoryPath, onFileClick }) => {
  const [isExpanded, setIsExpanded] = useState(false);

  const toggleFolder = () => {
    setIsExpanded(!isExpanded);
  };

  const getFileExtension = (filename) => {
    return filename.split('.').pop().toLowerCase();
  };

  const getFileIcon = (filename, type) => {
    if (type === 'folder') {
      return <FontAwesomeIcon icon={faFolder} />;
    }

    const extension = getFileExtension(filename);
    switch (extension) {
      case 'csv':
        return <FontAwesomeIcon icon={faFileCsv} />;
      case 'txt':
        return <FontAwesomeIcon icon={faFileAlt} />;
      case 'java':
        return <FontAwesomeIcon icon={faFileCode} />;
      default:
        return <FontAwesomeIcon icon={faFileAlt} />;
    }
  };

  const handleFolderClick = (folder, file) => {
    const filePath = `${directoryPath}/${folder.name}/${file.name}`;
    onFileClick({ ...file, path: filePath });
  };

  // Filter out files that are not Java files
  const javaFiles = folder.children.filter((child) => {
    return child.type === 'folder' || getFileExtension(child.name) === 'java';
  });

  return (
    <div key={folder.name}>
      <div>
        <span onClick={toggleFolder}>
          {getFileIcon(folder.name, folder.type)}
          {folder.name}
        </span>
      </div>
      {folder.children && isExpanded && (
        <div className="child-container">
          {javaFiles.map((child) => (
            <div key={child.name}>
              {child.type === 'folder' ? (
                <RenderFolderStructure folder={child} directoryPath={directoryPath} onFileClick={onFileClick} />
              ) : (
                <span onClick={() => handleFolderClick(folder, child)}>
                  {getFileIcon(child.name, child.type)}
                  {child.name}
                </span>
              )}
            </div>
          ))}
        </div>
      )}
       <style>
            {`
              svg {
                font-family: "Russo One", sans-serif;
                display: flex;
                align-items: center;
                justify-content: center;
                width: 5%;
                height: 80%;
              }
            `}
          </style>
    </div>
  );
};

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
  const [selectedScoreKey, setSelectedScoreKey] = useState(null);
  const [folderStructure, setFolderStructure] = useState(null);

  const itemsPerPage = 12;
  const totalPages = Math.ceil((scores ? Object.keys(scores) : []).length / itemsPerPage);
  const [currentPage, setCurrentPage] = useState(1);

  const handlePageChange = (page) => {
    setCurrentPage(page);
  };


  const fetchFolderStructure = () => {
    fetch("http://localhost:5000/get-folder-structure?directory_path=GP GUI Base/electron-react-app/src/uploads")
      .then((response) => response.json())
      .then((data) => {
        setFolderStructure(data);
      })
      .catch((error) => {
        console.error("Error fetching folder structure:", error);
      });
  };

  const handleFileClick = async (file) => {
    setSelectedFile(file);
    setLoading(true);
  
    try {
      const fileContentResponse = await fetch(`http://localhost:5000/get-file-content?file_path=${file.path}`); // Use file.path instead of file
      const fileContent = await fileContentResponse.text();
  
      const formData = new FormData();
      formData.append("file", new Blob([fileContent], { type: "text/plain" }), file.name);
  
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
    setSelectedScoreKey(key === selectedScoreKey ? null : key);
  };

  const renderScores = () => {
    if (!scores || Object.keys(scores).length === 0) {
      return <div>No scores available.</div>;
    }
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

  useEffect(() => {
    fetchFolderStructure();
  }, []);

  useEffect(() => {
    console.log("Scores updated:", scores);
  }, [scores]);

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
        <div className="row">
          <div className="col-md-4  tree-struc"> 
            {folderStructure ? (
              <RenderFolderStructure folder={folderStructure} directoryPath="GP GUI Base/electron-react-app/src/uploads/teiid_dataset" onFileClick={handleFileClick} />
            ) : (
              <p>Loading folder structure...</p>
            )}
          </div>
          <div className="col-md-8">
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
