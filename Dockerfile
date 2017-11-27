FROM java:8-jre-alpine
WORKDIR /app
COPY target/case-yellow-analysis-0.0.1-SNAPSHOT.jar /app
CMD java -jar case-yellow-analysis-0.0.1-SNAPSHOT.jar
