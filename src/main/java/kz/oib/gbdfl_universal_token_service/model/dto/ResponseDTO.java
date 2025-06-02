package kz.oib.gbdfl_universal_token_service.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kz.oib.gbdfl_universal_token_service.model.dto.common.DirectoryTypeDTO;
import kz.oib.gbdfl_universal_token_service.model.dto.common.PersonsTypeDTO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseDTO {
    @Schema(description = "Id сообщения")
    private String messageId;
    @Schema(description = "Дата сообщения")
    private LocalDateTime messageDate;
    @Schema(description = "Id запроса")
    private String requestId;
    @Schema(description = "Отправитель")
    private DirectoryTypeDTO sender;
    @Schema(description = "Получатель")
    private DirectoryTypeDTO receiver;
    @Schema(description = "Результат отправки сообщения")
    private DirectoryTypeDTO messageResult;
    @Schema(description = "Персоны")
    private PersonsTypeDTO persons;
}
