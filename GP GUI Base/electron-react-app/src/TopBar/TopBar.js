import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import './TopBar.css';


export function Header() {
  return (
    <header className="header">
      <div className="row align-items-center">
        <div className="col-3">
          <img src={require("../assets/searching.png")} alt="Icon" />
        </div>
        <div className="col-6">
          <div className="row">
            <div className="col">
              <a href="/index.js" className="hyperpages mt-4">Home</a>
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
