#!/bin/bash

set -e  # 명시적 실패 이외에는 중단하지 않음

NGINX_CONFIG_PATH="/etc/nginx/conf.d"
ACTUATOR_PATH=${ACTUATOR_PATH:-"/actuator"}
HEALTH_ENDPOINT="${ACTUATOR_PATH}/health"

if grep -q "8081" "${NGINX_CONFIG_PATH}/service-url.inc"; then
  BLUE_PORT="8081"
  BLUE_SERVICE="backend-blue"
  GREEN_PORT="8082"
  GREEN_SERVICE="backend-green"
else
  BLUE_PORT="8082"
  BLUE_SERVICE="backend-green"
  GREEN_PORT="8081"
  GREEN_SERVICE="backend-blue"
fi

echo "------------------------------------------------------------"
echo "> 현재 활성 서비스: $BLUE_SERVICE ($BLUE_PORT)"
echo "> 배포 대상 서비스: $GREEN_SERVICE ($GREEN_PORT)"
echo "> ACTUATOR_PATH: $ACTUATOR_PATH"
echo "> HEALTH_ENDPOINT: $HEALTH_ENDPOINT"
echo "------------------------------------------------------------"

# === 이미지 Pull (필요 시) ===
echo "> 도커 이미지 Pull (선택 사항)"
docker-compose pull $GREEN_SERVICE

# === 새 서비스 컨테이너 실행 ===
echo "> $GREEN_SERVICE 실행 시작"
docker-compose up -d --no-deps $GREEN_SERVICE

# === 1차 헬스 체크: 직접 포트로 확인 (프록시 전환 전) ===
sleep 10
echo "> ${GREEN_PORT} 포트 헬스 체크 시작 (Nginx 전환 전)"
for i in {1..10}; do
  sleep 3
  STATUS=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:${GREEN_PORT}${HEALTH_ENDPOINT} || true)

  if [ "$STATUS" == "200" ]; then
    echo "> ✅ 헬스 체크 통과 (직접 접근)"
    break
  elif [ -z "$STATUS" ]; then
    echo "> ❌ 응답 없음 (curl 실패)"
  else
    echo "> 응답 상태 코드: $STATUS"
  fi

  if [ "$i" -eq 10 ]; then
    echo "> ❌ 헬스 체크 실패. 롤백 수행..."
    docker-compose stop $GREEN_SERVICE
    docker-compose rm -f $GREEN_SERVICE
    exit 1
  fi
done

# === 프록시 전환 ===
echo "------------------------------------------------------------"
echo "> Nginx 프록시 전환: ${GREEN_PORT} 포트로"
echo "proxy_pass http://localhost:${GREEN_PORT};" | sudo tee ${NGINX_CONFIG_PATH}/service-url.inc > /dev/null
sudo nginx -t && sudo nginx -s reload

# === 2차 헬스 체크: Nginx 경유 접근 확인 ===
echo "> Nginx 프록시 경유 헬스 체크 중..."
sleep 2
response=$(curl -s http://localhost${HEALTH_ENDPOINT} || true)
up_count=$(echo "$response" | grep -o '"status":"UP"' | wc -l)

if [ "$up_count" -ge 1 ]; then
  echo "> 🎉 프록시 전환 성공 및 Nginx 접근 정상"
else
  echo "> ❌ 프록시된 서비스 접근 실패"
  echo "> 응답 내용: $response"
  exit 1
fi

# === 이전 서비스 종료 및 정리 ===
echo "------------------------------------------------------------"
echo "> 이전 서비스 $BLUE_SERVICE 중단 및 제거"
docker-compose stop $BLUE_SERVICE
docker-compose rm -f $BLUE_SERVICE
docker image prune -a -f

echo "------------------------------------------------------------"
echo "> 🚀 배포 완료: 현재 활성 서비스 → $GREEN_SERVICE"