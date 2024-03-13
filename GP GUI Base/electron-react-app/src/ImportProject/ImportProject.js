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
    <div class="progress-container">
      <div
        class="progress-bar"
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
      <PageTitle title={"EL PROJECT ELSOHADY"} activeLink="Import Project" />
      <ProcessingProject />
      <ProgressBar />
      <ThisMighTakeFew />
    </div>
  );
}
