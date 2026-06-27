package gabrielcoelho.vendasapi.pedido.dto.response;

import gabrielcoelho.vendasapi.pedido.entity.FormaPagamento;
import gabrielcoelho.vendasapi.pedido.entity.StatusPedido;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de saída exposto pela API. Mantém a representação externa do
 * pedido isolada da entidade JPA, seguindo o mesmo padrão usado em
 * TransportadoraResponse/VendedorResponse.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoResponse {

    private Long id;
    private String codigo;

    private Long clienteId;
    private String clienteNome;

    private Long vendedorId;
    private String vendedorNome;

    private Long transportadoraId;
    private String transportadoraNomeFantasia;

    private FormaPagamento formaPagamento;
    private StatusPedido status;

    private List<ItemPedidoResponse> itens;
    private BigDecimal total;

    private LocalDateTime dataCadastro;
}

