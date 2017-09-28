FROM tomcat:8.5.20-jre8


# The folowing will be used when the app is in production mode
# Currently, it's easier to just map the tomcat running directory
# to the development machine

RUN apt-get update && apt-get install -y --no-install-recommends \
		maven \
		openjdk-8-jdk

RUN mkdir /code
WORKDIR /code

ADD pom.xml /code/

RUN mvn verify clean --fail-never

ADD . /code/

RUN mvn package

RUN rm -rf /usr/local/tomcat/webapps/ROOT*

RUN cp target/facebroke.war /usr/local/tomcat/webapps/ROOT.war

CMD ["catalina.sh", "run"]
