import React, { useState } from 'react';
import './TraceLinks.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Header } from '../TopBar/TopBar';
import { PageTitle } from '../PageTitle/PageTitle';


function DropDowns() {
    const useCaseOptions = ['Project 1', 'Project 2', 'Project 3', 'Project 4', 'Project 5'];
    const codeOptions = ['Project 1', 'Project 2', 'Project 3', 'Project 4', 'Project 5'];
    const dictionary = {
        'Project 1 - Project 1': 10,
        'Project 1 - Project 2': 20,
        'Project 1 - Project 3': 30,
        'Project 1 - Project 4': 40,
        'Project 1 - Project 5': 50,
        'Project 2 - Project 1': 60,
        'Project 2 - Project 2': 70,
        'Project 2 - Project 3': 80,
        'Project 2 - Project 4': 90,
        'Project 2 - Project 5': 100,
        'Project 3 - Project 1': 110,
        'Project 3 - Project 2': 120,
        'Project 3 - Project 3': 130,
        'Project 3 - Project 4': 140,
        'Project 3 - Project 5': 150,
        'Project 4 - Project 1': 160,
        'Project 4 - Project 2': 170,
        'Project 4 - Project 3': 180,
        'Project 4 - Project 4': 190,
        'Project 4 - Project 5': 200,
        'Project 5 - Project 1': 210,
        'Project 5 - Project 2': 220,
        'Project 5 - Project 3': 230,
        'Project 5 - Project 4': 240,
        'Project 5 - Project 5': 250,
    };
    const [useCaseSelected, setUseCaseSelected] = useState(false);
    const [codeSelected, setCodeSelected] = useState(false);
   
    const handleCaseSelection = (event) => {
        setUseCaseSelected(event.target.value !== "");
    };

    const handleCodeSelection = (event) => {
        setCodeSelected(event.target.value !== "");
    };
    function getScore(useCase, code) {
        const key = `${useCase} - ${code}`;
        return dictionary[key];
    }
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

            const selectedUseCase = document.getElementById('exampleFormControlSelect1').value;
            const selectedCode = document.getElementById('exampleFormControlSelect2').value;
            const score = getScore(selectedUseCase, selectedCode);
            const tracelinkMessage = document.querySelector('.tracelink-message');
            if (tracelinkMessage) {
                tracelinkMessage.style.display = 'block';
                tracelinkMessage.innerHTML = `Trace Links Exist between the documents by ${score}% indicating high correlation`;
            }
          
        }
    };
    return (
        <div className="dropDowns mt-5">
        <div className="row justify-content-center align-items-center">
            <div className="col-md-4 d-flex justify-content-center">
                <div className="form-group">
                    <label className="drops-label" htmlFor="exampleFormControlSelect1">Use Case Document</label>
                    <select className="form-control drops-label" id="exampleFormControlSelect1" onChange={handleCaseSelection}>
                        <option value="" selected hidden>Use Case</option>
                        {useCaseOptions.map((option, index) => (
                            <option key={index}>{option}</option>
                        ))}
                    </select>
                </div>
            </div>
            <div className="col-md-4 d-flex justify-content-center">
                <div className="form-group">
                    <label className="drops-label" htmlFor="exampleFormControlSelect2">Code Document</label>
                    <select className="form-control drops-label" id="exampleFormControlSelect2" onChange={handleCodeSelection}>
                        <option value="" selected hidden>Code</option>
                        {codeOptions.map((option, index) => (
                            <option key={index}>{option}</option>
                        ))}
                    </select>
                </div>
            </div>
        </div>
    
        <div className="row justify-content-center mt-5">
            <div className="col-md-4 d-flex justify-content-center">
                <button type="button" className="process-btn" onClick={handleProcessDocs}>Process Documents</button>
            </div>
        </div>
    
        <div className="row justify-content-center mt-3">
            <div className="col-md-4">
                <div className="warning-message text-danger">
                    Please select a Use Case and a Code Document to process.
                </div>
            </div>
        </div>


        <div className="row justify-content-center mt-5">
            <div className="col-md-4">
                <div className="tracelink-message">
               
                </div>
            </div>
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
            <DropDowns />
        </div>
    );
}
