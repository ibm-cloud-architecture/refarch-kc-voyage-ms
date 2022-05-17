package ibm.eda.kc.voyagems.infra.events.voyage;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use=Id.DEDUCTION, defaultImpl = VoyageAllocated.class)
@JsonSubTypes({@Type(VoyageAllocated.class)})
public abstract class VoyageVariablePayload {
    
}
