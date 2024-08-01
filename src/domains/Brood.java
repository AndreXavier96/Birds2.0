package domains;

import java.util.Date;
import java.util.List;

public class Brood {
	private Integer Id;
	private List<Egg> Eggs;
	private Date Start;
	private Date Finish;
	private Bird Father;
	private Bird Mother;
	private List<Bird> AdoptiveParents;
	private Cage Cage;
	
	public Brood() {
		super();
	}
	
	public Brood(Integer id, List<Egg> eggs, Date start, Date finish, Bird father, Bird mother,
			List<Bird> adoptiveParents, Cage cage) {
		super();
		Id = id;
		Eggs = eggs;
		Start = start;
		Finish = finish;
		Father = father;
		Mother = mother;
		AdoptiveParents = adoptiveParents;
		Cage = cage;
	}

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public List<Egg> getEggs() {
		return Eggs;
	}

	public void setEggs(List<Egg> eggs) {
		Eggs = eggs;
	}

	public Date getStart() {
		return Start;
	}

	public void setStart(Date start) {
		Start = start;
	}

	public Date getFinish() {
		return Finish;
	}

	public void setFinish(Date finish) {
		Finish = finish;
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

	public List<Bird> getAdoptiveParents() {
		return AdoptiveParents;
	}

	public void setAdoptiveParents(List<Bird> adoptiveParents) {
		AdoptiveParents = adoptiveParents;
	}

	public Cage getCage() {
		return Cage;
	}

	public void setCage(Cage cage) {
		Cage = cage;
	}

	
}
