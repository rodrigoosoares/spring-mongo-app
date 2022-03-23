package poc.mongo.mongoapp.validation.validators;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import poc.mongo.mongoapp.controllers.requests.HeadersDTO;
import poc.mongo.mongoapp.exceptions.BadRequestException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RequestHeadersValidatorTest {

    final RequestHeadersValidator requestHeadersValidator = new RequestHeadersValidator();

    @Test
    @DisplayName("Should pass validation if all headers are valid")
    void shouldPassValidationIfAllHeadersAreValid() {
        // Given
        final HeadersDTO headersDTO = new HeadersDTO();
        headersDTO.setTraceId("4a608f5e-7739-4866-b350-8f62b8e2a07a");

        // When/Then
        assertDoesNotThrow(() -> requestHeadersValidator.execute(headersDTO));
    }

    @Test
    @DisplayName("Should throw BadRequestException if traceId header is invalid")
    void shouldThrowBadRequestExceptionIfTraceIdHeaderIsInvalid() {
        // Given
        final HeadersDTO headersDTO = new HeadersDTO();

        // When/Then
        assertThrows(BadRequestException.class, () -> requestHeadersValidator.execute(headersDTO));
    }

}