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
      preAllocatedVUs: 460,
      maxVUs: 460,
      stages: [
        { target: 0, duration: '30s' }, // 워밍업: 0 RPS 유지
        { target: 150, duration: '10s' }, // 스파이크: 0→150 RPS
        { target: 150, duration: '3m' }, // 스파이크 유지: 3분
        { target: 0, duration: '30s' }, // 램프다운: 0 RPS
      ],
    },
  },
  thresholds: {
    http_req_duration: ['p(95)<800'],
  },
};

export default function () {
  const courseId = pickCourse();
  const url = `${BASE_URL}/courses/${courseId}/enroll`;

  const userId = __VU + 41;
  const params = { headers: { 'X-USER-ID': userId } };

  const res = http.post(url, null, params);

  check(res, {
    '✅ 상태 코드 2xx 또는 4xx': (r) => r.status < 500,
    '⏱️ 응답 시간 < 800ms': (r) => r.timings.duration < 800,
  });

  sleep(1); // 실제 사용자 행동을 흉내내기 위해 약간의 대기
}
