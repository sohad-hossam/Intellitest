import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import './TopBar.css';


export function Header({ visibleHyperlinks,activeLink  }) {
    const allHyperlinks = [
        { url: '/', label: 'Home' }, 
        { url: '../AboutUs', label: 'About Us' },
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
            <div className="row align-items-center">
                <div className="col-3">
                    {/* Use the conditional icon source */}
                    <img src={iconSource} alt="Icon" />
                </div>
                <div className="col-6">
                    <div className="row">
                        {filteredHyperlinks.map((link, index) => (
                            <div key={index} className="col">
                                <a href={link.url} className={`hyperpages mt-4 ${link.label === activeLink ? 'activeLink' : ''}`} >{link.label}</a>
                            </div>
                        ))}
                    </div>
                </div>
            </div>
        </header>
    );
}
