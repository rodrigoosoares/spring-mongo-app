package poc.mongo.mongoapp.validation.validators;

import poc.mongo.mongoapp.controllers.requests.Status;
import poc.mongo.mongoapp.validation.StatusValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class StatusValidator implements ConstraintValidator<StatusValidation, String> {

    @Override
    public void initialize(StatusValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(final String status, final ConstraintValidatorContext constraintValidatorContext) {

        for(final Status validStatus : Status.values()) {

            if (Objects.equals(validStatus.getStatus(), status)) {
                return true;
            }
        }

        return false;
    }
}
