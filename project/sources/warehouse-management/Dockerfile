FROM java:8-jdk-alpine
RUN  addgroup appgroup && \
     adduser -D app appgroup
COPY ./target/warehouse-management*.jar /app/warehouse-management.jar
EXPOSE 8000
USER app
CMD ["/usr/bin/java", "-jar", "/app/warehouse-management.jar"]