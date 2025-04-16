import App from 'App';
import CancelPage from 'page/CancelPage';
import EnrollPage from 'page/EnrollPage';

export const routes = [
  {
    path: '/',
    element: <App />,
    children: [
      {
        index: true,
        element: <EnrollPage />,
      },
      {
        path: '/enroll',
        element: <EnrollPage />,
      },
      {
        path: '/cancel',
        element: <CancelPage />,
      },
    ],
  },
];
