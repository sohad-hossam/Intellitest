import React, { useState, useEffect } from "react";

function ViewSource() {
  const [folderStructure, setFolderStructure] = useState(null);

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
