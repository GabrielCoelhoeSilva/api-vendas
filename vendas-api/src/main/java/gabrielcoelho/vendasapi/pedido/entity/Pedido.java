package gabrielcoelho.vendasapi.pedido.entity;

import gabrielcoelho.vendasapi.cliente.entity.Cliente;
import gabrielcoelho.vendasapi.transportadora.entity.Transportadora;
import gabrielcoelho.vendasapi.vendedor.entity.Vendedor;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa um pedido de venda, vinculado a um Cliente, um Vendedor
 * e (opcionalmente, até ser faturado) uma Transportadora.
 *
 * Segue o mesmo padrão estrutural de Transportadora/Vendedor: código
 * único e imutável, timestamps via @PrePersist, e os relacionamentos
 * com Transportadora/Vendedor já estavam previstos nos comentários e
 * DTOs deixados nesses módulos.
 */
@Entity
@Table(
        name = "pedido",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_pedido_codigo", columnNames = "codigo")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", nullable = false, length = 20, updatable = false)
    private String codigo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_vendedor", nullable = false)
    private Vendedor vendedor;

    @ManyToOne
    @JoinColumn(name = "id_transportadora")
    private Transportadora transportadora;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento", nullable = false, length = 20)
    private FormaPagamento formaPagamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusPedido status;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ItemPedido> itens = new ArrayList<>();

    @Column(name = "total", precision = 16, scale = 2, nullable = false)
    private BigDecimal total;

    @Column(name = "data_cadastro", nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

    @PrePersist
    public void prePersist() {
        if (this.status == null) {
            this.status = StatusPedido.ABERTO;
        }
        if (this.dataCadastro == null) {
            this.dataCadastro = LocalDateTime.now();
        }
    }

    /**
     * Mantém a lista de itens e a referência inversa (item -> pedido)
     * sempre consistentes, evitando ter que lembrar de setar os dois
     * lados manualmente em cada ponto de uso.
     */
    public void adicionarItem(ItemPedido item) {
        item.setPedido(this);
        this.itens.add(item);
    }
}

