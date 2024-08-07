package domains;

import java.util.Date;
import lombok.Data;

@Data
public class BirdTreatment {
    private Bird bird;
    private Treatment treatment;
    private Date start;
    private Date finish;	
}

