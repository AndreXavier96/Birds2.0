package domains;

public class Couples {
	private Integer Id;
	private Bird Male;
	private Bird Female;
	private Cage Cage;
	private String State;
	
	public Couples() {
		super();
	}

	public Couples(Integer id, Bird male, Bird female,Cage cage,String state) {
		super();
		Id = id;
		Male = male;
		Female = female;
		Cage = cage;
		State = state;
	}

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public Bird getMale() {
		return Male;
	}

	public void setMale(Bird male) {
		Male = male;
	}

	public Bird getFemale() {
		return Female;
	}

	public void setFemale(Bird female) {
		Female = female;
	}
	
	public Cage getCage() {
		return Cage;
	}

	public void setCage(Cage cage) {
		Cage = cage;
	}

	public String getState() {
		return State;
	}

	public void setState(String state) {
		State = state;
	}

	@Override
	public String toString() {
		return "Couples [Id=" + Id + ", Male=" + Male + ", Female=" + Female + ", Cage=" + Cage + ", State=" + State
				+ "]";
	}
	
	
}
