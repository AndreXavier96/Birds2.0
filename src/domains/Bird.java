package domains;

import java.util.Date;

public class Bird {
	private Integer id;
	private Breeder NrBreeder;
	private String Band;
	private Integer Year;
	private Date EntryDate;
	private String EntryType;
	private Double BuyPrice;
	private Double SellPrice;
	private String State;
	private String Sex;
	private Bird Father;
	private Bird Mother;
	private Specie Species;
	private Mutation Mutations;
	private Cage Cage;
	private Integer Breeder;
	private Integer Posture;

	

	public Bird(Integer id, domains.Breeder nrBreeder, String band, Integer year, Date entryDate, String entryType,
			Double buyPrice, Double sellPrice, String state, String sex, Bird father, Bird mother, Specie species,
			Mutation mutations, domains.Cage cage, Integer breeder, Integer posture) {
		super();
		this.id = id;
		NrBreeder = nrBreeder;
		Band = band;
		Year = year;
		EntryDate = entryDate;
		EntryType = entryType;
		BuyPrice = buyPrice;
		SellPrice = sellPrice;
		State = state;
		Sex = sex;
		Father = father;
		Mother = mother;
		Species = species;
		Mutations = mutations;
		Cage = cage;
		Breeder = breeder;
		Posture = posture;
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

	public Breeder getNrBreeder() {
		return NrBreeder;
	}

	public void setNrBreeder(Breeder nrBreeder) {
		NrBreeder = nrBreeder;
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

	public Double getSellPrice() {
		return SellPrice;
	}

	public void setSellPrice(Double sellPrice) {
		SellPrice = sellPrice;
	}

	public String getState() {
		return State;
	}

	public void setState(String state) {
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

	public Integer getBreeder() {
		return Breeder;
	}

	public void setBreeder(Integer breeder) {
		Breeder = breeder;
	}

	public Integer getPosture() {
		return Posture;
	}

	public void setPosture(Integer posture) {
		Posture = posture;
	}

	@Override
	public String toString() {
		return "Bird [id=" + id + ", NrBreeder=" + NrBreeder + ", Band=" + Band + ", Year=" + Year + ", EntryDate="
				+ EntryDate + ", EntryType=" + EntryType + ", BuyPrice=" + BuyPrice + ", SellPrice=" + SellPrice
				+ ", State=" + State + ", Sex=" + Sex + ", Father=" + Father + ", Mother=" + Mother + ", Species="
				+ Species + ", Mutations=" + Mutations + ", Cage=" + Cage + ", Breeder=" + Breeder + ", Posture="
				+ Posture + "]";
	}

	

	

}
