import React, { useState } from 'react';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { dracula } from 'react-syntax-highlighter/dist/esm/styles/prism';

const CodeEditor = ({ content, fileType, highlightLines = [] }) => {

  const [error, setError] = useState(null); 

  const getLanguageForFileType = (fileType) => {
    switch (fileType) {
      case 'txt':
        return 'plaintext';
      case 'java':
        return 'java';
      case 'csv':
        return 'python';
      default:
        return 'java';
    }
  };

  const language = getLanguageForFileType(fileType);

  return (
    <div>
      {error ? (
        <div>Error: {error.message}</div>
      ) : (
        <SyntaxHighlighter
          language={language}
          style={dracula}
          customStyle={{
            fontSize: '14px',
            lineHeight: '1.5',
            backgroundColor:  'rgba(15, 42, 42, 0.9)', 
            padding: '20px', 
            borderRadius: '5px',
            overflowX: 'auto', 
            maxHeight: '690px', 
            overflowY: 'auto', 
          }}
          showLineNumbers={true}
          wrapLines={true}
          lineProps={lineNumber => {
            let style = { display: 'block' };
            if (highlightLines.includes(lineNumber)) {
              style.backgroundColor = 'rgba(255, 0, 0, 0.3)'; 
            }
            return { style };
          }}
          PreTag="div" 
        >
          {content}
        </SyntaxHighlighter>
      )}
    </div>
  );
};

export { CodeEditor };
