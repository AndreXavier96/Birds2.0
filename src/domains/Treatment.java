package domains;

import lombok.Data;

@Data
public class Treatment {
	private Integer id;
	private String Name;
	private String Description;
	private Integer Frequency;
	private String FrequencyType;
	private Integer DurationDays;
	private Integer TimesAplied;
	private Integer BirdsTreated;
}
