package com.himanshu.kafkabinder;
import org.apache.avro.Schema;
import org.apache.avro.SchemaParseException;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
public class AvroRecordFactory {

    public static GenericRecord createPersonRecord(String name, int age) {
        try {
            // Define the Avro schema for a Person record
            Schema.Parser parser = new Schema.Parser();
            Schema schema = parser.parse("{\"type\":\"record\",\"name\":\"Person\",\"fields\":[{\"name\":\"name\",\"type\":\"string\"},{\"name\":\"age\",\"type\":\"int\"}]}");

            // Create a GenericRecord instance with the defined schema
            GenericRecord record = new GenericData.Record(schema);
            record.put("name", name);
            record.put("age", age);

            return record;
        } catch (SchemaParseException e) {
            // Handle schema parsing errors
            e.printStackTrace();
            return null;
        }
    }
}
