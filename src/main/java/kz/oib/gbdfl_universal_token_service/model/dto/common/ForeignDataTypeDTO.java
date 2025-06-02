package kz.oib.gbdfl_universal_token_service.model.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ForeignDataTypeDTO {
    @Schema(description = "Название района")
    private String districtName;
    @Schema(description = "Название региона")
    private String regionName;
}
