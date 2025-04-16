import { useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';
import Searchbar from 'components/searchbar';
import Button from 'components/button';
import course from 'mock/course.json';
import style from './EnrollPage.module.css';

export default function EnrollPage() {
  const [searchParams] = useSearchParams();
  const { q } = Object.fromEntries(searchParams.entries());

  useEffect(() => {
    console.log(`${q} 검색`);
  }, [q]);

  return (
    <section className={style.container}>
      <Searchbar />
      <div className={style.view_container}>
        <div className={`${style.row} ${style.header}`}>
          <div className={style.cell}>과목번호</div>
          <div className={style.cell}>과목명</div>
          <div className={style.cell}>학점</div>
          <div className={style.cell}>교수님</div>
          <div className={style.cell}>강의시간</div>
          <div className={style.cell}>정원</div>
          <div className={style.cell}>여석</div>
          <div className={style.cell}>신청</div>
        </div>

        {course.map(
          ({
            code,
            name,
            credit,
            professorName,
            courseSchedule,
            maxStudent,
            remainingSeats,
          }) => (
            <div key={code} className={style.row}>
              <div className={style.cell}>{code}</div>
              <div className={style.cell}>{name}</div>
              <div className={style.cell}>{credit}</div>
              <div className={style.cell}>{professorName}</div>
              <div className={style.cell}>{courseSchedule}</div>
              <div className={style.cell}>{maxStudent}</div>
              <div className={style.cell}>{remainingSeats}</div>
              <div className={style.cell}>
                <Button onClick={() => alert(`${name} 신청`)}>신청</Button>
              </div>
            </div>
          )
        )}
      </div>
    </section>
  );
}
