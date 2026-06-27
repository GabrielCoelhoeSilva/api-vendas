package gabrielcoelho.vendasapi.transportadora.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "transportadora",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_transportadora_codigo", columnNames = "codigo"),
                @UniqueConstraint(name = "uk_transportadora_cnpj", columnNames = "cnpj"),
                @UniqueConstraint(name = "uk_transportadora_email", columnNames = "email")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transportadora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", nullable = false, length = 20, updatable = false)
    private String codigo;

    @Column(name = "razao_social", nullable = false, length = 150)
    private String razaoSocial;

    @Column(name = "nome_fantasia", length = 150)
    private String nomeFantasia;

    @Column(name = "cnpj", nullable = false, length = 14)
    private String cnpj;

    @Column(name = "inscricao_estadual", length = 20)
    private String inscricaoEstadual;

    @Column(name = "telefone", length = 20)
    private String telefone;

    @Column(name = "email", nullable = false, length = 150)
    private String email;

    @Column(name = "cep", length = 8)
    private String cep;

    @Column(name = "logradouro", length = 150)
    private String logradouro;

    @Column(name = "numero", length = 20)
    private String numero;

    @Column(name = "complemento", length = 100)
    private String complemento;

    @Column(name = "bairro", length = 100)
    private String bairro;

    @Column(name = "cidade", length = 100)
    private String cidade;

    @Column(name = "estado", length = 2)
    private String estado;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo;

    @Column(name = "data_cadastro", nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

    ///@OneToMany(mappedBy = "transportadora", fetch = FetchType.LAZY)
    ///@Builder.Default
    ///private List<Pedido> pedidos = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (this.ativo == null) {
            this.ativo = true;
        }
        if (this.dataCadastro == null) {
            this.dataCadastro = LocalDateTime.now();
        }
    }
}

