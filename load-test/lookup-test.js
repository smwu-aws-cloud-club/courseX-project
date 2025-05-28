import http from 'k6/http';
import { check, fail, sleep } from 'k6';

import { BASE_URL } from './config.js';
import { pickCourse } from './utils.js';

export const options = {
  scenarios: {
    allListSpike: {
      executor: 'ramping-arrival-rate',
      startRate: 0,
      timeUnit: '1s',
      preAllocatedVUs: 460,
      maxVUs: 460,
      stages: [
        { target: 0, duration: '30s' }, // 워밍업: 0 RPS
        { target: 150, duration: '10s' }, // 스파이크: 0→150 RPS
        { target: 150, duration: '3m' }, // 스파이크 유지: 3분
        { target: 0, duration: '30s' }, // 램프다운: 0 RPS
      ],
      exec: 'allList',
      tags: { scenario: 'allListSpike' },
    },
    searchSpread: {
      executor: 'ramping-arrival-rate',
      startTime: '1m',
      startRate: 0,
      timeUnit: '1s',
      preAllocatedVUs: 460,
      maxVUs: 460,
      stages: [
        { target: 0, duration: '30s' }, // 워밍업: 0 RPS
        { target: 150, duration: '10s' }, // 스파이크: 0→150 RPS
        { target: 150, duration: '3m' }, // 스파이크 유지: 3분
        { target: 0, duration: '30s' }, // 램프다운: 0 RPS
      ],
      exec: 'searchCourse',
      tags: { scenario: 'searchSpread' },
    },
  },
  thresholds: {
    'http_req_duration{scenario:allListSpike}': ['p(95)<800'], // allListSpike 시나리오에만 적용
    'http_req_duration{scenario:searchSpread}': ['p(95)<800'], // searchSpread 시나리오에만 적용
    http_req_failed: ['rate<0.01'], // 전체 실패율 1% 미만
  },
};

export function allList() {
  const url = `${BASE_URL}/courses`;

  try {
    const res = http.get(url);

    const success = check(res, {
      '✅ 전체 조회 200 OK': (r) => r.status === 200,
    });

    if (!success) {
      fail(
        `Check 실패 - status: ${res.status}, duration: ${res.timings.duration}`
      );
    }
  } catch (e) {
    fail(`네트워크 요청 실패: ${e.message}`);
  }

  sleep(1); // 실제 사용자 행동을 흉내내기 위해 약간의 대기
}

export function searchCourse() {
  const courseId = pickCourse();
  const url = `${BASE_URL}/courses?code=${courseId}`;

  try {
    const res = http.get(url);

    const success = check(res, {
      '✅ 개별 조회 200 OK': (r) => r.status === 200,
    });

    if (!success) {
      fail(
        `Check 실패 - status: ${res.status}, duration: ${res.timings.duration}`
      );
    }
  } catch (e) {
    fail(`네트워크 요청 실패: ${e.message}`);
  }

  sleep(1); // 실제 사용자 행동을 흉내내기 위해 약간의 대기
}
