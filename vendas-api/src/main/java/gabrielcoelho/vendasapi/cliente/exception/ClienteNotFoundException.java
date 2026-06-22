package gabrielcoelho.vendasapi.cliente.exception;

public class ClienteNotFoundException extends RuntimeException{

    public ClienteNotFoundException(Long id) {
        super("Cliente não encontrado com o id: " + id);
    }
}
