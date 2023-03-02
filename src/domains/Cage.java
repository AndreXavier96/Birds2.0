package domains;

public class Cage {
	private Integer id;
	private String Code;
	private String Type;

	public Cage() {
		super();
	}

	public Cage(Integer id, String code, String type) {
		super();
		this.id = id;
		Code = code;
		Type = type;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return Code;
	}

	public void setCode(String code) {
		Code = code;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	@Override
	public String toString() {
		return "Codigo " + Code;
	}
	
	
}
