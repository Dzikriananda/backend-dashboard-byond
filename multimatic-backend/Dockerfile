#
#FROM openjdk:17-jdk-slim
#
## Install the Cloud SQL Proxy
#ADD https://dl.google.com/cloudsql/cloud_sql_proxy.linux.amd64 /cloud_sql_proxy
#RUN chmod +x /cloud_sql_proxy
#
## Copy your app jar
#COPY target/app.jar app.jar
#
#EXPOSE 8080
## Start Cloud SQL Proxy and your app
#CMD ["sh", "-c", "./cloud_sql_proxy -instances=java-helloworld-450410:us-central1:database-cloud=tcp:5432 & java -jar app.jar"]
# Use an official OpenJDK runtime as the base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/app.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
