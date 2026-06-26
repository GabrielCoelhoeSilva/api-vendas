package gabrielcoelho.vendasapi.transportadora.dto.request;

import gabrielcoelho.vendasapi.transportadora.validation.Cnpj;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * DTO utilizado no cadastro (POST) de uma nova transportadora.
 * O campo "ativo" não é exposto: toda transportadora é criada como
 * ativa, por regra de negócio definida na camada Service.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransportadoraCreateRequest {

    @NotBlank(message = "O código da transportadora é obrigatório")
    @Size(max = 20, message = "O código deve ter no máximo 20 caracteres")
    private String codigo;

    @NotBlank(message = "A razão social é obrigatória")
    @Size(max = 150, message = "A razão social deve ter no máximo 150 caracteres")
    private String razaoSocial;

    @Size(max = 150, message = "O nome fantasia deve ter no máximo 150 caracteres")
    private String nomeFantasia;

    @NotBlank(message = "O CNPJ é obrigatório")
    @Cnpj(message = "O CNPJ informado é inválido")
    private String cnpj;

    @Size(max = 20, message = "A inscrição estadual deve ter no máximo 20 caracteres")
    private String inscricaoEstadual;

    @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres")
    private String telefone;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Informe um e-mail válido")
    @Size(max = 150, message = "O e-mail deve ter no máximo 150 caracteres")
    private String email;

    @Pattern(regexp = "\\d{8}", message = "O CEP deve conter 8 dígitos numéricos")
    private String cep;

    @Size(max = 150, message = "O logradouro deve ter no máximo 150 caracteres")
    private String logradouro;

    @Size(max = 20, message = "O número deve ter no máximo 20 caracteres")
    private String numero;

    @Size(max = 100, message = "O complemento deve ter no máximo 100 caracteres")
    private String complemento;

    @Size(max = 100, message = "O bairro deve ter no máximo 100 caracteres")
    private String bairro;

    @Size(max = 100, message = "A cidade deve ter no máximo 100 caracteres")
    private String cidade;

    @Pattern(regexp = "[A-Za-z]{2}", message = "O estado deve ser informado com a sigla de 2 letras (UF)")
    private String estado;
}

