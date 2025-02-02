import React, { useState, useEffect } from "react";
import "./MaintainabilityScores.css";
import "bootstrap/dist/css/bootstrap.min.css";
import { Header } from "../TopBar/TopBar";
import { PageTitle } from "../PageTitle/PageTitle";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faFolder, faFileCsv, faFileAlt, faFileCode, faSearch } from '@fortawesome/free-solid-svg-icons';

const RenderFolderStructure = ({ folder, directoryPath, onFileClick, searchQuery }) => {
  const [isExpanded, setIsExpanded] = useState(false);

  const toggleFolder = () => {
    setIsExpanded(!isExpanded);
  };

  const getFileExtension = (filename) => {
    return filename.split('.').pop().toLowerCase();
  };

  const matchesSearchQuery = (name) => {
    return name.toLowerCase().includes(searchQuery.toLowerCase());
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

  const handleFileClick = (file) => {
    const filePath = `${directoryPath}/${file.name}`;
    onFileClick({ ...file, path: filePath });
  };

  const filterAndCombineFolders = (folder) => {
    let combinedName = folder.name;
    let currentFolder = folder;

    while (currentFolder.children && currentFolder.children.length === 1 && currentFolder.children[0].type === 'folder') {
      currentFolder = currentFolder.children[0];
      combinedName += `/${currentFolder.name}`;
    }

    return { ...currentFolder, name: combinedName };
  };

  const filteredChildren = folder.children.filter((child) => {
    return child.type === 'folder' || (matchesSearchQuery(child.name) && getFileExtension(child.name) === 'java');
  }).map((child) => {
    return child.type === 'folder' ? filterAndCombineFolders(child) : child;
  });

  return (
    <div key={folder.name}>
      <div>
        <span onClick={toggleFolder}>
          {getFileIcon(folder.name, folder.type)}
          &nbsp;&nbsp;
          {folder.name}
        </span>
      </div>
      {folder.children && isExpanded && (
        <div className="child-container">
          {filteredChildren.map((child) => (
            <div key={child.name}>
              {child.type === 'folder' ? (
                <RenderFolderStructure folder={child} directoryPath={`${directoryPath}/${child.name}`} onFileClick={onFileClick} searchQuery={searchQuery} />
              ) : (
                <span onClick={() => handleFileClick(child)}>
                  {getFileIcon(child.name, child.type)}
                  &nbsp;&nbsp;
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

const ProgressBar = ({ label, score, onMouseEnter, onMouseLeave }) => {
  return (
    <div className="progress-bar-container mt-3" onMouseEnter={onMouseEnter} onMouseLeave={onMouseLeave}>
      <div className="row align-items-center">
        <div className="col-md-4 text-start">
          <div className="progress-label">{label}</div>
        </div>
        <div className="col-md-8">
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
    "Maintainability Scores",
    "Trace Links",
    "About Us",
    "FilesDisplay",
  ];
  const [scores, setScores] = useState({});
  const [loading, setLoading] = useState(true);
  const [selectedFile, setSelectedFile] = useState(null);
  const [selectedScoreKey, setSelectedScoreKey] = useState(null);
  const [folderStructure, setFolderStructure] = useState(null);
  const [searchQuery, setSearchQuery] = useState('');

  const itemsPerPage = 10;
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
      const fileContentResponse = await fetch(`http://localhost:5000/get-file-content?file_path=${file.path}`);
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

  const toggleCardVisibility = (key, isVisible) => {
    setSelectedScoreKey(isVisible ? key : null);
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
          onMouseEnter={() => toggleCardVisibility(key, true)}
          onMouseLeave={() => toggleCardVisibility(key, false)}
        />
        {selectedScoreKey === key && (
          <div className="items-card-container">
            <div className={`items-card mt-3 ${selectedScoreKey === key ? 'show' : 'hide'}`}>
              <h4 className="card-title">{key} - Maintainability Score : <span className="score-value">{Math.round(value.maintainability_score)}%</span></h4>
              <div className="m-3">
                <div className="info-item">
                  <span className="info-label">Program vocabulary:</span> <span className="info-value">{Math.round(value.halstead_volume_results.D)}</span>
                </div>
                <div className="info-item">
                  <span className="info-label">Program length:</span> <span className="info-value">{Math.round(value.halstead_volume_results.N)}</span>
                </div>
                <div className="info-item">
                  <span className="info-label">Calculated program length:</span> <span className="info-value">{value.halstead_volume_results.N_hat.toFixed(3)}</span>
                </div>
                <div className="info-item">
                  <span className="info-label">Volume:</span> <span className="info-value">{((value.halstead_volume_results.V / 1000) * 10).toFixed(3)}</span>
                </div>
                <div className="info-item">
                  <span className="info-label">Difficulty:</span> <span className="info-value">{((value.halstead_volume_results.D / 100) * 10).toFixed(3)}</span>
                </div>
                <div className="info-item">
                  <span className="info-label">Effort:</span> <span className="info-value">{((value.halstead_volume_results.E / 100000) * 10).toFixed(3)}</span>
                </div>
                <div className="info-item">
                  <span className="info-label">Time required to program:</span> <span className="info-value">{(value.halstead_volume_results.T / 60).toFixed(3)}</span>
                </div>
                <div className="info-item">
                  <span className="info-label">Number of delivered bugs:</span> <span className="info-value">{value.halstead_volume_results.B.toFixed(3)}</span>
                </div>
                <div className="info-item">
                  <span className="info-label">SLOC:</span> <span className="info-value">{Math.round(value.sloc_and_comment_lines_results.SLOC)}</span>
                </div>
                <div className="info-item">
                  <span className="info-label">Comment Lines Ratio:</span> <span className="info-value">{value.sloc_and_comment_lines_results.comment_lines_ratio.toFixed(3)}</span>
                </div>
                <div className="info-item">
                  <span className="info-label">Cyclomatic Complexity:</span> <span className="info-value">{value.cyclomatic_complexity.toFixed(3)}</span>
                </div>
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

  const styles = {
    searchContainer: {
      position: 'relative',
      marginBottom: '20px',
    },
    searchIcon: {
      position: 'absolute',
      top: '50%',
      left: '10px',
      transform: 'translateY(-50%)',
      color: '#123434',
    },
    searchInput: {
      padding: "5px 10px 5px 30px",
      width: "100%",
      borderRadius: "10px",
      backgroundColor: "white",
      color: "#123434",
      border: "3px solid #123434",
    }
  };
  
  if (!folderStructure) {
    return null;
  }

  return (
    <div className="App">
      <Header visibleHyperlinks={visibleHyperlinks} activeLink="Maintainability Scores" />
      <div className="container mohtawa mt-5">
        <div className="row">
          <div className="col-md-4 tree-struc">
            <div style={styles.searchContainer}>
              <FontAwesomeIcon icon={faSearch} style={styles.searchIcon} />
              <input
                type="text"
                placeholder="Go to file"
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                style={styles.searchInput}
              />
            </div>
            <RenderFolderStructure folder={folderStructure} directoryPath="GP GUI Base/electron-react-app/src/uploads" onFileClick={handleFileClick} searchQuery={searchQuery} />
          </div>
          <div className="col-md-8 p-2">
            <PageTitle title="Maintainability Scores" />
            {renderScores()}
          </div>
        </div>
        <nav aria-label="Page navigation example">
          <ul className="pagination justify-content-center mt-3">
            {[...Array(totalPages).keys()].map((page) => (
              <li key={page} className={`page-item ${currentPage === page + 1 ? "active" : ""}`}>
                <button className={`page-link ${currentPage === page + 1 ? "btn-primary" : ""}`} onClick={() => handlePageChange(page + 1)}>
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
