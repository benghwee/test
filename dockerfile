# Use a Java runtime as the base
FROM eclipse-temurin:17

ENV KAFKA_HOME=/opt/kafka
ENV VERSION="7.9.4"
ENV ARCHIVE="confluent-${VERSION}.tar.gz"
ENV URL="https://packages.confluent.io/archive/7.9/${ARCHIVE}"
ENV DIR="${KAFKA_HOME}/confluent-${VERSION}"
ENV CLUSTER_ID="MkU3OEVBNTcwNTJENDM2Qk"


ENV KAFKA_PID="kafka.pid"
ENV SCHEMA_PID="schema-registry.pid"

ENV KAFKA_LOG="kafka.log"
ENV SCHEMA_LOG="schema-registry.log"

RUN mkdir -p ${KAFKA_HOME}

# Download and extract
WORKDIR ${KAFKA_HOME}

RUN curl -L -o ${ARCHIVE} ${URL} \
 && tar -xzf ${ARCHIVE} \
 && rm ${ARCHIVE}

ENV KAFKA_CONFIG=${DIR}/etc/kafka/kraft/server.properties
ENV SCHEMA_CONFIG=${DIR}/etc/schema-registry/schema-registry.properties

COPY kafka-server-start.sh /opt/kafka/kafka-server-start.sh
RUN sed -i 's/\r$//' /opt/kafka/kafka-server-start.sh \
 && chmod +x /opt/kafka/kafka-server-start.sh

RUN chmod +x /opt/kafka/kafka-server-start.sh

CMD ["/opt/kafka/kafka-server-start.sh"]