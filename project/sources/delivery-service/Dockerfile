FROM java:8-jdk-alpine
RUN  addgroup appgroup && \
     adduser -D app appgroup
COPY ./target/delivery-service*.jar /app/delivery-service.jar
EXPOSE 8000
USER app
CMD ["/usr/bin/java", "-jar", "-Xmx200m", "/app/delivery-service.jar"]