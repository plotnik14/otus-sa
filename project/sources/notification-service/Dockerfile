FROM java:8-jdk-alpine
RUN  addgroup appgroup && \
     adduser -D app appgroup
COPY ./target/notification-service*.jar /app/notification-service.jar
EXPOSE 8000
USER app
CMD ["/usr/bin/java", "-jar", "/app/notification-service.jar"]