package ibm.eda.kc.voyagems.infra.repo;

import java.util.List;

import ibm.eda.kc.voyagems.domain.Voyage;

public interface VoyageRepository {
    public List<Voyage> getAll();
    public Voyage addVoyage(Voyage entity);
    public Voyage updateVoyage(Voyage entity);
    public Voyage getById(String key);
    public Voyage  getVoyageForOrder(String transactionID, String origin, String destination, long capacity);
    public  Voyage getVoyagesForTransaction(String transactionID);
    public void cleanTransaction(String transactionID, long capacity);
}
