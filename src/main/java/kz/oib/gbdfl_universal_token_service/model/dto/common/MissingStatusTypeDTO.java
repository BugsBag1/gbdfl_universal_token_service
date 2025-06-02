package kz.oib.gbdfl_universal_token_service.model.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MissingStatusTypeDTO {
    @Schema(description = "Потерянный")
    private Boolean missing;
    @Schema(description = "Дата пропажи")
    private LocalDate missingDate;
    @Schema(description = "Дата конца пропажи")
    private LocalDate missingEndDate;
    @Schema(description = "Номер пропажи")
    private String missingNumber;
    @Schema(description = "Территориальный ГП")
    private DirectoryTypeDTO gpTerritorial;
}
