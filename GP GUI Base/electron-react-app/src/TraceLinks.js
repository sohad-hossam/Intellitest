import React, { useState } from 'react';
import './TraceLinks.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Header } from './TopBar/TopBar';
import { PageTitle } from './PageTitle/PageTitle';


function DropDowns() {
    const useCaseOptions = ['Project 1', 'Project 2', 'Project 3', 'Project 4', 'Project 5'];
    const codeOptions = ['Project 1', 'Project 2', 'Project 3', 'Project 4', 'Project 5'];

    const [useCaseSelected, setUseCaseSelected] = useState(false);
    const [codeSelected, setCodeSelected] = useState(false);

    const handleCaseSelection = (event) => {
        setUseCaseSelected(event.target.value !== "");
    };

    const handleCodeSelection = (event) => {
        setCodeSelected(event.target.value !== "");
    };

    const handleProcessDocs = () => {
        if (!useCaseSelected || !codeSelected) {
            const warningMessage = document.querySelector('.warning-message');
            if (warningMessage) {
                warningMessage.style.display = 'block';
            }
        } else {
            const warningMessage = document.querySelector('.warning-message');
            if (warningMessage) {
                warningMessage.style.display = 'none';
            }
        }
    };
    return (
        <div className="dropDowns mt-5">
            <div className="row d-flex justify-content-center align-items-center">
                <div className="col-md-2">
                </div>
                <div className="col-md-4 d-flex justify-content-center">
                    <div className="form-group">
                        <label className="drops-label" htmlFor="exampleFormControlSelect1">Use Case Document</label>
                        <select className="form-control drops-label" id="exampleFormControlSelect1" onChange={handleCaseSelection} style={{ appearance: 'auto'}}>
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
                        <select className="form-control drops-label" id="exampleFormControlSelect2" onChange={handleCodeSelection}style={{ appearance: 'auto'}}>
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
            <div className="mt-5"></div>
          
             <button type="button" className="process-btn mt-5" onClick={handleProcessDocs}>Process Documents</button>
             <div className="mt-5"></div>
             <div className="mt-5"></div>
             <div className="mt-5"></div>
            <div className="mt-5"></div>
             <div className="warning-message text-danger mt-5" >
                    Please select a Use Case and a Code Document to process.
                </div>
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
