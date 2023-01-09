package domains;

public class Mutation {
	private Integer Id;
	private String Name;
	private String Type;
	private String Symbol;
	private Specie specie;
	
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
	
	
	public Mutation(Integer id, String name, String type, String symbol, Specie specie) {
		super();
		Id = id;
		Name = name;
		Type = type;
		Symbol = symbol;
		this.specie = specie;
	}
	public Mutation() {
		super();
	}
	public Specie getSpecie() {
		return specie;
	}
	public void setSpecie(Specie specie) {
		this.specie = specie;
	}
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	
}
