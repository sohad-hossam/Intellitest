import React, { useEffect, useState } from "react";
import "./ImportProject.css";
import "bootstrap/dist/css/bootstrap.min.css";
import { Header } from "../TopBar/TopBar";

import { ViewSource } from "../ViewSource/ViewSource";

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
  const visibleHyperlinks = ["Home", "Import Project", "Proceed With"];
  const [progress, setProgress] = useState(0);
  const [redirectToViewSource, setRedirectToViewSource] = useState(false);
  const [folderStructure, setFolderStructure] = useState(null); // State to store folder structure

  useEffect(() => {
    const timer = setTimeout(() => {
      if (progress < 100) {
        setProgress(progress + 20);
      } else {
        setRedirectToViewSource(true);
      }
    }, 3000); 
    
    
  }, [progress]);
  useEffect(() => {
   
    if (redirectToViewSource) {
      window.location.href = "http://localhost:3000/#/ViewSource";
    }
  }, [redirectToViewSource]);

  return (
    <div className="ImportProject">
      <Header
        visibleHyperlinks={visibleHyperlinks}
        activeLink="Import Project"
      />
      <div className="LoadingText">
     
        <svg viewBox="0 0 1320 300">
          <text x="50%" y="50%" dy=".35em" textAnchor="middle">
            Intellitest
          </text>
        </svg>
      </div>
      <div className="LoadingBar">
        <ProcessingProject />
        <ProgressBar progress={progress} />
        <ThisMighTakeFew />

      </div>
    
    </div>
  );
}
