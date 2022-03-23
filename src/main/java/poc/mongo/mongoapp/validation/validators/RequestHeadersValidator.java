package poc.mongo.mongoapp.validation.validators;

import org.springframework.stereotype.Service;
import poc.mongo.mongoapp.controllers.requests.HeadersDTO;
import poc.mongo.mongoapp.exceptions.BadRequestException;

import java.util.Objects;

@Service
public class RequestHeadersValidator {


    public void execute(final HeadersDTO headersDTO) throws BadRequestException {

        validateTraceId(headersDTO.getTraceId());
    }

    private void validateTraceId(final String traceId) throws BadRequestException {

        if(Objects.isNull(traceId)) {
            throw new BadRequestException("The header 'traceId' is required");
        }
    }

}
