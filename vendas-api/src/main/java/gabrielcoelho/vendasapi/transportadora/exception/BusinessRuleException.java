package gabrielcoelho.vendasapi.transportadora.exception;

/**
 * Lançada quando uma regra de negócio é violada (ex.: inativar
 * transportadora com entregas pendentes, alterar transportadora de
 * pedido faturado, etc). Tratada com HTTP 422.
 */
public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }
}

