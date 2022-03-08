package poc.mongo.mongoapp.validation.validators;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class StatusValidatorTest {

    final StatusValidator statusValidator = new StatusValidator();


    @ParameterizedTest
    @ValueSource(strings = {"active", "deleted"})
    void shouldReturnTrueWhenReceiveValidStatus(final String status) {
        // Given
        final ConstraintValidatorContext mockedContext = mock(ConstraintValidatorContext.class);

        //When
        final boolean result = statusValidator.isValid(status, mockedContext);


        //Then
        assertThat(result, is(true));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ACTIVE", "DELETED", "INVALID"})
    void shouldReturnFalseWhenReceiveInValidStatus(final String status) {
        // Given
        final ConstraintValidatorContext mockedContext = mock(ConstraintValidatorContext.class);

        //When
        final boolean result = statusValidator.isValid(status, mockedContext);


        //Then
        assertThat(result, is(false));
    }

}