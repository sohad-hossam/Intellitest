import React, { useEffect, useState } from "react";
import "./ImportProject.css";
import "bootstrap/dist/css/bootstrap.min.css";
import { Header } from "../TopBar/TopBar";
import { PageTitle } from "../PageTitle/PageTitle";

function ProcessingProject() {
  return <div className="ProcessingProject">Processing Project . . .</div>;
}

function ProgressBar() {
  const [progress, setProgress] = useState(0);

  useEffect(() => {
    const interval = setInterval(() => {
      if (progress >= 100) {
        clearInterval(interval);
      } else {
        setProgress((prevProgress) => prevProgress + 10);
      }
    }, 10000); 

    return () => clearInterval(interval);
  }, [progress]);

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
  const [progress, setProgress] = useState(0);
  const [loading, setLoading] = useState(false);
  const [fileUploaded, setFileUploaded] = useState(false); 

  const handleFileChange = (event) => {
    setSelectedFile(event.target.files[0]);
    setFileUploaded(true); 
  };

  const uploadFile = async () => {
    setLoading(true);
    const formData = new FormData();
    formData.append("file", selectedFile);
  
    try {
      const response = await fetch("http://localhost:5000/upload-folder", {
        method: "POST",
        body: formData,
      });
      if (!response.ok) {
        throw new Error(`Upload failed with status ${response.status}`);
      }
      setLoading(false);
    } catch (error) {
      console.error("Error uploading file:", error);
      setLoading(false);
    }
  };
  
  return (
    <div className="ImportProject">
      <Header visibleHyperlinks={visibleHyperlinks} activeLink="Import Project" />
      <div className="LoadingText">
        <PageTitle title={"EL PROJECT ELSOHADY"} activeLink="Import Project" />
        <svg viewBox="0 0 1320 300">
          <text x="50%" y="50%" dy=".35em" text-anchor="middle">
            Intellitest
          </text>
        </svg>
      </div>
      <div className="LoadingBar">
        {fileUploaded && <ProcessingProject />} 
        {fileUploaded && <ProgressBar />}
        {fileUploaded && <ThisMighTakeFew />}
        <div className="file-upload">
          <input type="file" onChange={handleFileChange} />
          <button onClick={uploadFile}>Upload</button>
        </div>
        {loading && <p>Uploading...</p>}
      </div>
    </div>
  );
}