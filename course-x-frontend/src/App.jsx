import { Outlet } from 'react-router-dom';
import Navbar from 'components/navbar.jsx';
import Info from 'components/info';
import style from 'App.module.css';

function App() {
  return (
    <main className={style.container}>
      <Navbar />
      <Outlet />
      <Info />
    </main>
  );
}

export default App;
