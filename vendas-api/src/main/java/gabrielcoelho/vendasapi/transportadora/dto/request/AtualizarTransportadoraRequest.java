package gabrielcoelho.vendasapi.transportadora.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * DTO utilizado para alterar a transportadora vinculada a um pedido
 * já existente, respeitando a regra de que pedidos faturados não
 * podem ter a transportadora alterada.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AtualizarTransportadoraRequest {

    @NotNull(message = "O id da transportadora é obrigatório")
    private Long transportadoraId;
}
