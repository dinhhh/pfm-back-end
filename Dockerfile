FROM maven:3.6.3-openjdk-17
WORKDIR .
COPY . .
RUN mvn clean install -Dmaven.test.skip
CMD sh scripts/start-server.sh