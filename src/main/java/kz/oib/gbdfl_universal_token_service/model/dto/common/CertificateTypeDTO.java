package kz.oib.gbdfl_universal_token_service.model.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CertificateTypeDTO {
    @Schema(description = "Номер")
    private String number;
    @Schema(description = "Дата начала")
    private LocalDate beginDate;
    @Schema(description = "Орган выдачи")
    private String issueOrganisation;
}
