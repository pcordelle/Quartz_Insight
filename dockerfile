# build stage
FROM maven:3.6.0-jdk-11

# copy app
RUN mkdir /app/
RUN mkdir /app/src
RUN mkdir /app/classes
RUN mkdir /app/dependencies

COPY Game.java /app/src/Game.java
COPY GameController.java /app/src/GameController.java
COPY GameService.java /app/src/GameService.java
COPY JsonUtil.java /app/src/JsonUtil.java
COPY Main.java /app/src/Main.java
COPY ResponseError.java /app/src/ResponseError.java
COPY User.java /app/src/User.java
COPY UserController.java /app/src/UserController.java
COPY UserService.java /app/src/UserService.java
COPY pom.xml /app/pom.xml

#setup working directory
WORKDIR /app

#install app
RUN cd /app

# get dependencies
RUN mvn dependency:copy-dependencies -DoutputDirectory=/app/dependencies -Dhttps.protocols=TLSv1.2

# compil app
RUN javac /app/src/*.java -d /app/classes -classpath "/app/dependencies/spark-core-2.2.jar:/app/dependencies/slf4j-api-1.7.7.jar:/app/dependencies/slf4j-simple-1.7.7.jar:/app/dependencies/jetty-server-9.0.2.v20130417.jar:/app/dependencies/javax.servlet-3.0.0.v201112011016.jar:/app/dependencies/jetty-http-9.0.2.v20130417.jar:/app/dependencies/jetty-util-9.0.2.v20130417.jar:/app/dependencies/jetty-io-9.0.2.v20130417.jar:/app/dependencies/jetty-webapp-9.0.2.v20130417.jar:/app/dependencies/jetty-xml-9.0.2.v20130417.jar:/app/dependencies/jetty-servlet-9.0.2.v20130417.jar:/app/dependencies/jetty-security-9.0.2.v20130417.jar:/app/dependencies/gson-2.2.4.jar:/app/dependencies/postgresql-42.2.20.jar:/app/dependencies/checker-qual-3.5.0.jar"

# start app
CMD java -Dfile.encoding=Cp1252 -classpath "/app/classes:/app/dependencies/spark-core-2.2.jar:/app/dependencies/slf4j-api-1.7.7.jar:/app/dependencies/slf4j-simple-1.7.7.jar:/app/dependencies/jetty-server-9.0.2.v20130417.jar:/app/dependencies/javax.servlet-3.0.0.v201112011016.jar:/app/dependencies/jetty-http-9.0.2.v20130417.jar:/app/dependencies/jetty-util-9.0.2.v20130417.jar:/app/dependencies/jetty-io-9.0.2.v20130417.jar:/app/dependencies/jetty-webapp-9.0.2.v20130417.jar:/app/dependencies/jetty-xml-9.0.2.v20130417.jar:/app/dependencies/jetty-servlet-9.0.2.v20130417.jar:/app/dependencies/jetty-security-9.0.2.v20130417.jar:/app/dependencies/gson-2.2.4.jar:/app/dependencies/postgresql-42.2.20.jar:/app/dependencies/checker-qual-3.5.0.jar" app.Main

# expose a port
EXPOSE 4567
