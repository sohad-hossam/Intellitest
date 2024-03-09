import React, { useEffect, useState } from 'react';
import './MaintainabilityScores.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Header } from './TopBar/TopBar';
import { PageTitle } from './PageTitle/PageTitle';

function ProgressBar({ score }) {
  return (
    <div className="progress-container">
      <div
        className="progress-bar"
        style={{
          width: `${score}%`,
          backgroundColor: '#092635'
        }}
      ></div>
    </div>
  );
}

export function MaintainabilityScore() {
  const visibleHyperlinks = ['Home', 'About Us', 'Maintainability Scores', 'Trace Links'];

  const scores = {
    label1: 20,
    label2: 80,
    label3: 40
  };

  return (
    <div className="App">
      <Header visibleHyperlinks={visibleHyperlinks} activeLink="Maintainability Scores" />
      <PageTitle title={"Maintainability Scores"} />
     </div>
  );
}