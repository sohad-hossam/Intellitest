import React, { useState } from 'react';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { a11yDark } from 'react-syntax-highlighter/dist/esm/styles/prism';
import { atomDark } from 'react-syntax-highlighter/dist/esm/styles/prism';
import { base16AteliersulphurpoolLight } from 'react-syntax-highlighter/dist/esm/styles/prism';
import { cb } from 'react-syntax-highlighter/dist/esm/styles/prism';
import { coldarkCold } from 'react-syntax-highlighter/dist/esm/styles/prism';
import { coldarkDark } from 'react-syntax-highlighter/dist/esm/styles/prism';
import { coy } from 'react-syntax-highlighter/dist/esm/styles/prism';
import { coyWithoutShadows } from 'react-syntax-highlighter/dist/esm/styles/prism';
import { darcula } from 'react-syntax-highlighter/dist/esm/styles/prism';
import { dark } from 'react-syntax-highlighter/dist/esm/styles/prism';
import { dracula } from 'react-syntax-highlighter/dist/esm/styles/prism';
import { duotoneDark } from 'react-syntax-highlighter/dist/esm/styles/prism';
import { duotoneEarth } from 'react-syntax-highlighter/dist/esm/styles/prism';
import { duotoneForest } from 'react-syntax-highlighter/dist/esm/styles/prism';
import { duotoneLight } from 'react-syntax-highlighter/dist/esm/styles/prism';
import { duotoneSea } from 'react-syntax-highlighter/dist/esm/styles/prism';
import { duotoneSpace } from 'react-syntax-highlighter/dist/esm/styles/prism';
import { funky } from 'react-syntax-highlighter/dist/esm/styles/prism';
import { ghcolors } from 'react-syntax-highlighter/dist/esm/styles/prism';
import { gruvboxDark } from 'react-syntax-highlighter/dist/esm/styles/prism';



const CodeEditor = ({ content, fileType }) => {
  console.log('Content:', content);
  console.log('FileType:', fileType);
  const [error, setError] = useState(null); // Define error state variable

  const getLanguageForFileType = (fileType) => {
    console.log('FileType in getLanguageForFileType:', fileType);
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
  console.log('Language:', language);

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
            maxHeight: '800px', 
            overflowY: 'auto', 
          }}
        preserveWhitespace={true}
        PreTag="div" 
        LineTag="span" 
      >
        {content}
      </SyntaxHighlighter>
      
      
      )}
    </div>
  );
};

export { CodeEditor };
