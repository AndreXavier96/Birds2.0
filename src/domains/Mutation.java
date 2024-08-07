package domains;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
}
