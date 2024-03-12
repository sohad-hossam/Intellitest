//import React, { useEffect, useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
//import { Header } from './TopBar/TopBar';
//import { PageTitle } from './PageTitle/PageTitle';
import { ImportProject } from './ImportProject';
import { MaintainabilityScore } from './MaintainabilityScores';
import { HomePage } from './HomePage';
import { AboutUs } from './AboutUs';
function App() {
  return (
     <div className="App">
      <MaintainabilityScore/>
      </div>
    );
}

export default App;
