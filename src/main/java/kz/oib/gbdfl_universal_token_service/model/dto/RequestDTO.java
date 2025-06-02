package kz.oib.gbdfl_universal_token_service.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class RequestDTO implements Serializable {
    @Schema(description = "Id сообщения")
    private String messageId;
    @Schema(description = "Дата сообщения")
    private LocalDateTime messageDate;
    @Schema(description = "Код отправителя")
    private String senderCode;
    @Schema(description = "ИИН")
    private String iin;
    @Schema(description = "Токен безопасности")
    private String kdpToken;
    @Schema(description = "Публичный ключ, необходимый для проверки валидности токена безопасности")
    private String publicKey;
}
