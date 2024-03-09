import React from 'react';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
<link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet"></link>

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
                    <a href="/page1" class="hyperpages mt-4">Home</a>
                </div>
                <div className="col">
                    <a href="/page2" class="hyperpages mt-4">About Us</a>
                </div>
                <div className="col">
                    <a href="/page3" class="hyperpages mt-4">Import Project</a>
                </div>
                <div className="col">
                    <a href="/page4" class=" hyperpages mt-4">Proceed with</a>
                </div>
            </div>
        </div>
  
    </div>
</header>
);
}
function App() {
  return (
      <div className="App">
          <Header />
      </div>
  );
}

export default App;