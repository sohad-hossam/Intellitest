import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { Header } from "../TopBar/TopBar";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faFolder, faFileCsv, faFileAlt, faFileCode, faChevronRight, faSearch } from '@fortawesome/free-solid-svg-icons';
import { CodeEditor } from "../CodeEditor/CodeEditor";
import './ViewSource.css';

const RenderFolderStructure = ({ folder, directoryPath, onFileClick, searchQuery }) => {
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
        const filePath = `${directoryPath}/${file.name}`;
        onFileClick({ ...file, path: filePath });
    };

    const matchesSearchQuery = (name) => {
        return name.toLowerCase().includes(searchQuery.toLowerCase());
    };

    const renderFolderChildren = (children, currentPath) => {
        return children.map((child) => (
            <div key={child.name}>
                {child.type === 'folder' ? (
                    <RenderFolderStructure
                        folder={child}
                        directoryPath={`${currentPath}/${child.name}`}
                        onFileClick={onFileClick}
                        searchQuery={searchQuery}
                    />
                ) : (
                    matchesSearchQuery(child.name) && (
                        <span onClick={() => handleFileClick(folder, child)}>
                            &nbsp;&nbsp;
                            {getFileIcon(child.name, child.type)}
                            &nbsp;&nbsp;
                            {child.name}
                        </span>
                    )
                )}
            </div>
        ));
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
                    {renderFolderChildren(folder.children, directoryPath)}
                </div>
            )}
        </div>
    );
};

function ViewSource() {
    const navigate = useNavigate();
    const { file } = useParams();

    const [folderStructure, setFolderStructure] = useState(null);
    const [selectedFileContent, setSelectedFileContent] = useState('');
    const [selectedFileType, setSelectedFileType] = useState('');
    const [selectedFilePath, setSelectedFilePath] = useState('');
    const [searchQuery, setSearchQuery] = useState('');
    const visibleHyperlinks = [
        "Home",
        "Maintainability Scores",
        "Trace Links",
        "Source Code",
        "FilesDisplay"
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
        searchContainer: {
            position: 'relative',
            marginBottom: '20px',
        },
        searchIcon: {
            position: 'absolute',
            top: '50%',
            left: '10px',
            transform: 'translateY(-50%)',
            color: 'white',
        },
        searchInput: {
            padding: "5px 10px 5px 30px",
            width: "100%",
            borderRadius: "10px",
            backgroundColor: "#123434",
            color: "white",
            border: "3px solid white",
        }
    };

    useEffect(() => {
        fetchFolderStructure();

        if (file) {
            fetchFileContent(file);
        }
    }, [file]);

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

    const fetchFileContent = (filePath) => {
        setSelectedFileType(getFileExtension(filePath));
        setSelectedFilePath(filePath);
        fetch(`http://localhost:5000/get-file-content?file_path=${filePath}`)
            .then((response) => response.text())
            .then((data) => {
                setSelectedFileContent(data);
            })
            .catch((error) => {
                console.error("Error fetching file content:", error);
            });
    };

    const mockJavaCode = `/**
     * Constructor for ProcedurePlan.
     */
    public ProcedurePlan(Program originalProgram) {
        this.originalProgram = originalProgram;
        createVariableContext();
    }
`;
    const ErrorScript = () => {
        return (
            <div className="error-script">
                <pre>
                    <code>
                        {mockJavaCode.split('\n').map((line, index) => (
                            <div key={index} className={index === 3 ? 'highlighted-line' : ''}>
                                {index + 1}: {line}
                            </div>
                        ))}
                    </code>
                </pre>
            </div>
        );
    };

    const handleFileClick = (file) => {
        const filePath = file.path;
        navigate(`/ViewSource/${encodeURIComponent(filePath)}`);
        fetchFileContent(filePath);
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
                <div className="col-md-1"></div>
                <div className="col-md-3 vscode-file-tree">
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
                    <RenderFolderStructure
                        folder={folderStructure}
                        directoryPath="GP GUI Base/electron-react-app/src/uploads"
                        onFileClick={handleFileClick}
                        searchQuery={searchQuery}
                    />
                </div>
                <div className="col-md-7 vscode-code-editor">
                    {selectedFileContent && (
                        <>
                            <EditorHeader filePath={selectedFilePath} />
                            <CodeEditor content={selectedFileContent} fileType={selectedFileType} />
                        </>
                    )}
                </div>
                <div className="col-md-1"></div>
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
            <div className="row">
                <div className="col-md-1"></div>
                <div className="col-md-10 error-separator"></div>
                <div className="col-md-1"></div>
            </div>
            <div className="row">
                <div className="col-md-1"></div>
                <div className="col-md-10 cc">
                    <ErrorScript />
                </div>
                <div className="col-md-1"></div>
            </div>
        </div>
    );
}

export { ViewSource };
