package domains;

import java.util.Date;
import lombok.Data;

@Data
public class Egg {
	private Integer Id;
	private Date postureDate;
	private Date verifiedFertilityDate;
	private Date outbreakDate;
	private String type;//fecundado desconhecido vazio
	private String statute;//chocado emDesemvolvimento partido desconhecido morteNoOvo AusenciaDeEmbriao
	private Bird bird;
}
