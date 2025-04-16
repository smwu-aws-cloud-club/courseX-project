import { Outlet } from 'react-router-dom';
import Navbar from 'components/navbar';
import style from 'App.module.css';

function App() {
  return (
    <main className={style.container}>
      <Navbar />
      <Outlet />
    </main>
  );
}

export default App;
