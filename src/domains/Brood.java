package domains;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class Brood {
	private Integer Id;
	private List<Egg> Eggs;
	private Date Start;
	private Date Finish;
	private Bird Father;
	private Bird Mother;
	private List<Bird> AdoptiveParents;
	private Cage Cage;
}
