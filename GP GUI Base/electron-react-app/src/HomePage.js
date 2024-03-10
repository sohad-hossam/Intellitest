import React, { useEffect, useState } from 'react';
import './HomePage.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Header } from './TopBar/TopBar';
import { PageTitle } from './PageTitle/PageTitle';

function Message() {
    return (
      <div className="Message">
       Uncovering Excellence:<br></br> Where Quality Meets Precision.
      </div>
    );
  }

function ButtonsMoving(){
    return (
        <div className="container mt-5">
          <div className="row mt-5">
            <div className="col-md-2 mt-5"></div>
            <div className="col-md-8 text-center mt-5">
              <a href="./ImportProject.js" className="btnImport  m-5">Import Project</a>
              <a href="./ProceedWith" className="btnProceed m-5">Proceed with</a>
            </div>
            <div className="col-md-2"></div>
          </div>
        </div>
      );
  }

export function HomePage() {
    const visibleHyperlinks = ['Home', 'About Us', 'Import Project','Proceed With'];
    return (
      <div className="App">
      <row>  
        <Header visibleHyperlinks={visibleHyperlinks} activeLink="Home" />
      </row>
      <row >
        <PageTitle title={"Welcome to Intellitest"} />
      </row>
      
      <row>
        <Message />
      </row>
      
      <row   class="mt-5">
        <div class="mt-5"></div>
      </row>
      
      <row   class="mt-5">
        <div class="mt-5"></div>
      </row>
      
      <row   class="mt-5">
        <div class="mt-5"></div>
      </row>
      <row   class="mt-5">
        <div class="mt-5"></div>
      </row>
      
      <row  class="mt-5">
        <ButtonsMoving />
      </row>
      </div>
    );
  }