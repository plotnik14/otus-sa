FROM java:8-jdk-alpine
RUN  addgroup appgroup && \
     adduser -D app appgroup
COPY ./target/otus-app*.jar /app/otus-app.jar
EXPOSE 8000
USER app
CMD ["/usr/bin/java", "-jar", "/app/otus-app.jar"]