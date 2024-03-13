import React, { useState } from 'react';
import './TraceLinks.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Header } from './TopBar/TopBar';
import { PageTitle } from './PageTitle/PageTitle';

function DropDowns() {
    const useCaseOptions = ['Project 1', 'Project 2', 'Project 3', 'Project 4', 'Project 5'];
    const codeOptions = ['Project 1', 'Project 2', 'Project 3', 'Project 4', 'Project 5'];

    return (
        <div className="dropDowns mt-5">
            <div className="row d-flex justify-content-center align-items-center">
                <div className="col-md-2">
                </div>
                <div className="col-md-4 d-flex justify-content-center">
                    <div className="form-group">
                        <label className="drops-label" htmlFor="exampleFormControlSelect1">Use Case Document</label>
                        <select className="form-control drops-label" id="exampleFormControlSelect1">
                            <option value="" disabled selected>Use Case</option>
                            {useCaseOptions.map((option, index) => (
                                <option key={index}>{option}</option>
                            ))}
                        </select>
                    </div>
                </div>
                <div className="col-md-4 d-flex justify-content-center">
                    <div className="form-group">
                        <label className="drops-label" htmlFor="exampleFormControlSelect2">Code Document</label>
                        <select className="form-control drops-label" id="exampleFormControlSelect2">
                            <option value="" disabled selected>Code</option>
                            {codeOptions.map((option, index) => (
                                <option key={index}>{option}</option>
                            ))}
                        </select>
                    </div>
                </div>
                <div className="col-md-2">
                </div>
            </div>
            <div className="mt-5">
                </div>
          
             <button type="button" className="process-btn mt-5">Process Documents</button>
            
        </div>

    );
}




export function TraceLinks() {
    const visibleHyperlinks = ['Home', 'About Us', 'Maintainability Scores', 'Trace Links'];

    return (
        <div className="App">
            <Header visibleHyperlinks={visibleHyperlinks} activeLink="Trace Links" />
            <PageTitle title={"Trace Links"} activeLink="Trace Links" />
            <div className="mt-5"></div>
            <div className="mt-5"></div>
            <DropDowns />
        </div>
    );
}
