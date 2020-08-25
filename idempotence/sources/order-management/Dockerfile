FROM java:8-jdk-alpine
RUN  addgroup appgroup && \
     adduser -D app appgroup
COPY ./target/order-management*.jar /app/order-management.jar
EXPOSE 8000
USER app
CMD ["/usr/bin/java", "-jar", "-Xmx200m", "/app/order-management.jar"]