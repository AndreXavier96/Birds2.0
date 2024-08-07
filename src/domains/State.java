package domains;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class State {
	private Integer id;
    private String type;
    private String date;
    private Double valor;
    private String motivo;   
    
    public State() {
		super();
	}

	public State(Integer id, String type, String date, Double valor, String motivo) {
		super();
		this.id = id;
		this.type = type;
		this.date = date;
		this.valor = valor;
		this.motivo = motivo;
	}
}