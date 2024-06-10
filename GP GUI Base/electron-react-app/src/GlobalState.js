// src/GlobalState.js
import React, { createContext, useState } from 'react';

const GlobalContext = createContext();

const GlobalProvider = ({ children }) => {
    const [summaryDescription, setSummaryDescription] = useState('');

    return (
        <GlobalContext.Provider value={{ summaryDescription, setSummaryDescription }}>
            {children}
        </GlobalContext.Provider>
    );
};

export { GlobalContext, GlobalProvider };
