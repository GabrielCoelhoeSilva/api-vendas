package gabrielcoelho.vendasapi.pedido.repository;

import gabrielcoelho.vendasapi.pedido.entity.Pedido;
import gabrielcoelho.vendasapi.pedido.entity.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PedidoRepository extends
        JpaRepository<Pedido, Long>,
        JpaSpecificationExecutor<Pedido> {

    boolean existsByCodigo(String codigo);

    /**
     * Usado por TransportadoraServiceImpl#inativar para impedir a
     * inativação de uma transportadora com entregas pendentes.
     */
    boolean existsByTransportadoraIdAndStatusNotIn(Long transportadoraId, List<StatusPedido> statusExcluidos);

    /**
     * Usado por VendedorServiceImpl#inativar para impedir a inativação
     * de um vendedor com pedidos ainda em aberto.
     */
    boolean existsByVendedorIdAndStatusNotIn(Long vendedorId, List<StatusPedido> statusExcluidos);
}

