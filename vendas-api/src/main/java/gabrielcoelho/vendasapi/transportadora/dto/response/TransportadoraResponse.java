package gabrielcoelho.vendasapi.transportadora.dto.response;

import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO de saída exposto pela API. Mantém a representação externa da
 * transportadora isolada da entidade JPA.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransportadoraResponse {

    private Long id;
    private String codigo;
    private String razaoSocial;
    private String nomeFantasia;
    private String cnpj;
    private String inscricaoEstadual;
    private String telefone;
    private String email;
    private String cep;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private Boolean ativo;
    private LocalDateTime dataCadastro;
}
