package domains;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Historic {
	private Integer id;
	private String title;
	private String date;
	private String obs;
	private Bird bird;
	
	public Historic() {
		super();
	}
	
	public Historic(Integer id, String title, String date, String obs,Bird bird) {
		super();
		this.id = id;
		this.title = title;
		this.date = date;
		this.obs = obs;
		this.bird=bird;
	}
	
	public Historic( String title, String date, String obs,Bird bird) {
		this.title = title;
		this.date = date;
		this.obs = obs;
		this.bird=bird;
	}
}
