package domains;

public class Federation {
	private Integer id;
	private String Name;
	private String Acronym;
	private String Country;
	private String Email;
	
	public Federation() {super();}

	public Federation(Integer id, String name, String acronym, String country, String email) {
		super();
		this.id = id;
		Name = name;
		Acronym = acronym;
		Country = country;
		Email = email;
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

	public String getAcronym() {
		return Acronym;
	}

	public void setAcronym(String acronym) {
		Acronym = acronym;
	}

	public String getCountry() {
		return Country;
	}

	public void setCountry(String country) {
		Country = country;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}
	
}
