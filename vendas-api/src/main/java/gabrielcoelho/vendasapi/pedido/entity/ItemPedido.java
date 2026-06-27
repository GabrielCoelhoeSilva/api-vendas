package gabrielcoelho.vendasapi.pedido.entity;

import gabrielcoelho.vendasapi.produto.entity.Produto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Item de um pedido. O preço unitário é uma "fotografia" do preço do
 * Produto no momento da criação do pedido (não é recalculado se o
 * preço do produto mudar depois) — prática comum em sistemas de
 * vendas para preservar o valor histórico do pedido.
 */
@Entity
@Table(name = "itens_pedido")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_pedido", nullable = false)
    private Pedido pedido;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_produto", nullable = false)
    private Produto produto;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @Column(name = "preco_unitario", precision = 16, scale = 2, nullable = false)
    private BigDecimal precoUnitario;

    @Column(name = "subtotal", precision = 16, scale = 2, nullable = false)
    private BigDecimal subtotal;
}

