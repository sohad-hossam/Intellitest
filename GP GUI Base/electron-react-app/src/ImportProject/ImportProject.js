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
    }, 10000); // Adjust interval duration to 10 seconds

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
  return (
    <div className="ImportProject">
      <Header
        visibleHyperlinks={visibleHyperlinks}
        activeLink="Import Project"
      />
      <div className="LoadingText">
        <PageTitle title={"EL PROJECT ELSOHADY"} activeLink="Import Project" />
        <svg viewBox="0 0 1320 300">
          <text x="50%" y="50%" dy=".35em" text-anchor="middle">
            Intellitest
          </text>
        </svg>
      </div>
      <div className="LoadingBar">
        <ProcessingProject />
        <ProgressBar />
        <ThisMighTakeFew />
      </div>
    </div>
  );
}
