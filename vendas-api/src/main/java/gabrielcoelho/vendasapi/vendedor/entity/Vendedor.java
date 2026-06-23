package gabrielcoelho.vendasapi.vendedor.entity;

import gabrielcoelho.vendasapi.venda.entity.Venda;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "vendedores",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_vendedor_codigo", columnNames = "codigo"),
                @UniqueConstraint(name = "uk_vendedor_email", columnNames = "email")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vendedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", nullable = false, length = 20, updatable = false)
    private String codigo;

    @Column(name = "nome", nullable = false, length = 120)
    private String nome;

    @Column(name = "email", nullable = false, length = 150)
    private String email;

    @Column(name = "telefone", length = 20)
    private String telefone;

    @Column(name = "percentual_comissao", nullable = false, precision = 5, scale = 2)
    private BigDecimal percentualComissao;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo;

    @Column(name = "data_cadastro", nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

    @OneToMany(mappedBy = "vendedor", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Venda> pedidos = new ArrayList<>();

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
