import React, { useState, useEffect } from "react";
import { Header } from "../TopBar/TopBar";
import { PageTitle } from "../PageTitle/PageTitle";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faFolder, faFileCsv, faFileAlt, faFileCode, faChevronRight } from '@fortawesome/free-solid-svg-icons';
import { CodeEditor } from "../CodeEditor/CodeEditor";
import './ViewSource.css';

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

    const handleFileClick = (folder, file) => {
        const filePath = `${directoryPath}/${folder.name}/${file.name}`; 
        onFileClick({ ...file, path: filePath });
    };

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
                    {folder.children.map((child) => (
                        <div key={child.name}>
                            {child.type === 'folder' ? (
                                <RenderFolderStructure folder={child} directoryPath={directoryPath} onFileClick={onFileClick} />
                            ) : (
                                <span onClick={() => handleFileClick(folder, child)}>
                                    &nbsp;&nbsp;
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
    const [selectedFilePath, setSelectedFilePath] = useState('');
    const visibleHyperlinks = [
        "Home",
        "Maintainability Scores",
        "Trace Links",
        "Source Code",
    ];
    const EditorHeader = ({ filePath }) => {
        return (
            <div style={styles.header}>
                <div style={{ display: 'flex', alignItems: 'center' }}>
                    <FontAwesomeIcon icon={faChevronRight} style={{ marginRight: '5px' }} />
                    <div>{filePath}</div>
                </div>
            </div>
        );
    };

    const styles = {
        header: {
            padding: "10px",
            backgroundColor: "#123434",
            borderBottom: "1px solid #092635",
        },
        filePath: {
            color: "#fff",
            fontWeight: "bold",
        },
    };
    useEffect(() => {
        fetchFolderStructure();
    }, []);

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

    const handleFileClick = (file) => {
        setSelectedFileType(getFileExtension(file.name));

        setSelectedFilePath(file.path)
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

  
    if (!folderStructure) {
        return null;
    }

    return (
        <div className="vscode-page">
            <Header visibleHyperlinks={visibleHyperlinks} activeLink="Source Code" />

            <div className="row">
                <div className="col-md-3 vscode-file-tree">
                    <RenderFolderStructure folder={folderStructure} directoryPath="GP GUI Base/electron-react-app/src/uploads/teiid_dataset" onFileClick={handleFileClick} />
                </div>
                <div className="col-md-9 vscode-code-editor">
                    {selectedFileContent && (
                        <>
                            <EditorHeader filePath={selectedFilePath} />
                            <CodeEditor content={selectedFileContent} fileType={selectedFileType} />
                        </>
                    )}
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
