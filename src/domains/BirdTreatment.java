package domains;

import java.util.Date;

public class BirdTreatment {
    private Bird bird;
    private Treatment treatment;
    private Date start;
    
	public BirdTreatment(Bird bird, Treatment treatment, Date start) {
		super();
		this.bird = bird;
		this.treatment = treatment;
		this.start = start;
	}

	public BirdTreatment() {
		super();
	}

	public Bird getBird() {
		return bird;
	}

	public void setBird(Bird bird) {
		this.bird = bird;
	}

	public Treatment getTreatment() {
		return treatment;
	}

	public void setTreatment(Treatment treatment) {
		this.treatment = treatment;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

   
}

