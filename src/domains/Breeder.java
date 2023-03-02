package domains;

import java.util.HashMap;
import java.util.List;

public class Breeder {
	private Integer Id;
	private Integer CC;
	private String Name;
	private Integer Nif;
	private Integer Cellphone;
	private String Email;
	private String PostalCode;
	private String Locale;
	private String District;
	private String Address;
	private String Type;
	private List<Club> Club;
	private HashMap<Integer, String> Stam; //<FederationId,Stam>
	
	
	public Breeder() {
		super();
	}
	
	public Breeder(String address,Integer id, Integer cC, String name, Integer nif, Integer cellphone, String email, String postalCode,
			String locale, String district, String type, List<Club> club, HashMap<Integer, String> stam) {
		super();
		Id = id;
		CC = cC;
		Name = name;
		Nif = nif;
		Cellphone = cellphone;
		Email = email;
		PostalCode = postalCode;
		Locale = locale;
		District = district;
		Type = type;
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
	public Integer getCC() {
		return CC;
	}
	public void setCC(Integer cC) {
		CC = cC;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public Integer getNif() {
		return Nif;
	}
	public void setNif(Integer nif) {
		Nif = nif;
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
	public String getPostalCode() {
		return PostalCode;
	}
	public void setPostalCode(String postalCode) {
		PostalCode = postalCode;
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
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
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
		return "Breeder [Id=" + Id + ", CC=" + CC + ", Name=" + Name + ", Nif=" + Nif + ", Cellphone=" + Cellphone
				+ ", Email=" + Email + ", PostalCode=" + PostalCode + ", Locale=" + Locale+", Address="+Address + ", District=" + District
				+ ", Type=" + Type + ", Club=" + Club + ", Stam=" + Stam + "]";
	}
	
	
}
