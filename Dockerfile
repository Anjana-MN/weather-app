FROM openjdk:17-jdk
ENV APP_ID "d2929e9483efc82c82c32ee7e02d563e"
COPY build/libs/weatherforecast-0.0.1-SNAPSHOT.jar weatherforecast-api.jar
CMD ["java","-jar","weatherforecast-api.jar"]