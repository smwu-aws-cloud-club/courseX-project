import style from './info.module.css';

export default function Info() {
  return (
    <aside className={style.container}>
      <div className={style.item}>
        <span className={style.label}>이름</span>
        <span className={style.text}>눈송이</span>
      </div>
      <div className={style.item}>
        <span className={style.label}>학번</span>
        <span className={style.text}>0000000</span>
      </div>
      <div className={style.item}>
        <span className={style.label}>전공</span>
        <span className={style.text}>컴퓨터과학전공</span>
      </div>
      <div className={style.item}>
        <span className={style.label}>수강가능학점</span>
        <span className={style.text}>17</span>
      </div>
    </aside>
  );
}
