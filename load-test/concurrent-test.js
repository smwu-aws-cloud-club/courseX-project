// 특정 과목 집중(Hotspot) 테스트
import http from 'k6/http';
import { check } from 'k6';
import { Counter } from 'k6/metrics';

import { BASE_URL } from './config.js';

// 커스텀 메트릭: 성공/실패 건수 집계
const successCounter = new Counter('successCount');
const failCounter = new Counter('failCount');

export const options = {
  scenarios: {
    concurrentTest: {
      executor: 'per-vu-iterations',
      vus: 100, // 동시 100명의 VU
      iterations: 1, // VU당 1회씩 요청 → 총 100건
      maxDuration: '30s', // 30초 경과 시 타임아웃
    },
  },
  thresholds: {
    successCount: ['count>=30', 'count<=30'],
    failCount: ['count>=70', 'count<=70'],
  },
};

export default function () {
  const courseId = 123; // 수강 인원 30명 제한 과목 ID
  const url = `${BASE_URL}/api/courses/${courseId}/enroll`;
  const params = { headers: { 'X-USER-ID': __VU } };

  const res = http.post(url, null, params);

  if (res.status === 201) {
    successCounter.add(1);
  } else if (res.status === 409) {
    failCounter.add(1);
  } else {
    failCounter.add(1);
  }

  check(res, {
    '✅ 상태 코드가 201(Created) 또는 409(Conflict)': (r) =>
      r.status === 201 || r.status === 409,
  });
}
