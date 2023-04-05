package domains;

public class ClubFederation {
    private String clubAcronym;
    private String federationName;
    private String breederStam;

    public ClubFederation(String clubAcronym, String federationName, String breederStam) {
        this.clubAcronym = clubAcronym;
        this.federationName = federationName;
        this.breederStam = breederStam;
    }

    public String getClubAcronym() {
        return clubAcronym;
    }

    public String getFederationName() {
        return federationName;
    }

    public String getBreederStam() {
        return breederStam;
    }

}

