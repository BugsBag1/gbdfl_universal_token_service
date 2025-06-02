package kz.oib.gbdfl_universal_token_service.model.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AddressTypeDTO {
    @Schema(description = "Тип адреса")
    private DirectoryTypeDTO type;
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
    @Schema(description = "Улица")
    private String street;
    @Schema(description = "Здание")
    private String building;
    @Schema(description = "Корпус")
    private String corpus;
    @Schema(description = "Квартира")
    private String flat;
    @Schema(description = "Дата начала")
    private LocalDate beginDate;
    @Schema(description = "Дата конца")
    private LocalDate endDate;
    @Schema(description = "AR код")
    private String arCode;
}
