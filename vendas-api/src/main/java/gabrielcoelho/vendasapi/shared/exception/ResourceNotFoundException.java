package gabrielcoelho.vendasapi.shared.exception;

/**
 * Lançada quando um recurso (transportadora, vendedor, pedido, cliente etc.)
 * não é encontrado pelo identificador informado. Tratada com HTTP 404.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
