package poc.mongo.mongoapp.validation;

import poc.mongo.mongoapp.validation.validators.StatusValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

@Target( { FIELD, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StatusValidator.class)
public @interface StatusValidation {

    String message() default "Invalid status. Should be 'active' or 'deleted'.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
