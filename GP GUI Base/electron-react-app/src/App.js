import "bootstrap/dist/css/bootstrap.min.css";
import "./App.css";
import { ImportProject } from "./ImportProject";
import { MaintainabilityScore } from "./MaintainabilityScores";
import { HomePage } from "./HomePage";
import { AboutUs } from "./AboutUs";
import { TraceLinks } from "./TraceLinks";
import { HashRouter, Route, Routes } from "react-router-dom";

function App() {
  return (
    <HashRouter basename="/">
      <div className="App">
        <Routes>
          <Route path="/" exact element={<HomePage />} />
          <Route path="/AboutUs" component={AboutUs} />
          <Route
            path="/MaintainabilityScore"
            component={MaintainabilityScore}
          />
          <Route path="/ImportProject" element={<TraceLinks />} />
          <Route path="/TraceLinks" element={<TraceLinks />} />
        </Routes>
      </div>
    </HashRouter>
  );
}

export default App;
