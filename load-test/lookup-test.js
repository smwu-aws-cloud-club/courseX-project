import http from 'k6/http';
import { check, sleep } from 'k6';

import { BASE_URL } from './config.js';
import { pickCourse } from './utils.js';

export const options = {
  scenarios: {
    allListSpike: {
      executor: 'ramping-arrival-rate',
      startRate: 0,
      timeUnit: '1s',
      preAllocatedVUs: 100,
      maxVUs: 2000,
      stages: [
        { target: 0, duration: '30s' }, // 워밍업: 0 RPS
        { target: 1000, duration: '1s' }, // 스파이크: 1초 만에 0→1000 RPS
        { target: 1000, duration: '1m' }, // 최고 부하 유지: 1분
        { target: 0, duration: '30s' }, // 램프다운: 1000→0
      ],
      exec: 'allList',
      tags: { scenario: 'allListSpike' },
    },
    searchSpread: {
      executor: 'ramping-arrival-rate',
      startTime: '1m',
      startRate: 0,
      timeUnit: '1s',
      preAllocatedVUs: 100,
      maxVUs: 300,
      stages: [
        { target: 0, duration: '30s' }, // 워밍업: 0 RPS
        { target: 200, duration: '1s' }, // 초당 200 RPS 스파이크
        { target: 200, duration: '1m' }, // 1분 유지
        { target: 0, duration: '30s' },
      ],
      exec: 'searchCourse',
      tags: { scenario: 'searchSpread' },
    },
  },
  thresholds: {
    'http_req_duration{scenario:allListSpike}': ['p(95)<1000'], // allListSpike 시나리오에만 적용
    'http_req_duration{scenario:searchSpread}': ['p(95)<500'], // searchSpread 시나리오에만 적용
    http_req_failed: ['rate<0.01'], // 전체 실패율 1% 미만
  },
};

export function allList() {
  const url = `${BASE_URL}/api/courses`;

  const res = http.get(url);

  check(res, {
    '✅ 전체 조회 200 OK': (r) => r.status === 200,
    '⏱️ 응답시간 < 1s': (r) => r.timings.duration < 1000,
  });

  sleep(1); // 실제 사용자 행동을 흉내내기 위해 약간의 대기
}

export function searchCourse() {
  const courseId = pickCourse();
  const url = `${BASE_URL}/api/courses?code=${courseId}`;

  const res = http.get(url);

  check(res, {
    '✅ 개별 조회 200 OK': (r) => r.status === 200,
    '⏱️ 응답시간 < 500ms': (r) => r.timings.duration < 500,
  });

  sleep(1); // 실제 사용자 행동을 흉내내기 위해 약간의 대기
}
