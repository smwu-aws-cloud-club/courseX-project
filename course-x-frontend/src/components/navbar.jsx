import { PATH } from 'constant/path';
import { Link } from 'react-router-dom';

export default function Navbar() {
  return (
    <aside>
      <div></div>
      <nav>
        <Link to={PATH.enroll}>수강신청</Link>
        <Link to={PATH.cancel}>수강취소</Link>
      </nav>
      <div></div>
    </aside>
  );
}
