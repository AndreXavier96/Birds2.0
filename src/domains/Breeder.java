package domains;

import java.util.HashMap;
import java.util.List;

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
	
	public Breeder() {
		super();
	}
	
	public Breeder(String address, Integer id, String name, Integer cellphone, String email, String locale,
			String district, List<Club> club, HashMap<Integer, String> stam) {
		super();
		Id = id;
		Name = name;
		Cellphone = cellphone;
		Email = email;
		Locale = locale;
		District = district;
		Club = club;
		Stam = stam;
		Address = address;
	}
	
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public Integer getCellphone() {
		return Cellphone;
	}
	public void setCellphone(Integer cellphone) {
		Cellphone = cellphone;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public String getLocale() {
		return Locale;
	}
	public void setLocale(String locale) {
		Locale = locale;
	}
	public String getDistrict() {
		return District;
	}
	public void setDistrict(String district) {
		District = district;
	}
	public List<Club> getClub() {
		return Club;
	}
	public void setClub(List<Club> club) {
		Club = club;
	}
	public HashMap<Integer, String> getStam() {
		return Stam;
	}
	public void setStam(HashMap<Integer, String> stam) {
		Stam = stam;
	}
	
	@Override
	public String toString() {
		return "Breeder [Id=" + Id + ", Name=" + Name + ", Cellphone=" + Cellphone
				+ ", Email=" + Email  + ", Locale=" + Locale+", Address="+Address + ", District=" + District
				 + ", Club=" + Club + ", Stam=" + Stam + "]";
	}
	
	
}
