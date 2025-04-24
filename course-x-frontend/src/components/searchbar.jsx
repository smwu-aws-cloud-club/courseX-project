import { useEffect, useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import style from './searchbar.module.css';

export default function Searchbar() {
  const [search, setSearch] = useState('');
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const code = searchParams.get('code') ?? '';

  useEffect(() => {
    if (code !== '' && code !== search) {
      setSearch(code);
    }
  }, [code]);

  const onChangeSearch = (e) => {
    setSearch(e.target.value);
  };

  const onSubmit = () => {
    if (code === search) {
      return;
    }

    navigate(`/enroll?code=${search}`);
  };

  const onKeyDown = (e) => {
    if (e.key === 'Enter') {
      onSubmit();
    }
  };

  return (
    <div className={style.container}>
      <input
        value={search}
        onChange={onChangeSearch}
        onKeyDown={onKeyDown}
        placeholder='과목코드'
      />
      <button onClick={onSubmit}>검색</button>
    </div>
  );
}
