import Button from 'components/button';
import course from 'mock/course.json';
import info from 'mock/info.json';
import style from './CancelPage.module.css';

export default function CancelPage() {
  const { name, totalCredit } = info;

  return (
    <section className={style.container}>
      <div className={style.info_container}>
        <span className={style.strong}>{name}</span>님의 총 수강 학점은{' '}
        <span className={style.strong}>{totalCredit}</span> 학점입니다.
      </div>
      <div className={style.view_container}>
        <div className={`${style.row} ${style.header}`}>
          <div className={style.cell}>과목번호</div>
          <div className={style.cell}>과목명</div>
          <div className={style.cell}>학점</div>
          <div className={style.cell}>교수님</div>
          <div className={style.cell}>강의시간</div>
          <div className={style.cell}>신청</div>
        </div>

        {course.map(({ code, name, credit, professorName, courseSchedule }) => (
          <div key={code} className={style.row}>
            <div className={style.cell}>{code}</div>
            <div className={style.cell}>{name}</div>
            <div className={style.cell}>{credit}</div>
            <div className={style.cell}>{professorName}</div>
            <div className={style.cell}>{courseSchedule}</div>
            <div className={style.cell}>
              <Button onClick={() => alert(`${name} 신청 취소`)}>취소</Button>
            </div>
          </div>
        ))}
      </div>
    </section>
  );
}
