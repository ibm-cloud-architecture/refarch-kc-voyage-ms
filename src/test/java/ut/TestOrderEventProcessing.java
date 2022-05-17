package ut;

import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;

import ibm.eda.kc.voyagems.infra.events.order.OrderCreatedEvent;
import ibm.eda.kc.voyagems.infra.events.order.OrderEvent;
import ibm.eda.kc.voyagems.infra.events.order.OrderEventDeserializer;
import ibm.eda.kc.voyagems.infra.events.voyage.VoyageEvent;
import ibm.eda.kc.voyagems.infra.events.voyage.VoyageEventDeserializer;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.kafka.InjectKafkaCompanion;
import io.quarkus.test.kafka.KafkaCompanionResource;
import io.smallrye.reactive.messaging.kafka.companion.ConsumerTask;
import io.smallrye.reactive.messaging.kafka.companion.KafkaCompanion;

@QuarkusTest
@QuarkusTestResource(KafkaCompanionResource.class)
public class TestOrderEventProcessing {
    @InjectKafkaCompanion 
    KafkaCompanion companion;

    @Test
    public void sendOrderCreated(){
        companion.registerSerde(OrderEvent.class, 
                    new io.quarkus.kafka.client.serialization.ObjectMapperSerializer<OrderEvent>(), 
                    new OrderEventDeserializer());
        companion.registerSerde(VoyageEvent.class, 
                    new io.quarkus.kafka.client.serialization.ObjectMapperSerializer<VoyageEvent>(), 
                    new VoyageEventDeserializer());
        OrderCreatedEvent oce = new OrderCreatedEvent("Shanghai","San Francisco");

        OrderEvent oe = new OrderEvent(OrderEvent.ORDER_CREATED_TYPE,oce);
        oe.orderID = "Test01";
        oe.quantity = 80;
        List<ProducerRecord<String,OrderEvent>> records = new ArrayList<ProducerRecord<String,OrderEvent>>();
        records.add(new ProducerRecord<String,OrderEvent>("orders",oe.orderID,oe));
        companion.produce(String.class, OrderEvent.class).fromRecords(records);

        ConsumerTask<String,VoyageEvent> voyageEvent = companion.consume(String.class, VoyageEvent.class).fromTopics("voyages", 10);
        voyageEvent.awaitCompletion();
        ConsumerRecord<String,VoyageEvent> reeferEventRecord = voyageEvent.getFirstRecord();
        System.out.println("Voyage --> " + reeferEventRecord.value().voyageID);
    }

}
