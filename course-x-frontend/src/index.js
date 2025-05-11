import React from 'react';
import ReactDOM from 'react-dom/client';
import reportWebVitals from 'reportWebVitals';
import './index.css';

import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { RouterProvider, createBrowserRouter } from 'react-router';
import { routes } from 'route';

const root = document.getElementById('root');
const router = createBrowserRouter(routes);

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: false, // 🔥 전역에서 쿼리 재시도 끔
      refetchOnWindowFocus: false,
    },
    mutations: {
      retry: false, // (선택) 변이 재시도도 끄려면 이 줄도 추가
    },
  },
});

if (process.env.NODE_ENV === 'development') {
  const { worker } = require('./mock/browser.js');
  worker.start();
}

ReactDOM.createRoot(root).render(
  <React.StrictMode>
    <QueryClientProvider client={queryClient}>
      <RouterProvider router={router} />
    </QueryClientProvider>
  </React.StrictMode>
);

reportWebVitals();
