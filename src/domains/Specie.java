package domains;

public class Specie {
	private Integer Id;
	private String CommonName;
	private String ScientificName;
	private Integer IncubationDays;
	private Integer DaysToBand;
	private Integer OutofCageAfterDays;
	private Integer MaturityAfterDays;
	
	public Specie() {
		super();
	}

	
	public Specie(Integer id, String commonName, String scientificName, Integer incubationDays, Integer daysToBand,
			Integer outofCageAfterDays, Integer maturityAfterDays) {
		super();
		Id = id;
		CommonName = commonName;
		ScientificName = scientificName;
		IncubationDays = incubationDays;
		DaysToBand = daysToBand;
		OutofCageAfterDays = outofCageAfterDays;
		MaturityAfterDays = maturityAfterDays;
	}


	public String getCommonName() {
		return CommonName;
	}
	public void setCommonName(String commonName) {
		CommonName = commonName;
	}
	public String getScientificName() {
		return ScientificName;
	}
	public void setScientificName(String scientificName) {
		ScientificName = scientificName;
	}
	public Integer getIncubationDays() {
		return IncubationDays;
	}
	public void setIncubationDays(Integer incubationDays) {
		IncubationDays = incubationDays;
	}
	public Integer getDaysToBand() {
		return DaysToBand;
	}
	public void setDaysToBand(Integer daysToBand) {
		DaysToBand = daysToBand;
	}
	public Integer getOutofCageAfterDays() {
		return OutofCageAfterDays;
	}
	public void setOutofCageAfterDays(Integer outofCageAfterDays) {
		OutofCageAfterDays = outofCageAfterDays;
	}
	public Integer getMaturityAfterDays() {
		return MaturityAfterDays;
	}
	public void setMaturityAfterDays(Integer maturityAfterDays) {
		MaturityAfterDays = maturityAfterDays;
	}
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
}
