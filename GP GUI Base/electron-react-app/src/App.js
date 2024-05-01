import "bootstrap/dist/css/bootstrap.min.css";
import "./App.css";
import { ImportProject } from "./ImportProject/ImportProject.js";
import { MaintainabilityScore } from "./MantainabilityScores/MaintainabilityScores.js";
import { HomePage } from "./HomePage/HomePage.js";
import { AboutUs } from "./AboutUs";
import { TraceLinks } from "./TraceLinks/TraceLinks.js";
import { HashRouter, Route, Routes } from "react-router-dom";

function App() {
  return (
    <HashRouter basename="/">
      <div className="App">
        <Routes>
          <Route path="/" exact element={<HomePage />} />
          <Route path="/AboutUs" element={<AboutUs />} />
          <Route
            path="/MaintainabilityScore"
            element={<MaintainabilityScore />}
          />
          <Route path="/ImportProject" element={<MaintainabilityScore />} />
          <Route path="/TraceLinks" element={<TraceLinks />} />
        </Routes>
      </div>
    </HashRouter>
  );
}

export default App;
