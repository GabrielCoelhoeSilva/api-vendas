package gabrielcoelho.vendasapi.pedido.mapper;

import gabrielcoelho.vendasapi.pedido.dto.response.ItemPedidoResponse;
import gabrielcoelho.vendasapi.pedido.dto.response.PedidoResponse;
import gabrielcoelho.vendasapi.pedido.entity.ItemPedido;
import gabrielcoelho.vendasapi.pedido.entity.Pedido;
import gabrielcoelho.vendasapi.transportadora.entity.Transportadora;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Responsável por converter a entidade Pedido (e seus itens) em seus
 * respectivos DTOs de resposta, mantendo a camada de domínio isolada
 * da API — mesmo padrão de TransportadoraMapper/VendedorMapper.
 */
@Component
public class PedidoMapper {

    public PedidoResponse toResponse(Pedido pedido) {
        if (pedido == null) {
            return null;
        }

        Transportadora transportadora = pedido.getTransportadora();

        return PedidoResponse.builder()
                .id(pedido.getId())
                .codigo(pedido.getCodigo())
                .clienteId(pedido.getCliente().getId())
                .clienteNome(pedido.getCliente().getNome())
                .vendedorId(pedido.getVendedor().getId())
                .vendedorNome(pedido.getVendedor().getNome())
                .transportadoraId(transportadora != null ? transportadora.getId() : null)
                .transportadoraNomeFantasia(transportadora != null ? transportadora.getNomeFantasia() : null)
                .formaPagamento(pedido.getFormaPagamento())
                .status(pedido.getStatus())
                .itens(toItemResponseList(pedido.getItens()))
                .total(pedido.getTotal())
                .dataCadastro(pedido.getDataCadastro())
                .build();
    }

    private List<ItemPedidoResponse> toItemResponseList(List<ItemPedido> itens) {
        return itens.stream()
                .map(this::toItemResponse)
                .collect(Collectors.toList());
    }

    private ItemPedidoResponse toItemResponse(ItemPedido item) {
        return ItemPedidoResponse.builder()
                .id(item.getId())
                .produtoId(item.getProduto().getId())
                .produtoNome(item.getProduto().getNome())
                .quantidade(item.getQuantidade())
                .precoUnitario(item.getPrecoUnitario())
                .subtotal(item.getSubtotal())
                .build();
    }
}

