package kz.oib.gbdfl_universal_token_service.model.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PersonRepatriationStatusTypeDTO {
    @Schema(description = "Статус репатриации")
    private DirectoryTypeDTO repatriationStatus;
    @Schema(description = "Дата репатриации")
    private LocalDateTime repatriationDate;
    @Schema(description = "Дата конца репатриации")
    private LocalDateTime repatriationEndDate;
    @Schema(description = "Номер репатриации")
    private String repatriationNumber;
    @Schema(description = "Организация по репатриации")
    private DirectoryTypeDTO repatriationOrg;
    @Schema(description = "Причина репатриации")
    private DirectoryTypeDTO repatriationReason;
}
