FROM amazoncorretto:25-alpine
ADD target/my-budget*.jar /my-budget-api.jar
EXPOSE 8080
CMD java -jar /my-budget-api.jar