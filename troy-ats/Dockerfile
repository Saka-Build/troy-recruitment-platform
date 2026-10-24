FROM eclipse-temurin:23-jre
LABEL authors="Troy ATS"
WORKDIR /app
COPY target/*.jar troyAts.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","troyAts.jar"]