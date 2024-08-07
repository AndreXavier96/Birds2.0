package domains;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

	@Override
	public String toString() {
		return "Bird [id=" + id + ", Band=" + Band + ", Year=" + Year + ", EntryDate=" + EntryDate + ", EntryType="
				+ EntryType + ", BuyPrice=" + BuyPrice + ", State=" + State + ", Sex="
				+ Sex + ", Father=" + Father + ", Mother=" + Mother + ", Species=" + Species + ", Mutations="
				+ Mutations + ", Cage=" + Cage + ", Breeder=" + Breeder + ", Posture=" + Posture + ", Image=" + Image+ "]";
	}
	

}
