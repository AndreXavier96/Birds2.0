package domains;

import lombok.Data;

@Data
public class Award {
	private Integer id;
	private Integer Pontuation;
	private Exibithion exibithion;
	private String judgmentImagePath;
	private Bird bird;

	@Override
	public String toString() {
		return "Passaro:"+bird.getBand()+" Exposicao:" + exibithion+" Pontuacao:"+Pontuation;
	}
}
