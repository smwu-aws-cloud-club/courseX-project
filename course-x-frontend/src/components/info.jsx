import info from 'mock/info.json';
import style from './info.module.css';

export default function Info() {
  const { name, studentId, major, availableCredit } = info;

  return (
    <aside className={style.container}>
      <div className={style.item}>
        <span className={style.label}>이름</span>
        <span className={style.text}>{name}</span>
      </div>
      <div className={style.item}>
        <span className={style.label}>학번</span>
        <span className={style.text}>{studentId}</span>
      </div>
      <div className={style.item}>
        <span className={style.label}>전공</span>
        <span className={style.text}>{major}</span>
      </div>
      <div className={style.item}>
        <span className={style.label}>수강가능학점</span>
        <span className={style.text}>{availableCredit}</span>
      </div>
    </aside>
  );
}
