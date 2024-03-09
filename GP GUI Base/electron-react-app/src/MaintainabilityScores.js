import React, { useEffect, useState } from 'react';
import './MaintainabilityScores.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Header } from './TopBar/TopBar';
import { PageTitle } from './PageTitle/PageTitle';

function ProgressBar({ progress }) {
  return (
    <div className="progress-container">
      <div
        className="progress-bar"
        style={{ 
          width: `${progress}%`, 
          backgroundColor: '#092635'
        }}
      ></div>
    </div>
  );
}

function DisplayScores({ label, score }) {
  return (
    <div className="DisplayScores">
      <div className="ScoreLabel">{label}</div>
      <div class="my-2"></div>
      <ProgressBar progress={score} />
    </div>
  );
}

export function MaintainabilityScore() {
  const visibleHyperlinks = ['Home', 'About Us', 'Maintainability Scores', 'Trace Links'];
  const scores = {
    label: 30,
    label2: 80,
    label3: 50,
  };

  return (
    <div className="App">
      <Header visibleHyperlinks={visibleHyperlinks} activeLink="Maintainability Scores" />
      <PageTitle title={"Maintainability Scores"} />
      {Object.keys(scores).map((label, index) => (
        <DisplayScores key={index} label={label} score={scores[label]} />
      ))}
    </div>
  );
}
