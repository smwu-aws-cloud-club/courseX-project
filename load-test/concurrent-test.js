// 특정 과목 집중(Hotspot) 테스트
import http from 'k6/http';
import { check, fail } from 'k6';
import { Counter } from 'k6/metrics';

import { BASE_URL } from './config.js';

// 커스텀 메트릭: 성공/실패 건수 집계
const successCounter = new Counter('successCount');

export const options = {
  scenarios: {
    concurrentTest: {
      executor: 'per-vu-iterations',
      vus: 115, // 동시 115명의 VU
      iterations: 1,
      maxDuration: '30s', // 30초를 넘으면 시간초과
    },
  },
  thresholds: {
    successCount: ['count>=20', 'count<=20'],
  },
};

const users = [
  -1, 42, 46, 50, 54, 58, 62, 66, 70, 74, 78, 82, 86, 90, 94, 98, 102, 106, 110,
  114, 118, 122, 126, 130, 134, 138, 142, 146, 150, 154, 158, 162, 166, 170,
  174, 178, 182, 186, 190, 194, 198, 202, 206, 210, 214, 218, 222, 226, 230,
  234, 238, 242, 246, 250, 254, 258, 262, 266, 270, 274, 278, 282, 286, 290,
  294, 298, 302, 306, 310, 314, 318, 322, 326, 330, 334, 338, 342, 346, 350,
  354, 358, 362, 366, 370, 374, 378, 382, 386, 390, 394, 398, 402, 406, 410,
  414, 418, 422, 426, 430, 434, 438, 442, 446, 450, 454, 458, 462, 466, 470,
  474, 478, 482, 486, 490, 494, 498,
];

export default function () {
  const courseId = 19; // 수강 인원 20명 제한 과목 ID
  const url = `${BASE_URL}/courses/${courseId}/enroll`;

  const userId = users[__VU];
  const params = { headers: { 'X-USER-ID': userId } };

  try {
    const res = http.post(url, null, params);

    const success = check(res, {
      '✅ 신청 성공': (r) => r.status === 201,
    });

    if (success) {
      successCounter.add(1);
    } else {
      fail(
        `Check 실패 - status: ${res.status}, duration: ${res.timings.duration}`
      );
    }
  } catch (e) {
    fail(`네트워크 요청 실패: ${e.message}`);
  }
}
