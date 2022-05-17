package ibm.eda.kc.voyagems.infra.events.voyage;

import java.util.Date;

import ibm.eda.kc.voyagems.infra.events.EventBase;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class VoyageEvent extends EventBase {
    public static final String TYPE_VOYAGE_ASSIGNED = "VoyageAssigned"; // from voyage ms
    public static final String TYPE_VOYAGE_NOT_FOUND = "VoyageNotFound"; // from voyage ms
    public String voyageID;
    public VoyageVariablePayload payload;

    public VoyageEvent(String voi) {
        this.voyageID = voi;
        this.timestampMillis = new Date().getTime();
        this.version = DEFAULT_VERSION;
    }

    public VoyageEvent(long timestampMillis,
            String type,
            String version,
            VoyageVariablePayload payload) {
        super(timestampMillis, type, version);
        this.payload = payload;
    }

    public VoyageEvent(String aType, VoyageVariablePayload payload) {
        this.payload = payload;
        this.type = aType;
        this.timestampMillis = new Date().getTime();
        this.version = DEFAULT_VERSION;
    }

    public VoyageEvent() {
        this.timestampMillis = new Date().getTime();
        this.version = DEFAULT_VERSION;
    }
}
