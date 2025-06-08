package com.himanshu.kafkabinder;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.TopicExistsException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
public class KafkaDeleteRecords {

    public void deleteRecord(String topicName){
        String bootstrapServers =  "localhost:9092";
        //String topicName = "<topic-name>";
        int partition = 0; // specify the partition
        long offset = 0;   // specify the offset to which records should be deleted

        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        try (AdminClient adminClient = AdminClient.create(props)) {
            // Define the offset to delete up to
            Map<TopicPartition, RecordsToDelete> offsetsToDelete = new HashMap<>();
            offsetsToDelete.put(new TopicPartition(topicName, partition), RecordsToDelete.beforeOffset(offset));

            // Request to delete records up to the specified offsets
            DeleteRecordsResult result = adminClient.deleteRecords(offsetsToDelete);

            // Check if the deletion was successful
            result.all().get();
            System.out.println("Records deleted up to offset " + offset);
        } catch (TopicExistsException e) {
            System.err.println("Topic does not exist: " + e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            System.err.println("Failed to delete records: " + e.getMessage());
        }
    }
}
