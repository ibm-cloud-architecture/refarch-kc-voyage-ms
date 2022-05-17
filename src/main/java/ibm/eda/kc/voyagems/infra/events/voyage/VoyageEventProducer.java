package ibm.eda.kc.voyagems.infra.events.voyage;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;

import io.smallrye.reactive.messaging.kafka.api.OutgoingKafkaRecordMetadata;

@ApplicationScoped
public class VoyageEventProducer {
	Logger logger = Logger.getLogger(VoyageEventProducer.class.getName());

    @Channel("voyages")
	public Emitter<VoyageEvent> eventProducer;


    public void sendEvent(String key, VoyageEvent voyageEvent){
		logger.info("Send voyage message --> " + voyageEvent.voyageID + " ts: " + voyageEvent.getTimestampMillis());
		eventProducer.send(Message.of(voyageEvent).addMetadata(OutgoingKafkaRecordMetadata.<String>builder()
			.withKey(key).build())
			.withAck( () -> {
				
				return CompletableFuture.completedFuture(null);
			})
			.withNack( throwable -> {
				return CompletableFuture.completedFuture(null);
			}));
	}

}
