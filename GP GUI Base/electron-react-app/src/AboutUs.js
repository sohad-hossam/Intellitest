import React, { useEffect, useState } from 'react';
import './HomePage.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Header } from './TopBar/TopBar';
import { PageTitle } from './PageTitle/PageTitle';

function Message() {
    return (
      <div className="Message1 mt-5">
            The Intellitest Team<br></br>
    Yasmin Hashem<br></br>
    Yasmeen Zaki<br></br>
    Bassant Hisham<br></br>
    Sohad Hossam <br></br>
    software Testing Platfrom with a Machine Learning Approach 
      </div>
    );
  }

export function AboutUs() {
    const visibleHyperlinks = ['Home', 'About Us', 'Import Project','Proceed With'];
    return (
      <div className="App">
        <Header visibleHyperlinks={visibleHyperlinks} activeLink="About Us" />
        <div class="mt-5"></div>
        <PageTitle title={"Welcome to Intellitest"} />
        <div class="mt-5"></div>
        <div class="mt-5"></div>
        <Message />
     
      </div>
    );
  }