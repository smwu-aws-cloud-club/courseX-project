import { ReactComponent as EnrollIcon } from 'asset/enroll.svg';
import { ReactComponent as CancelIcon } from 'asset/cancel.svg';

export const PATH = {
  enroll: '/enroll',
  cancel: '/cancel',
};

export const NAV = [
  {
    to: PATH.enroll,
    Icon: EnrollIcon,
    text: '수강신청',
  },
  {
    to: PATH.cancel,
    Icon: CancelIcon,
    text: '수강취소',
  },
];
