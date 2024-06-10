import React from "react";
import { HashRouter, Route, Routes } from "react-router-dom";
import { GlobalProvider } from "./GlobalState"; // Import the GlobalProvider component
import "bootstrap/dist/css/bootstrap.min.css";
import "./App.css";
import { ImportProject } from "./ImportProject/ImportProject.js";
import { MaintainabilityScore } from "./MantainabilityScores/MaintainabilityScores.js";
import { HomePage } from "./HomePage/HomePage.js";
import { AboutUs } from "./AboutUs";
import { TraceLinks } from "./TraceLinks/TraceLinks.js";
import { ViewSource } from "./ViewSource/ViewSource.js";
import { CodeEditor } from "./CodeEditor/CodeEditor.js";
import { FilesDisplay } from "./FilesDisplay/FilesDisplay.js";

function App() {
  return (
    <HashRouter basename="/">
      
      <GlobalProvider>
        <div className="App">
          <Routes>
            <Route path="/" exact element={<HomePage />} />
            <Route path="/AboutUs" element={<AboutUs />} />
            <Route
              path="/MaintainabilityScore"
              element={<MaintainabilityScore />}
            />
            <Route path="/ImportProject" element={<ImportProject />} />
            <Route path="/TraceLinks" element={<TraceLinks />} />
            <Route path="/ViewSource" element={<ViewSource />} />
            <Route path="/ViewSource/:file" element={<ViewSource />} />
            <Route path="/CodeEditor" element={<CodeEditor />} />
            <Route path="/FilesDisplay" element={<FilesDisplay />} />
          </Routes>
        </div>
      </GlobalProvider>
    </HashRouter>
  );
}

export default App;
