package kz.oib.gbdfl_universal_token_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kz.oib.gbdfl_universal_token_service.model.dto.ResponseDTO;
import kz.oib.gbdfl_universal_token_service.model.dto.ResponseWrapper;
import kz.oib.gbdfl_universal_token_service.service.AppService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.parsing.Problem;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/gbdflUniversalToken")
@RequiredArgsConstructor
public class AppController {
    private final AppService appService;

    @Operation(summary = "Универсальный сервис для передачи сведений о физических лицах по ИИН с использованием токена")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request\n" +
                            "\n" +
                            "**Типы ошибок, которые может вернуть API**\n" +
                            "\n" +
                            "| Атрибут `type`                    |  Причина                                      |\n" +
                            "| --------------------------------- | ----------------------------------------------|\n" +
                            "| `kdp-token-not-found`             |  Kdp токен не найден                           |",
                    content = {@Content(schema = @Schema(implementation = Problem.class), mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE)}
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Данные не найдены\n" +
                            "\n" +
                            "**Типы ошибок, которые может вернуть API**\n" +
                            "\n" +
                            "| Атрибут `type`                    | Причина                                      |\n" +
                            "| --------------------------------- | ---------------------------------------------|\n" +
                            "| `data-not-found`                  | Данные не найдены                            |",
                    content = {@Content(schema = @Schema(implementation = Problem.class), mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE)}
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error\n" +
                            "\n" +
                            "**Типы ошибок, которые может вернуть API**\n" +
                            "\n" +
                            "| Атрибут `type`                   | Причина                                                     |\n" +
                            "| ---------------------------------| ------------------------------------------------------------|\n" +
                            "| `remote-service-error`           | Ошибка на стороне гос. органа, см. поле `remoteServiceError`|\n" +
                            "| `shep-error`                     | Ошибка ВШЭП, см. поле `shepError`                           |\n" +
                            "| `shep-unknown-error`             | Неизвестная ошибка ВШЭП                                     |\n" +
                            "| `govtech-transport-error`        | Транспортная ошибка GovTech                                 |\n" +
                            "| `unknown-error`                  | Техническая ошибка                                          |",
                    content = {@Content(schema = @Schema(implementation = Problem.class), mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE)}
            )
    })
    @GetMapping
    public ResponseEntity<ResponseWrapper<ResponseDTO>> getPersonFlByIIN(
            @Parameter(description = "ИИН физического лица (необязательный параметр)", required = false)
            @Size(min = 12, max = 12)
            @RequestParam(required = false) String iin
    ) {
        ResponseDTO responseDTO = appService.getPersonFlByIIN(iin);
        ResponseWrapper<ResponseDTO> response = ResponseWrapper.<ResponseDTO>builder()
                .success(true)
                .timestamp(LocalDateTime.now())
                .data(responseDTO)
                .build();
        return ResponseEntity.ok(response);
    }
}
