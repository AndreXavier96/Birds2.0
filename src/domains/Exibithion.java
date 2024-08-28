package domains;

import lombok.Data;

@Data
public class Exibithion {
	private Integer id;
	private String Name;
	private String Locale;

	@Override
	public String toString() {
		return "Nome:" + Name+" Localidade:"+Locale;
	}
}
