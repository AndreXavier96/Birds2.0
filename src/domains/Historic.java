package domains;

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
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getObs() {
		return obs;
	}
	public void setObs(String obs) {
		this.obs = obs;
	}
	
	public Bird getBird() {
		return bird;
	}
	public void setBird(Bird bird) {
		this.bird = bird;
	}
}
