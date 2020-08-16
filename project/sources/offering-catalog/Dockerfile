FROM java:8-jdk-alpine
RUN  addgroup appgroup && \
     adduser -D app appgroup
COPY ./target/offering-catalog*.jar /app/offering-catalog.jar
EXPOSE 8000
USER app
CMD ["/usr/bin/java", "-jar", "/app/offering-catalog.jar"]