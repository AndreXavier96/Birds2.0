package domains;

import lombok.Data;

@Data
public class Cage {
	private Integer id;
	private String Code;
	private String Type;

	@Override
	public String toString() {
		return "Codigo " + Code;
	}
	
	
}
