package domains;

import java.util.Date;
import java.util.List;

import constants.MyValues;
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

	@Override
	public String toString() {
		return "Ninhada:" + " Pai: " + (Father != null ? Father.getBand() : "N/A") + ", Mae: "
				+ (Mother != null ? Mother.getBand() : "N/A") + ", inicio ninhada: "
				+ (Start != null ? Start.toString() : "N/A");
	}

	public int getTotalEggs() {
		return Eggs.size();
	}

	public int getChocadoEggsCount() {
		return (int) Eggs.stream()
				.filter(egg -> MyValues.CHOCADO.equals(egg.getStatute()) && egg.getBird() == null)
				.count();
	}

}
