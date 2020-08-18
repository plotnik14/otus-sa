FROM java:8-jdk-alpine
RUN  addgroup appgroup && \
     adduser -D app appgroup
COPY ./target/user-management*.jar /app/user-management.jar
EXPOSE 8000
USER app
CMD ["/usr/bin/java", "-jar", "-Xmx200m", "/app/user-management.jar"]