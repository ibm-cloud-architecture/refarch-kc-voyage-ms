package ibm.eda.kc.voyagems.infra.repo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;

import com.fasterxml.jackson.databind.ObjectMapper;

import ibm.eda.kc.voyagems.domain.Voyage;

@ApplicationScoped
public class VoyageRepositoryMem implements VoyageRepository {
    public  static ConcurrentHashMap<String,List<Voyage>> byDeparturePort = new ConcurrentHashMap<String,List<Voyage>>();
    private ConcurrentHashMap<String, Voyage> currentOrderBacklog = new ConcurrentHashMap<String,Voyage>();
    private static ConcurrentHashMap<String,Voyage> voyages = new ConcurrentHashMap<String,Voyage>();

    private static ObjectMapper mapper = new ObjectMapper();
    

    public VoyageRepositoryMem() {
        super();
        InputStream is = getClass().getClassLoader().getResourceAsStream("voyages.json");
        if (is == null) 
            throw new IllegalAccessError("file not found for voyage json");
        try {
            List<Voyage> currentDefinitions = mapper.readValue(is, mapper.getTypeFactory().constructCollectionType(List.class, Voyage.class));
            currentDefinitions.stream().forEach( (v) -> { 
                voyages.put(v.voyageID,v);
                if (v.departurePort != null) {
                    if (byDeparturePort.get(v.departurePort) == null) 
                        byDeparturePort.put(v.departurePort, new ArrayList<Voyage>());
                    byDeparturePort.get(v.departurePort).add(v);
                }
                
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        voyages.values().stream().forEach(v -> System.out.println(v.voyageID));
    }

    public List<Voyage> getAll(){
        return new ArrayList<Voyage>(voyages.values());
    }

    public Voyage addVoyage(Voyage entity) {
        if (entity.voyageID == null) {
            entity.voyageID = UUID.randomUUID().toString();
        }
        voyages.put(entity.voyageID, entity);
        return entity;
    }

    public Voyage updateVoyage(Voyage entity) {
        voyages.put(entity.voyageID, entity);
        return entity;
    }

    @Override
    public Voyage getById(String key) {
        return voyages.get(key);
    }

    /**
     * Search voyages from departure location that has support capacity
     * @return list of freezers support this expected catacity and at the expected location
     */
    public  Voyage  getVoyageForOrder(String transactionID,
                                String departure,
                                String destination,
                                long expectedCapacity) {
        List<Voyage> voyages = byDeparturePort.get(departure);
        if (voyages == null) return null;
        for (Voyage v : voyages) {
            if (v.arrivalPort.equals(destination)) {
                // add capacity management
                v.currentFreeCapacity -= expectedCapacity;
                return v;
            }
        }
        return null;
    }

    public  Voyage getVoyagesForTransaction(String transactionID) {
        return currentOrderBacklog.get(transactionID);
    }

    public void cleanTransaction(String transactionID , long capacity) {
        Voyage allocatedVoyage = this.currentOrderBacklog.get(transactionID);
        if (allocatedVoyage != null) {
                allocatedVoyage.currentFreeCapacity+=capacity;
        }
        this.currentOrderBacklog.remove(transactionID);
    }
}
