package domains;

import java.util.Date;

public class Bird {
	private Integer id;
	private String Band;
	private Integer Year;
	private Date EntryDate;
	private String EntryType;
	private Double BuyPrice;
	private State State;
	private String Sex;
	private Bird Father;
	private Bird Mother;
	private Specie Species;
	private Mutation Mutations;
	private Cage Cage;
	private Breeder Breeder;
	private Integer Posture;
	private String Image;
	private String Obs;

	public Bird(Integer id, Breeder nrBreeder, String band, Integer year, Date entryDate, String entryType,
			Double buyPrice, State state, String sex, Bird father, Bird mother, Specie species,
			Mutation mutations, Cage cage, Integer posture,String image,String obs) {
		super();
		this.id = id;
		Breeder = nrBreeder;
		Band = band;
		Year = year;
		EntryDate = entryDate;
		EntryType = entryType;
		BuyPrice = buyPrice;
		State = state;
		Sex = sex;
		Father = father;
		Mother = mother;
		Species = species;
		Mutations = mutations;
		Cage = cage;
		Posture = posture;
		Image=image;
		Obs=obs;
	}

	public Bird() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Breeder getBreeder() {
		return Breeder;
	}

	public void setBreeder(Breeder nrBreeder) {
		Breeder = nrBreeder;
	}

	public String getBand() {
		return Band;
	}

	public void setBand(String band) {
		Band = band;
	}

	public Integer getYear() {
		return Year;
	}

	public void setYear(Integer year) {
		Year = year;
	}

	public Date getEntryDate() {
		return EntryDate;
	}

	public void setEntryDate(Date entryDate) {
		EntryDate = entryDate;
	}

	public String getEntryType() {
		return EntryType;
	}

	public void setEntryType(String entryType) {
		EntryType = entryType;
	}

	public Double getBuyPrice() {
		return BuyPrice;
	}

	public void setBuyPrice(Double buyPrice) {
		BuyPrice = buyPrice;
	}

	public State getState() {
		return State;
	}

	public void setState(State state) {
		State = state;
	}

	public String getSex() {
		return Sex;
	}

	public void setSex(String sex) {
		Sex = sex;
	}

	public Bird getFather() {
		return Father;
	}

	public void setFather(Bird father) {
		Father = father;
	}

	public Bird getMother() {
		return Mother;
	}

	public void setMother(Bird mother) {
		Mother = mother;
	}

	public Specie getSpecies() {
		return Species;
	}

	public void setSpecies(Specie species) {
		Species = species;
	}

	public Mutation getMutations() {
		return Mutations;
	}

	public void setMutations(Mutation mutations) {
		Mutations = mutations;
	}

	public Cage getCage() {
		return Cage;
	}

	public void setCage(Cage cage) {
		Cage = cage;
	}

	public Integer getPosture() {
		return Posture;
	}

	public void setPosture(Integer posture) {
		Posture = posture;
	}
	
	public String getImage() {
		return Image;
	}

	public void setImage(String image) {
		Image = image;
	}
	
	public String getObs() {
		return Obs;
	}

	public void setObs(String obs) {
		Obs = obs;
	}
	@Override
	public String toString() {
		return "Bird [id=" + id + ", Band=" + Band + ", Year=" + Year + ", EntryDate=" + EntryDate + ", EntryType="
				+ EntryType + ", BuyPrice=" + BuyPrice + ", State=" + State + ", Sex="
				+ Sex + ", Father=" + Father + ", Mother=" + Mother + ", Species=" + Species + ", Mutations="
				+ Mutations + ", Cage=" + Cage + ", Breeder=" + Breeder + ", Posture=" + Posture + ", Image=" + Image+ "]";
	}
	

}
