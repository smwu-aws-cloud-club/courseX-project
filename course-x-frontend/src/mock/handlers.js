import { rest } from 'msw';
import course from 'mock/course.json';

const delay = 2000;

export const handlers = [
  // 수강신청
  rest.post('/api/courses/:course_id/enroll', async (req, res, ctx) => {
    const { course_id } = req.params;

    if (course_id === '1') {
      return res(
        ctx.delay(delay),
        ctx.status(201),
        ctx.json({
          success: true,
          message: '수강신청에 성공했습니다',
          data: null,
        })
      );
    }

    if (course_id === '2') {
      return res(
        ctx.delay(delay),
        ctx.status(400),
        ctx.json({
          success: false,
          message: '시간표가 중복되는 강의가 존재합니다',
          data: null,
        })
      );
    }

    if (course_id === '3') {
      return res(
        ctx.delay(delay),
        ctx.status(400),
        ctx.json({
          success: false,
          message: '이미 수강 신청이 완료된 강의입니다',
          data: null,
        })
      );
    }

    if (course_id === '4') {
      return res(
        ctx.delay(delay),
        ctx.status(400),
        ctx.json({
          success: false,
          message: '전공이 일치하는 전공수업이 아닙니다',
          data: null,
        })
      );
    }

    return res(
      ctx.delay(delay),
      ctx.status(404),
      ctx.json({
        success: false,
        message: '해당하는 강의를 찾을 수 없습니다',
        data: null,
      })
    );
  }),

  // 강의 조회
  rest.get('/api/courses', async (req, res, ctx) => {
    const code = req.url.searchParams.get('code');

    if (!code) {
      return res(
        ctx.delay(delay),
        ctx.status(200),
        ctx.json({
          success: true,
          message: '강의 목록 조회에 성공했습니다.',
          data: course,
        })
      );
    }

    return res(
      ctx.delay(delay),
      ctx.status(200),
      ctx.json({
        success: true,
        message: '강의 목록 조회에 성공했습니다.',
        data: [
          {
            code: '1234',
            name: '데이터베이스프로그래밍',
            credit: 3,
            professorName: '심준호',
            courseSchedule: '월: 12:30 - 13:30, 수: 12:30 - 13:30',
            maxStudent: 30,
            remainingSeats: 30,
          },
        ],
      })
    );
  }),
];
