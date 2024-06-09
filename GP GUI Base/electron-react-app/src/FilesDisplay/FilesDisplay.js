import React, { useState, useEffect } from "react";
import "../TraceLinks/TraceLinks.css";
import "bootstrap/dist/css/bootstrap.min.css";
import "./FilesDisplay.css";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faWrench, faExclamationCircle, faFlag, faClock, faLayerGroup } from '@fortawesome/free-solid-svg-icons';

import { Header } from "../TopBar/TopBar";
import dictionaries from '../uploads/java_files_dict.json'; 
export function FilesDisplay() {
  const [url, setUrl] = useState('');
  const [items, setItems] = useState({});
  const [details, setDetails] = useState(null);
  const [itemsReturned, setItemsReturned] = useState(false);
 

  const extractTeiidFromUrl = (url) => {
    const match = url.match(/TEIID-\d+/);
    return match ? match[0] : null;
  };

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

      const topFiveResponse = await fetch('http://localhost:5000/get-top-five', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ summary_description: data.summary + " " + data.description })
      });

      if (!topFiveResponse.ok) {
        throw new Error('Failed to fetch top five files');
      }

      const topFiveData = await topFiveResponse.json();
      const newItems = topFiveData.top_five_dict || {};
     
      setItems(newItems);
      console.log('newItems: ', newItems);
      setItemsReturned(true);
    } catch (error) {
      console.error('Error fetching the URL:', error);
    }
  };

  const visibleHyperlinks = [
    "Home",
    "About Us",
    "FilesDisplay",
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
          return "linear-gradient(to top, #004d00, #66b266)";
        case "Enhancement":
          return "linear-gradient(to top, #0066FF, #66CCFF)";
        default:
          return "linear-gradient(to top, #092635, #1c4b60, #6fa3c7)";
      }
    } else {
      return "linear-gradient(to top, #092635, #1c4b60, #6fa3c7)";
    }
  };

  useEffect(() => {
    if (items.length > 0) {
      console.log('Items updated:', items);
    }
  }, [items]);

  return (
    <div className="App">
      <Header visibleHyperlinks={visibleHyperlinks} activeLink="FilesDisplay" />
      <div className="card-body talabat">
        <h5 className="card-title text-center enterurl">Please Enter The Issue URL</h5>
        <div className="form-group d-flex justify-content-center mt-4">
          <input
            type="text"
            className="form-control url-input searchee"
            id="urlInput"
            value={url}
            onChange={(e) => setUrl(e.target.value)}
            placeholder="Enter URL"
          />
          <button className="btn btn-primary buttonurl" onClick={handleUrlSubmit}>Submit</button>
        </div>
      </div>

      {details && (
        <div className="row mt-3">
          <div className="col-1"></div>
          <div className="col-3">
            <div className="card big-circle-card">
              <div className="card-body">
                <h3 className="card-title">{extractTeiidFromUrl(url)}</h3>
                <h5 className="card-text">{details.summary}</h5>
                <p className="card-text">{details.description}</p>
                <div className="icon-container">
                  <div className="icon-text-container mt-2">
                    <FontAwesomeIcon icon={faWrench} fixedWidth />
                    <span>Fix For: {details.details.fixfor}</span>
                  </div>
                  <div className="icon-text-container mt-2">
                    <FontAwesomeIcon icon={faExclamationCircle} fixedWidth />
                    <span>Priority: {details.details.priority}</span>
                  </div>
                  <div className="icon-text-container mt-2">
                    <FontAwesomeIcon icon={faFlag} fixedWidth />
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
                    <FontAwesomeIcon icon={faLayerGroup} fixedWidth />
                    <span>Versions: {details.details.versions}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div className="col-8">
            <div className="circle-container">
              <div className="big-circle" style={{ boxShadow: `0 4px 8px ${getShadowColor()}, inset 0 -4px 8px ${getShadowColor()}`, background: `${getBackgroundColor()}` }}>
                <div className="big-circle-details text-center">
                  <h3>{details.details.type} Case</h3>
                  <p>{details.summary}</p>
                </div>
              </div>
              {itemsReturned &&
    Object.entries(items).map(([name, score], index) => {
       
        const transformedName = encodeURIComponent(dictionaries[name].replace(/\\/g, "/"));

        return (
            <a
                key={index}
                href={`http://localhost:3000/#/ViewSource/${transformedName}`}
                className={`text-center small-circle small-circle-${index + 1}`}
                style={{
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center"
                }}
            >
                <p className="circle-name" style={{ position: "absolute" }}>{name.slice(0, -5)}</p>
                <p className="circle-name mt-5" style={{ position: "absolute" }} data-score={score}>{score.toFixed(2)}%</p>
            </a>
        );
    })
}
            </div>
          </div>
        </div>
      )}
    </div>
  );

}
