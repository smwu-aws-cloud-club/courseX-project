import style from './button.module.css';

export default function Button({ children, onClick, disabled }) {
  return (
    <button className={style.button} onClick={onClick} disabled={disabled}>
      {children}
    </button>
  );
}
