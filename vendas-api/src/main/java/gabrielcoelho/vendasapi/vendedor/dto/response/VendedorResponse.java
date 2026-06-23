package gabrielcoelho.vendasapi.vendedor.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendedorResponse {

    private Long id;
    private String codigo;
    private String nome;
    private String email;
    private String telefone;
    private BigDecimal percentualComissao;
    private Boolean ativo;
    private LocalDateTime dataCadastro;
}

