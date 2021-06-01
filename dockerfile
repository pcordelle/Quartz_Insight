#base image is Maven with java JDK
FROM openjdk:16-alpine3.13

#copy app
RUN mkdir /app/
RUN mkdir /app/src
RUN mkdir /app/classes
COPY Game.java /app/src/Game.java
COPY GameController.java /app/src/GameController.java
COPY GameService.java /app/src/GameService.java
COPY JsonUtil.java /app/src/JsonUtil.java
COPY /Mainjava /app/src/Main.java
COPY ResponseError.java /app/src/ResponseError.java
COPY User.java /app/src/User.java
COPY UserController.java /app/src/UserController.java
COPY UserService.java /app/srcUserService.java
COPY ./pom.xml /app/pom.xml

#setup working directory
WORKDIR /app

#install app
RUN cd /app
RUN mvnw dependency:go-offline

#compil app
javac /app/src/*.java -d /app/classes

#start app
java -Dfile.encoding=Cp1252 -classpath "\app\classes;\.p2\pool\plugins\org.junit.jupiter.api_5.7.1.v20210222-1948.jar;\.p2\pool\plugins\org.junit.jupiter.engine_5.7.1.v20210222-1948.jar;\.p2\pool\plugins\org.junit.jupiter.migrationsupport_5.7.1.v20210222-1948.jar;\.p2\pool\plugins\org.junit.jupiter.params_5.7.1.v20210222-1948.jar;\.p2\pool\plugins\org.junit.platform.commons_1.7.1.v20210222-1948.jar;\.p2\pool\plugins\org.junit.platform.engine_1.7.1.v20210222-1948.jar;\.p2\pool\plugins\org.junit.platform.launcher_1.7.1.v20210222-1948.jar;\.p2\pool\plugins\org.junit.platform.runner_1.7.1.v20210222-1948.jar;\.p2\pool\plugins\org.junit.platform.suite.api_1.7.1.v20210222-1948.jar;\.p2\pool\plugins\org.junit.vintage.engine_5.7.1.v20210222-1948.jar;\.p2\pool\plugins\org.opentest4j_1.2.0.v20190826-0900.jar;\.p2\pool\plugins\org.apiguardian_1.1.0.v20190826-0900.jar;\.p2\pool\plugins\org.junit_4.13.0.v20200204-1500.jar;\.p2\pool\plugins\org.hamcrest.core_1.3.0.v20180420-1519.jar;\.m2\repository\com\sparkjava\spark-core\2.2\spark-core-2.2.jar;\.m2\repository\org\slf4j\slf4j-api\1.7.7\slf4j-api-1.7.7.jar;\.m2\repository\org\slf4j\slf4j-simple\1.7.7\slf4j-simple-1.7.7.jar;\.m2\repository\org\eclipse\jetty\jetty-server\9.0.2.v20130417\jetty-server-9.0.2.v20130417.jar;\.m2\repository\org\eclipse\jetty\orbit\javax.servlet\3.0.0.v201112011016\javax.servlet-3.0.0.v201112011016.jar;\.m2\repository\org\eclipse\jetty\jetty-http\9.0.2.v20130417\jetty-http-9.0.2.v20130417.jar;\.m2\repository\org\eclipse\jetty\jetty-util\9.0.2.v20130417\jetty-util-9.0.2.v20130417.jar;\.m2\repository\org\eclipse\jetty\jetty-io\9.0.2.v20130417\jetty-io-9.0.2.v20130417.jar;\.m2\repository\org\eclipse\jetty\jetty-webapp\9.0.2.v20130417\jetty-webapp-9.0.2.v20130417.jar;\.m2\repository\org\eclipse\jetty\jetty-xml\9.0.2.v20130417\jetty-xml-9.0.2.v20130417.jar;\.m2\repository\org\eclipse\jetty\jetty-servlet\9.0.2.v20130417\jetty-servlet-9.0.2.v20130417.jar;\.m2\repository\org\eclipse\jetty\jetty-security\9.0.2.v20130417\jetty-security-9.0.2.v20130417.jar;\.m2\repository\com\google\code\gson\gson\2.2.4\gson-2.2.4.jar;\.m2\repository\org\postgresql\postgresql\42.2.20\postgresql-42.2.20.jar;\.m2\repository\org\checkerframework\checker-qual\3.5.0\checker-qual-3.5.0.jar" app.Main

#expose a port
EXPOSE 4567
