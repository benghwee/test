#!/bin/sh

set -e

format_storage() {

  LOG_DIR=$(grep "^log.dirs=" "$KAFKA_CONFIG" | cut -d= -f2 | cut -d',' -f1)

  if [ -f "$LOG_DIR/meta.properties" ]; then
    echo "Kafka storage already formatted"
    return
  fi

  echo "Formatting Kafka storage..."

  "$DIR/bin/kafka-storage" format \
    -t "$CLUSTER_ID" \
    -c "$KAFKA_CONFIG"
}

start_kafka() {
  echo "Starting Kafka (KRaft)..."
  "$DIR/bin/kafka-server-start" "$KAFKA_CONFIG" &
  KAFKA_PID=$!
}

start_schema() {
  echo "Starting Schema Registry..."
  "$DIR/bin/schema-registry-start" "$SCHEMA_CONFIG" &
  SCHEMA_PID=$!
}

format_storage
start_kafka

echo "Waiting for Kafka to start..."
sleep 10

start_schema

echo "Kafka PID: $KAFKA_PID"
echo "Schema Registry PID: $SCHEMA_PID"

wait $KAFKA_PID