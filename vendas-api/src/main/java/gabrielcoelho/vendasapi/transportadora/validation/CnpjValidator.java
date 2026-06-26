package gabrielcoelho.vendasapi.transportadora.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

/**
 * Implementa a validação de formato e dígitos verificadores do CNPJ,
 * aceitando entrada com ou sem máscara.
 */
public class CnpjValidator implements ConstraintValidator<Cnpj, String> {

    private static final int[] PESOS_PRIMEIRO_DIGITO = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] PESOS_SEGUNDO_DIGITO = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    @Override
    public boolean isValid(String valor, ConstraintValidatorContext context) {
        if (!StringUtils.hasText(valor)) {
            // Campos obrigatórios são tratados por @NotBlank; aqui apenas
            // validamos o formato quando um valor é informado.
            return true;
        }

        String cnpj = valor.replaceAll("\\D", "");

        if (cnpj.length() != 14) {
            return false;
        }

        if (todosDigitosIguais(cnpj)) {
            return false;
        }

        int primeiroDigito = calcularDigitoVerificador(cnpj.substring(0, 12), PESOS_PRIMEIRO_DIGITO);
        int segundoDigito = calcularDigitoVerificador(cnpj.substring(0, 12) + primeiroDigito, PESOS_SEGUNDO_DIGITO);

        String digitosCalculados = "" + primeiroDigito + segundoDigito;
        String digitosInformados = cnpj.substring(12, 14);

        return digitosCalculados.equals(digitosInformados);
    }

    private int calcularDigitoVerificador(String base, int[] pesos) {
        int soma = 0;
        for (int i = 0; i < base.length(); i++) {
            soma += Character.getNumericValue(base.charAt(i)) * pesos[i];
        }
        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }

    private boolean todosDigitosIguais(String cnpj) {
        return cnpj.chars().distinct().count() == 1;
    }
}

