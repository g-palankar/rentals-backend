package dev.ganeshpalankar.rentals_backend.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validation annotation to check if a value is a valid enum constant.
 * Works in conjunction with Jackson's deserialization - if Jackson cannot
 * deserialize a string to the enum, it will be null and this validator
 * will provide a user-friendly error message.
 *
 * Usage:
 * <pre>
 * {@code
 * @ValidEnum(enumClass = PropertyType.class, message = "Invalid property type")
 * private PropertyType propertyType;
 * }
 * </pre>
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValidator.class)
@Documented
public @interface ValidEnum {

    /**
     * The enum class to validate against.
     */
    Class<? extends Enum<?>> enumClass();

    /**
     * Error message when validation fails.
     */
    String message() default "Invalid value for enum";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
