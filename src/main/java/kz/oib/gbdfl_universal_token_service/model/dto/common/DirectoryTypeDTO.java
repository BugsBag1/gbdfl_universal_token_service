package kz.oib.gbdfl_universal_token_service.model.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DirectoryTypeDTO {
    @Schema(description = "Код")
    private String code;
    @Schema(description = "Название на русском языке")
    private String nameRu;
    @Schema(description = "Название на государственном языке")
    private String nameKz;
}
