package kz.oib.gbdfl_universal_token_service.model.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.security.SecureRandom;
import java.time.LocalDate;

@Data
public class DocumentTypeDTO {
    @Schema(description = "Тип документа")
    private DirectoryTypeDTO type;
    @Schema(description = "Номер")
    private String number;
    @Schema(description = "Серия")
    private String series;
    @Schema(description = "Дата начала")
    private LocalDate beginDate;
    @Schema(description = "Дата конца")
    private LocalDate endDate;
    @Schema(description = "Орган выдачи")
    private DirectoryTypeDTO issueOrganization;
    @Schema(description = "Статус")
    private DirectoryTypeDTO status;
    @Schema(description = "Дата недействительности")
    private LocalDate invalidityDate;
    @Schema(description = "Фамилия")
    private String surname;
    @Schema(description = "Имя")
    private String name;
    @Schema(description = "Отчество")
    private String patronymic;
    @Schema(description = "Дата рождения")
    private LocalDate birthDate;
}
