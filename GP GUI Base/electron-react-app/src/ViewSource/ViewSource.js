import React, { useState, useEffect } from "react";
import { Header } from "../TopBar/TopBar";
import { PageTitle } from "../PageTitle/PageTitle";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faFolder, faFileCsv, faFileAlt, faFileCode } from '@fortawesome/free-solid-svg-icons';
import {CodeEditor} from "../CodeEditor/CodeEditor";

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
  
    const handleFileClick = (folder,file) => {
      const filePath = `${directoryPath}/${folder.name}/${file.name}`;
      onFileClick({ ...file, path: filePath }); 
    };
  
    return (
      <div key={folder.name}>
        <div>
          <span onClick={toggleFolder}>
            {getFileIcon(folder.name, folder.type)}
            {folder.name}
          </span>
        </div>
        {folder.children && isExpanded && (
          <div style={{ marginLeft: '20px' }}>
            {folder.children.map((child) => (
              <div key={child.name}>
                {child.type === 'folder' ? (
                  <RenderFolderStructure folder={child} directoryPath={directoryPath} onFileClick={onFileClick} />
                ) : (
                  <span onClick={() => handleFileClick(folder,child)}>
                    {getFileIcon(child.name, child.type)}
                    {child.name}
                  </span>
                )}
              </div>
            ))}
          </div>
        )}
      </div>
    );
  };
  
  function ViewSource() {
    const [folderStructure, setFolderStructure] = useState(null);
    const [selectedFileContent, setSelectedFileContent] = useState('');
    const [selectedFileType, setSelectedFileType] = useState('');
    const visibleHyperlinks = [
      "Home",
      "About Us",
      "Import Project",
      "Source Code",
    ];
  
    useEffect(() => {
      fetchFolderStructure();
    }, []);
  
    const fetchFolderStructure = () => {
      fetch("http://localhost:5000/get-folder-structure?directory_path=GP GUI Base/electron-react-app/src/uploads")
     
        .then((response) => response.json())
        .then((data) => {
          setFolderStructure(data);
          console.log(data);
        })
        .catch((error) => {
          console.error("Error fetching folder structure:", error);
        });
    };
  
    const handleFileClick = (file) => {
      setSelectedFileType(getFileExtension(file.name));
      console.log("file name: ", file.name);
      console.log("file path: ", file.path);
      fetch(`http://localhost:5000/get-file-content?file_path=${file.path}`)
        .then((response) => response.text())
        .then((data) => {
          setSelectedFileContent(data);
        })
        .catch((error) => {
          console.error("Error fetching file content:", error);
        });
    };
  
    const getFileExtension = (filename) => {
      return filename.split('.').pop().toLowerCase();
    };
  
    return (
        <div className="container">
          <Header visibleHyperlinks={visibleHyperlinks} activeLink="Source Code" />
          <PageTitle title={folderStructure ? folderStructure.name.replace(/_/g, ' ').toUpperCase() : ''} activeLink="Source Code" />
          <h1>View Source</h1>
          <div className="row">
            <div className="col-md-4">
              {folderStructure ? (
                <RenderFolderStructure folder={folderStructure} directoryPath="GP GUI Base/electron-react-app/src/uploads/teiid_dataset" onFileClick={handleFileClick} />
              ) : (
                <p>Loading folder structure...</p>
              )}
            </div>
            <div className="col-md-8">
              {selectedFileContent && <CodeEditor content={selectedFileContent} fileType={selectedFileType} />}
            </div>
          </div>
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
    }
    
  
  export { ViewSource };
  