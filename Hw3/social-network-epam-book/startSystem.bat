start cmd /k java -jar discovery/build/libs/discovery.jar
TIMEOUT 10
start cmd /k java -jar user/build/libs/user.jar
start cmd /k java -jar timeline/build/libs/timeline.jar
start cmd /k java -jar gateway/build/libs/gateway.jar