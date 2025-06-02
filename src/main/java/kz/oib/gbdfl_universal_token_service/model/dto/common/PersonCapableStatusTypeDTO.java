package kz.oib.gbdfl_universal_token_service.model.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PersonCapableStatusTypeDTO {
    @Schema(description = "Дееспособный статус")
    private DirectoryTypeDTO capableStatus;
    @Schema(description = "Дата дееспособности")
    private LocalDate capableDate;
    @Schema(description = "Дата конца дееспособности")
    private LocalDate capableEndDate;
    @Schema(description = "Номер дееспособности")
    private String capableNumber;
    @Schema(description = "Суд")
    private DirectoryTypeDTO court;
}
