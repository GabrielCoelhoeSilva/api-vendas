package gabrielcoelho.vendasapi.vendedor.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendedorCreateRequest {

    @NotBlank(message = "O código do vendedor é obrigatório")
    @Size(max = 20, message = "O código deve ter no máximo 20 caracteres")
    private String codigo;

    @NotBlank(message = "O nome do vendedor é obrigatório")
    @Size(max = 120, message = "O nome deve ter no máximo 120 caracteres")
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Informe um e-mail válido")
    @Size(max = 150, message = "O e-mail deve ter no máximo 150 caracteres")
    private String email;

    @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres")
    private String telefone;

    @NotNull(message = "O percentual de comissão é obrigatório")
    @DecimalMin(value = "0.0", inclusive = true, message = "O percentual de comissão deve ser maior ou igual a 0")
    @DecimalMax(value = "3.0", inclusive = true, message = "O percentual de comissão deve ser menor ou igual a 3")
    private BigDecimal percentualComissao;
}

