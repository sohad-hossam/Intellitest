import React, { useState, useEffect } from "react";
import { Header } from "../TopBar/TopBar";
import { PageTitle } from "../PageTitle/PageTitle";
function ViewSource() {
  const [folderStructure, setFolderStructure] = useState(null);
  const visibleHyperlinks = [
    "Home",
    "About Us",
    "Import Project",
    "Source Code",
  ];
  useEffect(() => {
   
    fetch("http://localhost:5000/get-folder-structure?directory_path=GP GUI Base/electron-react-app/src/uploads/teiid_dataset")
      .then((response) => response.json())
      .then((data) => {
        setFolderStructure(data);
        console.log(data);
      })
      .catch((error) => {
        console.error("Error fetching folder structure:", error);
      });
  }, []);
  

  
  const renderFolderStructure = (folder) => {
    return (
      <ul key={folder.name}>
        <li>{folder.name}</li>
        {folder.children &&
          folder.children.map((child) => (
            <li key={child.name}>
              {child.type === "folder"
                ? renderFolderStructure(child)
                : child.name}
            </li>
          ))}
      </ul>
    );
  };

  return (
    <div className="container">
      <Header visibleHyperlinks={visibleHyperlinks} activeLink="Source Code" />
      <PageTitle title={ folderStructure? folderStructure.name.replace(/_/g, ' ').toUpperCase(): ''} activeLink="Source Code"/>
      <h1>View Source</h1>
      {folderStructure ? (
        renderFolderStructure(folderStructure)
      ) : (
        <p>Loading folder structure...</p>
      )}
    </div>
  );
  
}

export { ViewSource };
