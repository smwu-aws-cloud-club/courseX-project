import { useMutation, useQuery } from '@tanstack/react-query';

import { fetchEnrollments, cancel as cancelApi } from 'api/course';

import Button from 'components/button';

import info from 'mock/info.json';
import style from './CancelPage.module.css';

function Row({
  enrollmentId,
  courseCode,
  courseName,
  courseCredit,
  courseProfessorName,
  courseSchedule,
}) {
  const cancel = useMutation({
    mutationFn: (enrollmentId) => cancelApi(enrollmentId),
    onSuccess: (message) => {
      alert(message);
    },
    onError: (error) => {
      alert(error.message);
    },
  });

  return (
    <div className={style.row}>
      <div className={style.cell}>{courseCode}</div>
      <div className={style.cell}>{courseName}</div>
      <div className={style.cell}>{courseCredit}</div>
      <div className={style.cell}>{courseProfessorName}</div>
      <div className={style.cell}>
        {courseSchedule
          .split(',')
          .map((str) => str.trim())
          .join(`\n`)}
      </div>
      <div className={style.cell}>
        <Button
          onClick={() => cancel.mutate(enrollmentId)}
          disabled={cancel.isPending}
        >
          {cancel.isPending ? '...' : '취소'}
        </Button>
      </div>
    </div>
  );
}

export default function CancelPage() {
  const { name, totalCredit } = info;
  const { data: courses, isPending } = useQuery({
    queryKey: ['enrollments'],
    queryFn: fetchEnrollments,
    staleTime: Infinity,
    cacheTime: Infinity,
  });

  return (
    <section className={style.container}>
      <div className={style.info_container}>
        <span className={style.strong}>{name}</span>님의 총 수강 학점은{' '}
        <span className={style.strong}>{totalCredit}</span> 학점입니다.
      </div>
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
              <div className={style.cell}>신청</div>
            </div>

            {courses.map((course) => (
              <Row key={`enroll-list-${course.enrollmentId}`} {...course} />
            ))}
          </>
        )}
      </div>
    </section>
  );
}
