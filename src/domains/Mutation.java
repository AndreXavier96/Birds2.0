package domains;

public class Mutation {
	private Integer Id;
	private String Name;
	private String Var1;
	private String Var2;
	private String Var3;
	private String Obs;
	private Specie specie;
	
	public Mutation(Integer id, String name, String var1, String var2, String var3, String obs, Specie specie) {
		super();
		Id = id;
		Name = name;
		Var1 = var1;
		Var2 = var2;
		Var3 = var3;
		Obs = obs;
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
	public String getVar1() {
		return Var1;
	}
	public void setVar1(String var1) {
		Var1 = var1;
	}
	public String getVar2() {
		return Var2;
	}
	public void setVar2(String var2) {
		Var2 = var2;
	}
	public String getVar3() {
		return Var3;
	}
	public void setVar3(String var3) {
		Var3 = var3;
	}
	public String getObs() {
		return Obs;
	}
	public void setObs(String obs) {
		Obs = obs;
	}
	public Specie getSpecie() {
		return specie;
	}
	public void setSpecie(Specie specie) {
		this.specie = specie;
	}
}
