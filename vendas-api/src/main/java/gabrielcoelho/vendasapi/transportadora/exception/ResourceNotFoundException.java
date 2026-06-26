package gabrielcoelho.vendasapi.transportadora.exception;

/**
 * Lançada quando uma transportadora (ou outro recurso) não é
 * encontrada pelo identificador informado. Tratada com HTTP 404.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
