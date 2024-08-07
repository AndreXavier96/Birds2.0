package domains;

import java.util.HashMap;
import java.util.List;

import lombok.Data;

@Data
public class Breeder {
	private Integer Id;
	private String Name;
	private Integer Cellphone;
	private String Email;
	private String Locale;
	private String District;
	private String Address;
	private List<Club> Club;
	private HashMap<Integer, String> Stam; //<FederationId,Stam>
	
	@Override
	public String toString() {
		return "Breeder [Id=" + Id + ", Name=" + Name + ", Cellphone=" + Cellphone
				+ ", Email=" + Email  + ", Locale=" + Locale+", Address="+Address + ", District=" + District
				 + ", Club=" + Club + ", Stam=" + Stam + "]";
	}
	
	
}
