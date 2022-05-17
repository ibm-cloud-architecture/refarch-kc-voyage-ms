package ibm.eda.kc.voyagems.infra.api;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ibm.eda.kc.voyagems.domain.Voyage;
import ibm.eda.kc.voyagems.domain.VoyageService;


@RequestScoped
@Path("/voyages")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VoyageResource {
    
    @Inject
    public VoyageService serv;
    
    @GET
    public List<Voyage> getAll() {
        return serv.getAllVoyages();
    }

    @POST
    public Voyage createVoyage(Voyage newVoyage) {
        return serv.saveVoyage(newVoyage);
    }

    @GET
    @Path("/transaction/{txid}")
    public Voyage getAllVoyagesForAtransaction(@PathParam("txid") String txid) {
        return serv.getAllVoyagesForTransaction(txid);
    }
}
