package kz.oib.gbdfl_universal_token_service.model.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BirthPlaceTypeDTO {
    @Schema(description = "Страна")
    private DirectoryTypeDTO country;
    @Schema(description = "Район")
    private DirectoryTypeDTO district;
    @Schema(description = "Регион")
    private DirectoryTypeDTO region;
    @Schema(description = "Внешние данные")
    private ForeignDataTypeDTO foreignData;
    @Schema(description = "Город")
    private String city;
    @Schema(description = "код при рождении AR")
    private String birthTeCodeAR;
}
