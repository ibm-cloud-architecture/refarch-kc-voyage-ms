package ibm.eda.kc.voyagems.infra.events.order;

import java.util.concurrent.CompletionStage;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import ibm.eda.kc.voyagems.domain.Voyage;
import ibm.eda.kc.voyagems.infra.events.voyage.VoyageAllocated;
import ibm.eda.kc.voyagems.infra.events.voyage.VoyageEvent;
import ibm.eda.kc.voyagems.infra.events.voyage.VoyageEventProducer;
import ibm.eda.kc.voyagems.infra.repo.VoyageRepository;

/**
 * Listen to the orders topic and processes event from order service:
 * - order created event
 * - order cancelled event
 * Normally it should also support order updated event and recompute capacity
 */
@ApplicationScoped
public class OrderAgent {
    Logger logger = Logger.getLogger(OrderAgent.class.getName());

    @Inject
    VoyageRepository repo;

    @Inject
    VoyageEventProducer voyageEventProducer;

    @Incoming("orders")
    public CompletionStage<Void> processOrder(Message<OrderEvent> messageWithOrderEvent){
        logger.info("Received order : " + messageWithOrderEvent.getPayload().orderID);
        OrderEvent oe = messageWithOrderEvent.getPayload();
        switch( oe.getType()){
            case OrderEvent.ORDER_CREATED_TYPE:
                processOrderCreatedEvent(oe);
                break;
            case OrderEvent.ORDER_UPDATED_TYPE:
                logger.info("Receive order update " + oe.status);
                if (oe.status.equals(OrderEvent.ORDER_ON_HOLD_TYPE)) {
                    compensateOrder(oe.orderID,oe.quantity);
                } else {
                    logger.info("Do future processing in case of order update");
                }
                    
                break;
            default:
                break;
        }
        return messageWithOrderEvent.ack();
    }

    /**
     * When order created, search for voyage close to the pickup location, and a distination close
     * for a given date
     */
    public VoyageEvent processOrderCreatedEvent( OrderEvent oe){
        OrderCreatedEvent oce = (OrderCreatedEvent)oe.payload;
        Voyage voyage = repo.getVoyageForOrder(oe.orderID, 
                                oce.pickupCity, 
                                oce.destinationCity,
                                oe.quantity);
        VoyageEvent ve = new VoyageEvent();
        if (voyage == null) {
            // normally do nothing
            logger.info("No voyage found for " + oce.pickupCity);
        } else {
            VoyageAllocated voyageAssignedEvent = new VoyageAllocated(oe.orderID);
            ve.voyageID = voyage.voyageID;
            ve.setType(VoyageEvent.TYPE_VOYAGE_ASSIGNED);
            ve.payload = voyageAssignedEvent;
           
            voyageEventProducer.sendEvent(ve.voyageID,ve);
        }
        return ve;
    }
 
    public void compensateOrder(String txid,long capacity) {
        logger.info("Compensate on order " + txid);
        repo.cleanTransaction(txid,capacity);
    }
}
