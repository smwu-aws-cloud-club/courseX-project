import { useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';
import { useMutation } from '@tanstack/react-query';

import { enroll as enrollApi } from 'api/course';

import Searchbar from 'components/searchbar';
import Button from 'components/button';

import courses from 'mock/course.json';
import style from './EnrollPage.module.css';

function Row({
  code,
  name,
  credit,
  professorName,
  courseSchedule,
  maxStudent,
  remainingSeats,
}) {
  const enroll = useMutation({
    mutationFn: (code) => enrollApi(code),
    onSuccess: (message) => {
      alert(message);
    },
    onError: (error) => {
      alert(error.message);
    },
  });

  return (
    <div className={style.row}>
      <div className={style.cell}>{code}</div>
      <div className={style.cell}>{name}</div>
      <div className={style.cell}>{credit}</div>
      <div className={style.cell}>{professorName}</div>
      <div className={style.cell}>
        {courseSchedule
          .split(',')
          .map((str) => str.trim())
          .join(`\n`)}
      </div>
      <div className={style.cell}>{maxStudent}</div>
      <div className={style.cell}>{remainingSeats}</div>
      <div className={style.cell}>
        <Button onClick={() => enroll.mutate(code)} disabled={enroll.isPending}>
          {enroll.isPending ? '...' : '신청'}
        </Button>
      </div>
    </div>
  );
}

export default function EnrollPage() {
  const [searchParams] = useSearchParams();
  const { code } = Object.fromEntries(searchParams.entries());

  useEffect(() => {
    console.log(`${code} 검색`);
  }, [code]);

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

        {courses.map((course) => (
          <Row key={`course-${course.code}`} {...course} />
        ))}
      </div>
    </section>
  );
}
