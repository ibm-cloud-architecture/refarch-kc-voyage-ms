package ibm.eda.kc.voyagems.infra.events.order;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

/**
 * The deduction-based polymorphism feature deduces subtypes based on the presence of properties 
 * distinct to a particular subtype
 * 
 */
@JsonTypeInfo(use=Id.DEDUCTION, defaultImpl = OrderCreatedEvent.class)
@JsonSubTypes({
    @Type(value=OrderCreatedEvent.class,name="OrderCreatedEvent"),
    @Type(value=OrderUpdatedEvent.class, name="OrderUpdatedEvent")})
public abstract class  OrderVariablePayload {

}

