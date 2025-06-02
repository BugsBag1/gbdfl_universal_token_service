package kz.oib.gbdfl_universal_token_service.model.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import kz.oib.gbdfl_universal_token_service.xsd.RequestAndResponse.PersonRepatriationStatus;
import lombok.Data;

@Data
public class PersonTypeDTO {
    @Schema(description = "ИИН")
    private String iin;
    @Schema(description = "Фамилия")
    private String surname;
    @Schema(description = "Имя")
    private String name;
    @Schema(description = "Отчество")
    private String patronymic;
    @Schema(description = "Имя на английском")
    private String engFirstName;
    @Schema(description = "Фамилия на английском")
    private String engSurname;
    @Schema(description = "Пол")
    private DirectoryTypeDTO gender;
    @Schema(description = "Национальность")
    private DirectoryTypeDTO nationality;
    @Schema(description = "Гражданство")
    private DirectoryTypeDTO citizenship;
    @Schema(description = "Жизненный статус")
    private DirectoryTypeDTO lifeStatus;
    @Schema(description = "Статус заключенного")
    private PersonImprisoneStatusTypeDTO imprisonedStatus;
    @Schema(description = "Свидетельство о рождении")
    private CertificateTypeDTO birthCertificate;
    @Schema(description = "Свидетельство о смерти")
    private CertificateTypeDTO deathCertificate;
    @Schema(description = "Место рождения")
    private BirthPlaceTypeDTO birthPlace;
    @Schema(description = "Регистрационный адрес")
    private RegAddressTypeDTO regAddress;
    @Schema(description = "Статус дееспособного лица")
    private PersonCapableStatusTypeDTO personCapableStatus;
    @Schema(description = "Статус пропажи")
    private MissingStatusTypeDTO missingStatus;
    @Schema(description = "Статус исчезновения")
    private DisappearStatusTypeDTO disappearStatus;
    @Schema(description = "Исключительный статус")
    private PersonExcludeStatusTypeDTO excludeStatus;
    @Schema(description = "Статус репатриации")
    private PersonRepatriationStatusTypeDTO repatriationStatus;
    @Schema(description = "Документы")
    private DocumentsTypeDTO documents;
    @Schema(description = "Адреса")
    private AddressesTypeDTO addresses;
    @Schema(description = "Удаленный")
    private Boolean removed;
}
