FROM openjdk:8-jre-alpine
VOLUME /tmp
ARG JAR_FILE
COPY ./target/${JAR_FILE} app.jar
COPY ./preprocessing.py /py_code/preprocessing.py
COPY ./ssh /root/.ssh
RUN chmod 0600 /root/.ssh/id_rsa
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
