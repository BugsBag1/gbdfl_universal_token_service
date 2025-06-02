package kz.oib.gbdfl_universal_token_service.model.mapper;

import kz.oib.gbdfl_universal_token_service.model.dto.ResponseDTO;
import kz.oib.gbdfl_universal_token_service.xsd.RequestAndResponse.Response;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResponseMapper {
    ResponseDTO toDTO(Response response);
}
