package gabrielcoelho.vendasapi.pedido.dto.request;

import gabrielcoelho.vendasapi.pedido.entity.FormaPagamento;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

/**
 * DTO utilizado para atualizar um pedido (PUT).
 *
 * Assim como em TransportadoraUpdateRequest, os campos "codigo" e
 * "transportadoraId" não são expostos aqui:
 * - "codigo" é imutável, definido apenas na criação;
 * - "transportadoraId" tem uma regra de negócio própria (não pode
 *   ser alterado após o pedido ser faturado) e por isso possui um
 *   endpoint dedicado (PATCH /pedidos/{id}/transportadora).
 *
 * A atualização completa de um pedido só é permitida enquanto ele
 * estiver com status ABERTO (regra aplicada na Service).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoUpdateRequest {

    @NotNull(message = "O id do vendedor é obrigatório")
    private Long vendedorId;

    @NotNull(message = "A forma de pagamento é obrigatória")
    private FormaPagamento formaPagamento;

    @NotEmpty(message = "O pedido deve conter ao menos um item")
    @Valid
    private List<ItemPedidoRequest> itens;
}
