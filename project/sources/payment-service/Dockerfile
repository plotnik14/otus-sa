FROM java:8-jdk-alpine
RUN  addgroup appgroup && \
     adduser -D app appgroup
COPY ./target/payment-service*.jar /app/payment-service.jar
EXPOSE 8000
USER app
CMD ["/usr/bin/java", "-jar", "-Xmx200m", "/app/payment-service.jar"]