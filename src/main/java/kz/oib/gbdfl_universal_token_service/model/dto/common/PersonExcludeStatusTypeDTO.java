package kz.oib.gbdfl_universal_token_service.model.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PersonExcludeStatusTypeDTO {
    @Schema(description = "Исключающая причина")
    private DirectoryTypeDTO excludeReason;
    @Schema(description = "Дата исключения причины")
    private LocalDate excludeReasonDate;
    @Schema(description = "Дата исключения")
    private LocalDate excludeDate;
    @Schema(description = "Исключить участника")
    private DirectoryTypeDTO excludeParticipant;
}
