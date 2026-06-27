package gabrielcoelho.vendasapi.pedido.dto.request;

import gabrielcoelho.vendasapi.pedido.entity.FormaPagamento;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

/**
 * DTO utilizado no cadastro (POST) de um novo pedido.
 * A transportadora é opcional na criação (pode ser definida depois,
 * via PATCH /pedidos/{id}/transportadora, reaproveitando o DTO já
 * existente em transportadora.dto.request.AtualizarTransportadoraRequest).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoCreateRequest {

    @NotNull(message = "O id do cliente é obrigatório")
    private Long clienteId;

    @NotNull(message = "O id do vendedor é obrigatório")
    private Long vendedorId;

    private Long transportadoraId;

    @NotNull(message = "A forma de pagamento é obrigatória")
    private FormaPagamento formaPagamento;

    @NotEmpty(message = "O pedido deve conter ao menos um item")
    @Valid
    private List<ItemPedidoRequest> itens;
}

