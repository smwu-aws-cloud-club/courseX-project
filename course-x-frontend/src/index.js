import React from 'react';
import ReactDOM from 'react-dom/client';
import reportWebVitals from './reportWebVitals';
import './index.css';

import { RouterProvider, createBrowserRouter } from 'react-router';
import { routes } from 'route';

const root = document.getElementById('root');
const router = createBrowserRouter(routes);

ReactDOM.createRoot(root).render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>
);

reportWebVitals();
