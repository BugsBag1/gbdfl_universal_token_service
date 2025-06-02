package kz.oib.gbdfl_universal_token_service.model.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PersonImprisoneStatusTypeDTO {
    @Schema(description = "Статус заключенного")
    private DirectoryTypeDTO imprisonedStatus;
    @Schema(description = "Дата начала в заключении")
    private LocalDate imprisonedBeginDate;
    @Schema(description = "Дата окончания заключения")
    private LocalDate imprisonedEndDate;
}
