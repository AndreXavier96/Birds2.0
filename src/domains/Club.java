package domains;

import lombok.Data;

@Data
public class Club {
	private Integer id;
	private String Name;
	private String Acronym;
	private String Locale;
	private String Address;
	private String Phone;
	private String Email;
	private Federation federation;

	@Override
	public String toString() {
		return "[" + Acronym + "] "+Name;
	}

	
}
