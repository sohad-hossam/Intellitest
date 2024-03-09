import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import './PageTitle.css';

export function PageTitle({ title }) { 
    return (
      <div className="PageTitle">
        {title}
      </div>
    );
}