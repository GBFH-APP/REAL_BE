import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    vus: 100,           // 동시 접속자 수
    duration: '30s',    // 테스트 시간
};

export default function () {
    const res = http.get('http://3.39.164.199/notice/all/notice/normal'); // 혹은 /main, /home 등 실제 엔드포인트에 따라 변경

    check(res, {
        '✅ 200 응답 받음, 공지사항': (r) => r.status === 200
    });

    sleep(1);

    // 날짜를 2024년 9월 1일부터 랜덤하게 설정 (최근 2주 기준)
    const baseDate = new Date('2025-06-08');
    baseDate.setDate(baseDate.getDate() + (__VU % 7)); // VU별 날짜 분산
    const dateStr = baseDate.toISOString().split('T')[0]; // "2024-09-10" 같은 형식

    const cafeteria = http.get(`http://3.39.164.199/menu/week?date=${dateStr}`);

    check(cafeteria, {
        '✅ 200 응답 받음, 식단표 조회': (r) => r.status === 200});

    sleep(1); // 다음 요청까지 대기
}