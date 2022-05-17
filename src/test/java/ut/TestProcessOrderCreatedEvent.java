package ut;



import javax.inject.Inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ibm.eda.kc.voyagems.domain.Voyage;
import ibm.eda.kc.voyagems.infra.events.order.OrderAgent;
import ibm.eda.kc.voyagems.infra.events.order.OrderCreatedEvent;
import ibm.eda.kc.voyagems.infra.events.order.OrderEvent;
import ibm.eda.kc.voyagems.infra.events.voyage.VoyageAllocated;
import ibm.eda.kc.voyagems.infra.events.voyage.VoyageEvent;
import ibm.eda.kc.voyagems.infra.events.voyage.VoyageEventDeserializer;
import ibm.eda.kc.voyagems.infra.repo.VoyageRepository;
import io.quarkus.kafka.client.serialization.ObjectMapperSerializer;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class TestProcessOrderCreatedEvent {
    
    @Inject
    OrderAgent agent;

    @Inject
    VoyageRepository repo;

    @Test
    public void shouldHaveOneVoyage(){
        Voyage v = repo.getVoyageForOrder("T01", "San Francisco", "Shanghai",100);
        Assertions.assertNotNull(v);
        Assertions.assertEquals("V001", v.voyageID);
        v = repo.getVoyageForOrder("T01", "Boston", "Shanghai",100);
        Assertions.assertEquals("V005", v.voyageID);
    }

    @Test
    public void shouldBeAbleToCast(){
        VoyageAllocated oce = new VoyageAllocated("Order01");

        VoyageEvent oe = new VoyageEvent("V001");
        oe.payload = oce;
        ObjectMapperSerializer<VoyageEvent> mapper = new ObjectMapperSerializer<VoyageEvent>();
        byte[] inMessage = mapper.serialize("voyages", oe);
        VoyageEventDeserializer deserialize = new VoyageEventDeserializer();
        VoyageEvent oe2 = deserialize.deserialize("voyages", inMessage);
        VoyageAllocated oce2 = (VoyageAllocated)oe2.payload;
        Assertions.assertEquals("Order01", oce2.orderID);
        mapper.close();
        deserialize.close();
    }

    @Test
    public void orderCreatedEvent(){

        OrderCreatedEvent oce = new OrderCreatedEvent("Shanghai","San Francisco");

        OrderEvent oe = new OrderEvent(OrderEvent.ORDER_CREATED_TYPE,oce);
        oe.orderID = "Test01";
        oe.quantity = 80;
        VoyageEvent ve = agent.processOrderCreatedEvent(oe);
        Assertions.assertEquals(VoyageEvent.TYPE_VOYAGE_ASSIGNED,ve.getType());
        Assertions.assertNotNull(ve.payload);
        Assertions.assertEquals(oe.orderID,((VoyageAllocated)ve.payload).orderID);
        Assertions.assertNotNull(ve.voyageID);
        System.out.println("voyage -> " + ve.voyageID);
        Assertions.assertEquals("V001", ve.voyageID);
    }
}
