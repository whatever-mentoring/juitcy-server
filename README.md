# ewhatever-server
server repository for EWHATEVER

### (임시) 빌드 및 배포 방법
0. ssh 통신을 위해 qna/pem 하위에 ewhatever.pem 파일 추가하기 
1. 로컬에서 jar build
2. gradle task 중 deploy-deployDev 실행
   - 오류날 경우 원격 서버 접속 후 jar 파일 전송 확인
   - 제대로 전송됐다면, home/ubuntu/qna/bin 위치에서 ./restart.sh 로 쉘스크립트 실행시키기