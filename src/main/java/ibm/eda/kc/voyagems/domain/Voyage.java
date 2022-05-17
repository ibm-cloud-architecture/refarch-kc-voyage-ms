package ibm.eda.kc.voyagems.domain;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Voyage {
   public static String ASSIGNED = "assign";

   public String voyageID;
   public String vesselID;
   public long capacity;
   public String type; 
   public String status;
   public String departurePort;
   public String arrivalPort;
   public long currentFreeCapacity;
   public String creationDate;

   public Voyage(){}

   public String toString(){
      return "Voyage: " + voyageID + " capacity: " + capacity + " status: " + status;
   }
}
