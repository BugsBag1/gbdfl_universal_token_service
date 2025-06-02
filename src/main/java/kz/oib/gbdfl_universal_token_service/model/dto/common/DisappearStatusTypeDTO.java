package kz.oib.gbdfl_universal_token_service.model.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DisappearStatusTypeDTO {
    @Schema(description = "Исчезновения")
    private Boolean disappear;
    @Schema(description = "Дата исчезновения")
    private LocalDate disappearDate;
    @Schema(description = "Дата конца исчезновения")
    private LocalDate disappearEndDate;
    @Schema(description = "Номер исчезновения")
    private String disappearNumber;
    @Schema(description = "Территориальный ГП")
    private DirectoryTypeDTO gpTerritorial;
}
