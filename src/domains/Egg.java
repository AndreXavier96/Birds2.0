package domains;

import java.util.Date;

public class Egg {
	private Integer Id;
	private Date postureDate;
	private Date verifiedFertilityDate;
	private Date outbreakDate;
	private String type;//fecundado desconhecido vazio
	private String statute;//chocado emDesemvolvimento partido desconhecido morteNoOvo AusenciaDeEmbriao
	private Bird bird;
	
	public Egg() {
		super();
	}

	public Egg(Integer id, Date postureDate, Date verifiedFertilityDate, Date outbreakDate, String type, String statute,
			Bird bird) {
		super();
		Id = id;
		this.postureDate = postureDate;
		this.verifiedFertilityDate = verifiedFertilityDate;
		this.outbreakDate = outbreakDate;
		this.type = type;
		this.statute = statute;
		this.bird = bird;
	}

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public Date getPostureDate() {
		return postureDate;
	}

	public void setPostureDate(Date postureDate) {
		this.postureDate = postureDate;
	}

	public Date getVerifiedFertilityDate() {
		return verifiedFertilityDate;
	}

	public void setVerifiedFertilityDate(Date verifiedFertilityDate) {
		this.verifiedFertilityDate = verifiedFertilityDate;
	}

	public Date getOutbreakDate() {
		return outbreakDate;
	}

	public void setOutbreakDate(Date outbreakDate) {
		this.outbreakDate = outbreakDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatute() {
		return statute;
	}

	public void setStatute(String statute) {
		this.statute = statute;
	}

	public Bird getBird() {
		return bird;
	}

	public void setBird(Bird bird) {
		this.bird = bird;
	}
	
	
	
}
