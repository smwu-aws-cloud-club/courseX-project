// 전체 과목 분산(Spread) 테스트
import http from 'k6/http';
import { check, sleep } from 'k6';

import { BASE_URL } from './config.js';
import { pickCourse } from './utils.js';

export const options = {
  scenarios: {
    enrollSpikeTest: {
      executor: 'ramping-arrival-rate',
      startRate: 0,
      timeUnit: '1s',
      preAllocatedVUs: 1000,
      maxVUs: 2000,
      stages: [
        { target: 0, duration: '30s' }, // 워밍업: 0 RPS 유지
        { target: 1000, duration: '1s' }, // 스파이크: 0→1000 RPS
        { target: 1000, duration: '1m' }, // 스파이크 유지: 1분
        { target: 0, duration: '30s' }, // 램프다운: 0 RPS
      ],
    },
  },
  thresholds: {
    http_req_duration: ['p(95)<1000'],
  },
};

export default function () {
  const courseId = pickCourse();
  const url = `${BASE_URL}/api/courses/${courseId}/enroll`;
  const params = { headers: { 'X-USER-ID': __VU } };

  const res = http.post(url, null, params);

  check(res, {
    '✅ 상태 코드 201(Created) 확인': (r) => r.status === 201,
    '⏱️ 응답 시간 < 1s': (r) => r.timings.duration < 1000,
  });

  sleep(1); // 실제 사용자 행동을 흉내내기 위해 약간의 대기
}
