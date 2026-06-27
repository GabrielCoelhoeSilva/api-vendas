package gabrielcoelho.vendasapi.pedido.entity;

/**
 * Representa o ciclo de vida de um pedido.
 *
 * ABERTO     -> pedido recém-criado, ainda pode ser alterado ou excluído.
 * FATURADO   -> pedido faturado; a partir daqui a transportadora não
 *               pode mais ser alterada (regra de negócio) e o pedido
 *               não pode mais ser excluído, apenas entregue ou cancelado.
 * ENTREGUE   -> pedido concluído. Estado final.
 * CANCELADO  -> pedido cancelado. Estado final.
 */
public enum StatusPedido {
    ABERTO,
    FATURADO,
    ENTREGUE,
    CANCELADO
}

