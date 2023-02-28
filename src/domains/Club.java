package domains;

public class Club {
	private Integer id;
	private String Name;
	private String Acronym;
	private String Locale;
	private String Address;
	private String Phone;
	private String Email;
	private Federation federation;
	
	public Club() {super();}

	public Club(Integer id, String name, String acronym, String locale, String address, String phone, String email,
			Federation federation) {
		super();
		this.id = id;
		Name = name;
		Acronym = acronym;
		Locale = locale;
		Address = address;
		Phone = phone;
		Email = email;
		this.federation = federation;
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

	public String getLocale() {
		return Locale;
	}

	public void setLocale(String locale) {
		Locale = locale;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String phone) {
		Phone = phone;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public Federation getFederation() {
		return federation;
	}

	public void setFederation(Federation federation) {
		this.federation = federation;
	}

	@Override
	public String toString() {
		return "[" + Acronym + "] "+Name;
	}

	
}
