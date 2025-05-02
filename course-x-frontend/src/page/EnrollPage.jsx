import { useSearchParams } from 'react-router-dom';
import { useMutation, useQuery } from '@tanstack/react-query';

import { enroll as enrollApi, fetchCourses } from 'api/course';

import Searchbar from 'components/searchbar';
import Button from 'components/button';

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
  const code = searchParams.get('code') ?? '';

  const { data: courses = [], isPending } = useQuery({
    queryKey: ['courses', code],
    queryFn: () => fetchCourses(code),
    staleTime: Infinity,
    cacheTime: Infinity,
  });

  return (
    <section className={style.container}>
      <Searchbar />

      <div className={style.view_container}>
        {isPending && <div>로딩 중...</div>}
        {!isPending && (
          <>
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
          </>
        )}
      </div>
    </section>
  );
}
