package gabrielcoelho.vendasapi.transportadora.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Validação de Bean Validation que garante o formato e os dígitos
 * verificadores de um CNPJ antes que ele chegue à camada de
 * persistência (regra de negócio nº 16).
 *
 * Aceita CNPJ com ou sem máscara (XX.XXX.XXX/XXXX-XX ou apenas números).
 */
@Documented
@Constraint(validatedBy = CnpjValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cnpj {

    String message() default "CNPJ inválido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
