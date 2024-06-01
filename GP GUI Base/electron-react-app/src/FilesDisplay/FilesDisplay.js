/* FilesDisplay.js */

import React, { useState, useEffect } from "react";
import "../TraceLinks/TraceLinks.css";
import "bootstrap/dist/css/bootstrap.min.css";
import "./FilesDisplay.css";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faWrench, faExclamationCircle, faFlag, faClock, faLayerGroup } from '@fortawesome/free-solid-svg-icons';

import { Header } from "../TopBar/TopBar";

export function FilesDisplay() {
  const [url, setUrl] = useState('');
  const [items, setItems] = useState([]);
  const [details, setDetails] = useState(null);
 
  const useCaseDictionary = {
    "TEIID-5928": [
      { name: "java.1", score: Math.floor(Math.random() * 100) }, 
      { name: "java.2", score: Math.floor(Math.random() * 100) },
      { name: "java.3", score: Math.floor(Math.random() * 100) },
      { name: "java.4", score: Math.floor(Math.random() * 100) },
      { name: "java.5", score: Math.floor(Math.random() * 100) }
    ],
    "TEIID-6025": [
      { name: "java.6", score: Math.floor(Math.random() * 100) }, 
      { name: "java.7", score: Math.floor(Math.random() * 100) },
      { name: "java.8", score: Math.floor(Math.random() * 100) },
      { name: "java.9", score: Math.floor(Math.random() * 100) },
      { name: "java.10", score: Math.floor(Math.random() * 100) }
    ],
    "TEIID-5936": [
      { name: "java.11", score: Math.floor(Math.random() * 100) }, 
      { name: "java.12", score: Math.floor(Math.random() * 100) },
      { name: "java.13", score: Math.floor(Math.random() * 100) },
      { name: "java.14", score: Math.floor(Math.random() * 100) },
      { name: "java.15", score: Math.floor(Math.random() * 100) }
    ],
    "TEIID-5949": [
      { name: "java.16", score: Math.floor(Math.random() * 100) }, 
      { name: "java.17", score: Math.floor(Math.random() * 100) },
      { name: "java.18", score: Math.floor(Math.random() * 100) },
      { name: "java.19", score: Math.floor(Math.random() * 100) },
      { name: "java.20", score: Math.floor(Math.random() * 100) }
    ],
    "TEIID-5975": [
      { name: "java.21", score: Math.floor(Math.random() * 100) }, 
      { name: "java.22", score: Math.floor(Math.random() * 100) },
      { name: "java.23", score: Math.floor(Math.random() * 100) },
      { name: "java.24", score: Math.floor(Math.random() * 100) },
      { name: "java.25", score: Math.floor(Math.random() * 100) }
    ],
    "TEIID-5985": [
      { name: "java.26", score: Math.floor(Math.random() * 100) }, 
      { name: "java.27", score: Math.floor(Math.random() * 100) },
      { name: "java.28", score: Math.floor(Math.random() * 100) },
      { name: "java.29", score: Math.floor(Math.random() * 100) },
      { name: "java.30", score: Math.floor(Math.random() * 100) }
    ]
  };
  
  

  const extractTeiidFromUrl = (url) => {
    const match = url.match(/TEIID-\d+/);
    return match ? match[0] : null;
  };

  const [clickedCircle, setClickedCircle] = useState(null);

  const handleClick = (index) => {
    if (clickedCircle === index) {
      setClickedCircle(null);
    } else {
      setClickedCircle(index);
    }
  };

  useEffect(() => {
    const revealText = document.querySelector('.reveal-text');
    if (revealText) {
      revealText.classList.add('animate-reveal-text');
    }
  }, [details]);
  const handleUrlSubmit = async () => {
    try {
      const teiid = extractTeiidFromUrl(url);
      if (!teiid) {
        throw new Error('Invalid URL');
      }

      const response = await fetch('http://localhost:5000/fetch-details', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ url })
      });

      if (!response.ok) {
        throw new Error('Failed to fetch the URL');
      }

      const data = await response.json();
      console.log('Fetched data:', data);
      setDetails(data);

      const newItems = useCaseDictionary[teiid] || []; 
      setItems(newItems);
      console.log(items);
    } catch (error) {
      console.error('Error fetching the URL:', error);
    }
  };

  const visibleHyperlinks = [
    "Home",
    "About Us",
    "Maintainability Scores",
    "Trace Links"
  ];

const getShadowColor = () => {
  if (details && details.details && details.details.type) {
    const type = details.details.type;
    switch (type) {
      case "Bug":
        return "rgba(255, 0, 0, 0.5)";
      case "Quality Risk":
        return "rgba(255, 140, 0, 0.8)"; 
      case "Feature Request":
        return "rgba(0, 128, 0, 0.8)"; 
      case "Enhancement":
        return "rgba(0, 0, 255, 0.5)"; 
      default:
        return "rgba(0, 0, 0, 0.2)"; 
    }
  } else {
    return "rgba(0, 0, 0, 0.2)"; 
}
};
const getBackgroundColor = () => {
  if (details && details.details && details.details.type) {
    const type = details.details.type;
    switch (type) {
      case "Quality Risk":
        return "linear-gradient(to top, #FF8E00, #FFD600)";
      case "Bug":
        return "linear-gradient(to top, #FF0000, #FF3333)"; 
      case "Feature Request":
        return "linear-gradient(to top, #008000, #66FF66)"; 
      case "Enhancement":
        return "linear-gradient(to top, #0066FF, #66CCFF)"; 
      default:
        return "linear-gradient(to top, #092635, #1c4b60, #6fa3c7)"; 
    }
  } else {
    return "linear-gradient(to top, #092635, #1c4b60, #6fa3c7)"; 
  }
};

  return (
    <div className="App">
      <Header visibleHyperlinks={visibleHyperlinks} activeLink="Trace Links" />
 
          <div className="card">
            <div className="card-body">
              <h5 className="card-title">Enter URL</h5>
              <div className="form-group text-center">
                <input 
                  type="text" 
                  className="form-control" 
                  id="urlInput" 
                  value={url} 
                  onChange={(e) => setUrl(e.target.value)} 
                  placeholder="Enter URL" 
                />
                <button className="btn btn-primary mt-3" onClick={handleUrlSubmit}>Submit</button>
              </div>
            </div>
            </div>
          <div className="row">
            <div className="col-1"></div>
            <div className="col-3">
            <div className="card big-circle-card">
            {details && (   <div className="card-body">
        <h3 className="card-title">{extractTeiidFromUrl(url)}</h3>
        <h5 className="card-text">{details.summary}</h5>
        <p className="card-text">{details.description}</p>
        <div className="icon-container">
          <div className="icon-text-container mt-2">
            <FontAwesomeIcon icon={faWrench}fixedWidth  />
            <span>Fix For: {details.details.fixfor}</span>
          </div>
          <div className="icon-text-container mt-2">
            <FontAwesomeIcon icon={faExclamationCircle} fixedWidth  />
            <span>Priority: {details.details.priority}</span>
          </div>
          <div className="icon-text-container mt-2">
            <FontAwesomeIcon icon={faFlag}fixedWidth  />
            <span>Resolution: {details.details.resolution}</span>
          </div>
          <div className="icon-text-container mt-2">
            <FontAwesomeIcon icon={faClock} fixedWidth />
            <span>Sprint: {details.details.sprint}</span>
          </div>
          <div className="icon-text-container mt-2">
            <FontAwesomeIcon icon={faLayerGroup} fixedWidth />
            <span>Type: {details.details.type}</span>
          </div>
          <div className="icon-text-container mt-2">
            <FontAwesomeIcon icon={faLayerGroup}fixedWidth  />
            <span>Versions: {details.details.versions}</span>
          </div>
        </div>
      </div>)}
    </div>
            </div>
            <div className="col-8">
            <div className="circle-container">
                <div className="big-circle" style={{ boxShadow: `0 4px 8px ${getShadowColor()}, inset 0 -4px 8px ${getShadowColor()}`, background: `${getBackgroundColor()}` }}>
            
            {details && (
              <div>
            <div className="big-circle-details text-center">
              <h3>{details && details.details && details.details.type} Case</h3>
            </div>
            <div className="big-circle-details text-center">
              <p>{details && details.summary}</p>
            </div>
            </div>
          )}

                </div>
                {Object.keys(useCaseDictionary).map((issueId) =>
  useCaseDictionary[issueId].map((item, index) => (
    <div
      key={`${issueId}-${index}`}
      className={`text-center small-circle small-circle-${index + 1} ${
        clickedCircle === index ? "clicked" : ""
      }`}
      onClick={() => handleClick(index)}
      style={{
       
        display: "flex",
        alignItems: "center",
        justifyContent: "center"
      }}
    >
      <h5 className="circle-name" style={{ position: "absolute" }}>{item.name}</h5>
      <h5 className="circle-score" style={{ position: "absolute" }} data-score={item.score}>{item.score}%</h5>
    </div>
  ))
)}
          </div>
              </div>
          </div>
        
        </div>

  );
}
