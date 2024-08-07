package domains;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Specie {
	private Integer Id;
	private String CommonName;
	private String ScientificName;
	private Integer IncubationDays;
	private Integer DaysToBand;
	private Integer OutofCageAfterDays;
	private Integer MaturityAfterDays;
	private Integer BandSize;
	
	public Specie() {
		super();
	}

	public Specie(Integer id, String commonName, String scientificName, Integer incubationDays, Integer daysToBand,
			Integer outofCageAfterDays, Integer maturityAfterDays,Integer bandSize) {
		super();
		Id = id;
		CommonName = commonName;
		ScientificName = scientificName;
		IncubationDays = incubationDays;
		DaysToBand = daysToBand;
		OutofCageAfterDays = outofCageAfterDays;
		MaturityAfterDays = maturityAfterDays;
		BandSize=bandSize;
	}
}
