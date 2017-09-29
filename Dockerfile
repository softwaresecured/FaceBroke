FROM tomcat:8.5.20-jre8

RUN apt-get update && apt-get install -y --no-install-recommends \
		maven \
		openjdk-8-jdk

RUN mkdir /code
WORKDIR /code

COPY pom.xml /code/

RUN mvn verify clean --fail-never

COPY . /code/

RUN mvn package

RUN rm -rf  /usr/local/tomcat/webapps/ROOT*

RUN cp target/facebroke.war /usr/local/tomcat/webapps/ROOT.war

CMD ["catalina.sh", "run"]
