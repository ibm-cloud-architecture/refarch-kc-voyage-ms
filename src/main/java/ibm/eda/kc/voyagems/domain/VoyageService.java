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

    public Voyage getReeferById(String id) {
        return repository.getById(id);
    }

  
    public List<Voyage> getAllReefers() {
        return repository.getAll();
    }

    public Voyage saveReefer(Voyage r){
        repository.addVoyage(r);
        return r;
    }

    public Voyage updateFreezer(Voyage newFreezer) {
        return repository.updateVoyage(newFreezer);
    }
}
