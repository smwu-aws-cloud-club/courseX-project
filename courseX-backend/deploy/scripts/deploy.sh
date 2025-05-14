#!/bin/bash

if [ -z "$ECR_REGISTRY" ] || [ -z "$IMAGE_TAG" ]; then
  echo "환경 변수가 올바르게 설정되지 않았습니다."
  echo "ECR_REGISTRY와 IMAGE_TAG가 필요합니다."
  exit 1
fi

if [ -f .env ]; then
  ACTUATOR_PATH_VALUE=$(grep -m 1 "ACTUATOR_PATH=" .env | cut -d '=' -f2)
  if [ -n "$ACTUATOR_PATH_VALUE" ]; then
    ACTUATOR_PATH=$ACTUATOR_PATH_VALUE
  else
    ACTUATOR_PATH="/actuator"
  fi
else
  ACTUATOR_PATH="/actuator"
fi

HEALTH_ENDPOINT="${ACTUATOR_PATH}/health"

ACTIVE_SERVICE=$(grep -o 'backend-[a-z]*' nginx/conf.d/default.conf | head -1)

if [ "$ACTIVE_SERVICE" == "backend-blue" ]; then
  TARGET_SERVICE="backend-green"
else
  TARGET_SERVICE="backend-blue"
fi

echo "현재 활성 서비스: $ACTIVE_SERVICE, 배포 대상: $TARGET_SERVICE"
echo "Actuator 경로: $ACTUATOR_PATH"
echo "헬스체크 엔드포인트: http://$TARGET_SERVICE:8080$HEALTH_ENDPOINT"

docker pull $ECR_REGISTRY/coursex-backend:$IMAGE_TAG

docker-compose up -d --no-deps $TARGET_SERVICE

echo "헬스 체크 시작..."
for i in {1..10}; do
  echo "시도 $i/10..."
  sleep 3

  CONTAINER_STATUS=$(docker inspect --format="{{.State.Running}}" coursex-$TARGET_SERVICE 2>/dev/null)

  if [ "$CONTAINER_STATUS" == "true" ]; then
    HEALTH_CHECK=$(curl -s -o /dev/null -w "%{http_code}" http://$TARGET_SERVICE:8080$HEALTH_ENDPOINT 2>/dev/null)

    if [ "$HEALTH_CHECK" == "200" ]; then
      echo "새 서비스 정상 작동 확인. Nginx 설정 변경 중..."

      sed -i "s/$ACTIVE_SERVICE/$TARGET_SERVICE/g" nginx/conf.d/default.conf
      docker exec coursex-nginx nginx -s reload

      echo "트래픽이 $TARGET_SERVICE로 전환되었습니다."
      echo "이전 서비스($ACTIVE_SERVICE) 중지 및 정리 중..."
      docker-compose stop $ACTIVE_SERVICE
      docker-compose rm -f $ACTIVE_SERVICE
      docker image prune -a

      exit 0
    else
      echo "헬스 체크 실패: 상태 코드 $HEALTH_CHECK"
    fi
  else
    echo "컨테이너가 실행 중이지 않습니다."
  fi
done

echo "새 서비스 상태 확인에 실패했습니다. 배포를 중단합니다."
exit 1