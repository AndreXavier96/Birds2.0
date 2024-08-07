package domains;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClubFederation {
    private String clubAcronym;
    private String federationName;
    private String breederStam;

    public ClubFederation(String clubAcronym, String federationName, String breederStam) {
        this.clubAcronym = clubAcronym;
        this.federationName = federationName;
        this.breederStam = breederStam;
    }
}

