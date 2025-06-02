package kz.oib.gbdfl_universal_token_service.model.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class DocumentsTypeDTO {
    @Schema(description = "Список документов")
    private List<DocumentTypeDTO> document;
}
