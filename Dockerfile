FROM tomcat:8.5-alpine


RUN apk add --no-cache openjdk8 maven

RUN mkdir /code
WORKDIR /code

COPY pom.xml /code/

RUN mvn verify clean --fail-never

COPY . /code/

COPY server.xml /usr/local/tomcat/conf/
COPY context.xml /usr/local/tomcat/conf/

RUN mvn package

RUN rm -rf  /usr/local/tomcat/webapps/ROOT*

RUN cp target/facebroke.war /usr/local/tomcat/webapps/ROOT.war

CMD ["catalina.sh", "run"]
