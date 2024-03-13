import React, { useEffect, useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import './TopBar.css';


export function Header({ visibleHyperlinks,activeLink  }) {
    const allHyperlinks = [
        { url: '/', label: 'Home' }, 
        { url: './AboutUs', label: 'About Us' },
        { url: '/ImportProject', label: 'Import Project' },
        { url: '/ProceedWith', label: 'Proceed With' },
        { url: '/MaintainbiltyScores', label: 'Maintainability Scores' },
        { url: '/TraceLinks', label: 'Trace Links' },

    ];

    const filteredHyperlinks = allHyperlinks.filter(link =>
        visibleHyperlinks.includes(link.label)
    );

    const iconSource =  (activeLink === 'Home' || activeLink === 'AboutUs')  ? require("../assets/searching1.png") : require("../assets/searching.png");

    return (
        <header className="header">
        <div className="container">
            <div className="row">
                <div className="col-auto">
                    <img src={iconSource} alt="Icon" />
                </div>
                <div className="col-md-10 d-flex justify-content-center align-items-center">
                    {filteredHyperlinks.map(link => (
                    <a
                            className={`hyperpages mx-5`}
                            key={link.label}
                            href={link.url}
                            style={{ 
                            color: (activeLink === 'Home' || activeLink === 'AboutUs') ? 'white' : '#092635',
                            textDecoration: activeLink === link.label ? 'underline' : 'none'  
                            }}
                        >
                            {link.label}
                  </a>
                    ))}
                </div>
            </div>
        </div>
    </header>
    
    );
}
