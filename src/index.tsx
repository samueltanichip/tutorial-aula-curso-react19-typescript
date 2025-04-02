import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './app/App'; // Ajuste o caminho conforme sua estrutura

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);
root.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);
