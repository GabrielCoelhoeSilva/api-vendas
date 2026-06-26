package gabrielcoelho.vendasapi.transportadora.exception;

/**
 * Lançada quando há violação de unicidade de negócio (código, CNPJ
 * ou e-mail já cadastrados). Tratada com HTTP 409 (Conflict).
 */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}

