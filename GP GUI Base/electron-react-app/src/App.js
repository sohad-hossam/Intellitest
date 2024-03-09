import React, { useEffect, useState } from 'react';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';

function Header() {
  return (
    <header className="header">
      <div className="row align-items-center">
        <div className="col-3">
          <img src={require("./assets/searching.png")} alt="Icon" className="header-icon" />
        </div>
        <div className="col-6">
          <div className="row">
            <div className="col">
              <a href="/page1" className="hyperpages mt-4">Home</a>
            </div>
            <div className="col">
              <a href="/page2" className="hyperpages mt-4">About Us</a>
            </div>
            <div className="col">
              <a href="/page3" className="hyperpages mt-4">Import Project</a>
            </div>
            <div className="col">
              <a href="/page4" className=" hyperpages mt-4">Proceed with</a>
            </div>
          </div>
        </div>
      </div>
    </header>
  );
}

function PageTitle() {
  return (
    <div className="PageTitle">
      EL PROJECT ELSOHADY
    </div>
  );
}

function ProcessingProject() {
  return (
    <div className="ProcessingProject">
      Processing Project . . .
    </div>
  );
}

function ProgressBar() {
  const [progress, setProgress] = useState(0);

  useEffect(() => {
    const interval = setInterval(() => {
      if (progress >= 100) {
        clearInterval(interval);
      } else {
        setProgress(prevProgress => prevProgress + 10);
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
          backgroundColor: '#092635' // Set the background color inline
        }}
      ></div>
    </div>
  );
}

function ThisMighTakeFew() {
  return (
    <div className="ThisMighTakeFew">
      This Might Take Few Minutes . . .
    </div>
  );
}

function App() {
  return (
    <div className="App">
      <Header />
      <PageTitle />
      <ProcessingProject />
      <ProgressBar />
      <ThisMighTakeFew />
    </div>
  );
}

export default App;
