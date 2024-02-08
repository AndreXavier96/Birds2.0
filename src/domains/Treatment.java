package domains;

public class Treatment {
	private Integer id;
	private String Name;
	private String Description;
	private Integer Frequency;
	private String FrequencyType;
	private Integer DurationDays;

	public Treatment() {
		super();
	}

	public Treatment(Integer id, String name, String description, Integer frequency, String frequencyType,
			Integer durationDays) {
		super();
		this.id = id;
		Name = name;
		Description = description;
		Frequency = frequency;
		FrequencyType = frequencyType;
		DurationDays = durationDays;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public Integer getFrequency() {
		return Frequency;
	}

	public void setFrequency(Integer frequency) {
		Frequency = frequency;
	}

	public String getFrequencyType() {
		return FrequencyType;
	}

	public void setFrequencyType(String frequencyType) {
		FrequencyType = frequencyType;
	}

	public Integer getDurationDays() {
		return DurationDays;
	}

	public void setDurationDays(Integer durationDays) {
		DurationDays = durationDays;
	}
	
}
