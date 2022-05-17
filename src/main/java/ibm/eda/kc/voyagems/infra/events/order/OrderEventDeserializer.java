package ibm.eda.kc.voyagems.infra.events.order;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class OrderEventDeserializer extends ObjectMapperDeserializer<OrderEvent> {
    public OrderEventDeserializer(){
        // pass the class to the parent.
        super(OrderEvent.class);
    }
    
}
