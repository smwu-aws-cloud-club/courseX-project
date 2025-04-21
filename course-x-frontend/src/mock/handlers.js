import { rest } from 'msw';

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
];
