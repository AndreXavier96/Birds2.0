package domains;

import lombok.Data;

@Data
public class Couples {
	private Integer Id;
	private Bird Male;
	private Bird Female;
	private Cage Cage;
	private String State;

	@Override
	public String toString() {
		return "Couples [Id=" + Id + ", Male=" + Male + ", Female=" + Female + ", Cage=" + Cage + ", State=" + State
				+ "]";
	}
}
