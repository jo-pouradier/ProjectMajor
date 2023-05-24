FROM openjdk:8
RUN mkdir -p /app/SP
WORKDIR /app/SP
COPY ./target/FireApp-0.0.1-SNAPSHOT.jar FireApp-0.0.1-SNAPSHOT.jar
CMD ["java", "-jar", "FireApp-0.0.1-SNAPSHOT.jar"]
EXPOSE 80