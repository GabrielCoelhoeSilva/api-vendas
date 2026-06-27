package gabrielcoelho.vendasapi.pedido.repository;

import gabrielcoelho.vendasapi.pedido.entity.Pedido;
import gabrielcoelho.vendasapi.pedido.entity.StatusPedido;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

/**
 * Especificações JPA utilizadas para compor dinamicamente os filtros
 * de listagem de pedidos, no mesmo padrão de TransportadoraSpecification.
 */
public final class PedidoSpecification {

    private PedidoSpecification() {
    }

    public static Specification<Pedido> comCodigo(String codigo) {
        return (root, query, cb) -> StringUtils.hasText(codigo)
                ? cb.equal(cb.lower(root.get("codigo")), codigo.toLowerCase())
                : cb.conjunction();
    }

    public static Specification<Pedido> comCliente(Long clienteId) {
        return (root, query, cb) -> clienteId != null
                ? cb.equal(root.get("cliente").get("id"), clienteId)
                : cb.conjunction();
    }

    public static Specification<Pedido> comVendedor(Long vendedorId) {
        return (root, query, cb) -> vendedorId != null
                ? cb.equal(root.get("vendedor").get("id"), vendedorId)
                : cb.conjunction();
    }

    public static Specification<Pedido> comTransportadora(Long transportadoraId) {
        return (root, query, cb) -> transportadoraId != null
                ? cb.equal(root.get("transportadora").get("id"), transportadoraId)
                : cb.conjunction();
    }

    public static Specification<Pedido> comStatus(StatusPedido status) {
        return (root, query, cb) -> status != null
                ? cb.equal(root.get("status"), status)
                : cb.conjunction();
    }
}

