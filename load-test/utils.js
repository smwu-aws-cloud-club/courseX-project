const COURSE_IDS = [101, 102, 103]; // 테스트용 과목 ID 목록

export function pickCourse() {
  const index = (__VU + __ITER) % COURSE_IDS.length;
  return COURSE_IDS[index];
}
