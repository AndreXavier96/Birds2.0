package domains;

public class Mutation {
	private Integer Id;
	private String Name;
	private String Type;
	private String Symbol;
	private String Observation;
	private Specie specie;


	public Mutation(Integer id, String name, String type, String symbol, String observation, Specie specie) {
		super();
		Id = id;
		Name = name;
		Type = type;
		Symbol = symbol;
		Observation = observation;
		this.specie = specie;
	}
	
	public Mutation() {
		super();
	}

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getSymbol() {
		return Symbol;
	}

	public void setSymbol(String symbol) {
		Symbol = symbol;
	}

	public String getObservation() {
		return Observation;
	}

	public void setObservation(String observation) {
		Observation = observation;
	}

	public Specie getSpecie() {
		return specie;
	}

	public void setSpecie(Specie specie) {
		this.specie = specie;
	}

	
	
}
