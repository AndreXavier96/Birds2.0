package domains;

import java.util.Date;

public class State {
	private Integer id;
    private String type;
    private Date date;
    private Double valor;
    private String motivo;
    
    
    
    public State() {
		super();
	}

	public State(Integer id, String type, Date date, Double valor, String motivo) {
		super();
		this.id = id;
		this.type = type;
		this.date = date;
		this.valor = valor;
		this.motivo = motivo;
	}

	public String getType() {
        return type;
    }
    
	public void setType(String type) {
		this.type = type;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
    
}