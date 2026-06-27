package gabrielcoelho.vendasapi.pedido.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Item enviado pelo cliente da API ao criar/atualizar um pedido.
 *
 * Propositalmente NÃO possui o campo "precoUnitario": o preço é
 * sempre obtido do Produto no momento do processamento, na camada de
 * Service. Confiar no preço enviado pelo cliente permitiria que a
 * requisição "forjasse" valores e pagasse menos do que o praticado.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemPedidoRequest {

    @NotNull(message = "O id do produto é obrigatório")
    private Long produtoId;

    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 1, message = "A quantidade deve ser maior ou igual a 1")
    private Integer quantidade;
}
