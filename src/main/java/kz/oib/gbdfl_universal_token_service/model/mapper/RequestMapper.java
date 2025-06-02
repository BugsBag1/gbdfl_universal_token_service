package kz.oib.gbdfl_universal_token_service.model.mapper;

import kz.oib.gbdfl_universal_token_service.model.dto.RequestDTO;
import kz.oib.gbdfl_universal_token_service.xsd.RequestAndResponse.Request;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    @Mapping(target = "surname", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "patronymic", ignore = true)
    @Mapping(target = "birthDate", ignore = true)
    @Mapping(target = "birthDateFrom", ignore = true)
    @Mapping(target = "birthDateTo", ignore = true)
    @Mapping(target = "documentNumber", ignore = true)
    @Mapping(source = "messageId", target = "messageId")
    @Mapping(source = "messageDate", target = "messageDate")
    @Mapping(source = "senderCode", target = "senderCode")
    @Mapping(source = "iin", target = "iin")
    @Mapping(source = "kdpToken", target = "kdpToken")
    @Mapping(source = "publicKey", target = "publicKey")
    Request map(RequestDTO requestDTO);
}
