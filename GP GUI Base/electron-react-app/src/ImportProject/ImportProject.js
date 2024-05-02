import React, { useEffect, useState } from "react";
import "./ImportProject.css";
import "bootstrap/dist/css/bootstrap.min.css";
import { Header } from "../TopBar/TopBar";
import { PageTitle } from "../PageTitle/PageTitle";
import { Link } from "react-router-dom";
function ProcessingProject() {
  return <div className="ProcessingProject">Processing Project . . .</div>;
}

function ProgressBar({ progress }) {
  return (
    <div className="progress-container">
      <div
        className="progress-bar"
        style={{
          width: `${progress}%`,
          backgroundColor: "#092635",
        }}
      ></div>
    </div>
  );
}

function ThisMighTakeFew() {
  return (
    <div className="ThisMighTakeFew">This Might Take Few Minutes . . .</div>
  );
}

export function ImportProject() {
  const visibleHyperlinks = [
    "Home",
    "About Us",
    "Import Project",
    "Proceed With",
  ];
  const [selectedFile, setSelectedFile] = useState(null);
  const [fileUploaded, setFileUploaded] = useState(false);
  const [fileFullyUploaded, setfileFullyUploaded] = useState(false);
  const [progress, setProgress] = useState(0);

  const handleFileChange = (event) => {
    setSelectedFile(event.target.files[0]);
    setFileUploaded(true); 
    setProgress(20); 
  };

  const uploadFile = async () => {
    const formData = new FormData();
    formData.append("file", selectedFile);
  
    // Extract the directory path from the temporary file path
    const folderPath = selectedFile && selectedFile.webkitRelativePath ? selectedFile.webkitRelativePath.split('/').slice(0, -1).join('/') : '';
  
    // Append the folderPath to the FormData
    formData.append("folderPath", folderPath);
  
    try {
      const response = await fetch("http://localhost:5000/upload-folder", {
        method: "POST",
        body: formData,
      });
  
      if (!response.ok) {
        throw new Error(`Upload failed with status ${response.status}`);
      }
  
      setProgress(100);
      setFileUploaded(false);
      setfileFullyUploaded(true);
    } catch (error) {
      console.error("Error uploading file:", error);
    }
  };
  

  return (
    <div className="ImportProject">
      <Header visibleHyperlinks={visibleHyperlinks} activeLink="Import Project" />
      <div className="LoadingText">
        <PageTitle title={"EL PROJECT ELSOHADY"} activeLink="Import Project" />
        <svg viewBox="0 0 1320 300">
          <text x="50%" y="50%" dy=".35em" textAnchor="middle">
            Intellitest
          </text>
        </svg>
      </div>
      <div className="LoadingBar">
        {fileUploaded && <ProcessingProject />}
        {<ProgressBar progress={progress} />}
        {fileUploaded && <ThisMighTakeFew />}
        {fileFullyUploaded ? (
        <Link to={"/ViewSource"} className="btnProceed m-5">
        Proceed with
      </Link>
        ) : (
          <div className="file-upload">
            <label htmlFor="file-upload">Choose File</label>
            <input type="file" id="file-upload" onChange={handleFileChange} />
            <button onClick={uploadFile}>Upload</button>
          </div>
        )}
      </div>
    </div>
  );
  
}
