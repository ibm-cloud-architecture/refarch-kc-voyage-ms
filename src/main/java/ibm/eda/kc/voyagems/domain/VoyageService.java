package ibm.eda.kc.voyagems.domain;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import ibm.eda.kc.voyagems.infra.repo.VoyageRepository;

@ApplicationScoped
public class VoyageService {
    private static Logger logger = Logger.getLogger("VoyageService");

    @Inject
    public VoyageRepository repository;

    public VoyageService(VoyageRepository repo) {
        this.repository = repo;
    }

    public Voyage getVoyageById(String id) {
        return repository.getById(id);
    }

  
    public List<Voyage> getAllVoyages() {
        return repository.getAll();
    }

    public Voyage saveVoyage(Voyage r){
        repository.addVoyage(r);
        return r;
    }

    public Voyage updateVoyage(Voyage newVoyage) {
        return repository.updateVoyage(newVoyage);
    }

    public Voyage getAllVoyagesForTransaction(String txid) {
        return repository.getVoyagesForTransaction(txid);
    }

}
