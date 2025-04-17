import { Link, useLocation } from 'react-router-dom';
import { PATH, NAV } from 'constant/path';

import { ReactComponent as EmblemIcon } from 'asset/emblem.svg';
import { ReactComponent as LogoutIcon } from 'asset/logout.svg';

import style from './navbar.module.css';

export default function Navbar() {
  const { pathname } = useLocation();

  const focus = (to) => {
    if (pathname === '/') {
      return to === PATH.enroll;
    }

    return to.includes(pathname);
  };

  const logout = () => {
    alert('로그아웃');
  };

  return (
    <aside className={style.container}>
      <h1 className={style.title}>
        <EmblemIcon />
        CourseX
      </h1>
      <nav className={style.nav}>
        {NAV.map(({ to, Icon, text }) => (
          <Link
            key={`nav-${to}`}
            to={to}
            className={focus(to) ? style.focus : ''}
          >
            <Icon fillOpacity={focus(to) ? 1 : 0.7} />
            <span>{text}</span>
          </Link>
        ))}
      </nav>
      <div className={style.logout_btn} onClick={logout}>
        <LogoutIcon className={style.logoutIcon} />
        <span>로그아웃</span>
      </div>
    </aside>
  );
}
