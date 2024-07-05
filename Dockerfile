FROM openjdk:17-jdk-alpine
EXPOSE 8080
ADD target/receipt-processor-challenge-docker.jar receipt-processor-challenge-docker.jar
ENTRYPOINT ["java","-jar","/receipt-processor-challenge-docker.jar"]