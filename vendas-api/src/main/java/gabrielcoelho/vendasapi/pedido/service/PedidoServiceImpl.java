package gabrielcoelho.vendasapi.pedido.service;

import gabrielcoelho.vendasapi.cliente.entity.Cliente;
import gabrielcoelho.vendasapi.cliente.repository.ClienteRepository;
import gabrielcoelho.vendasapi.pedido.dto.request.ItemPedidoRequest;
import gabrielcoelho.vendasapi.pedido.dto.request.PedidoCreateRequest;
import gabrielcoelho.vendasapi.pedido.dto.request.PedidoUpdateRequest;
import gabrielcoelho.vendasapi.pedido.dto.response.PedidoResponse;
import gabrielcoelho.vendasapi.pedido.entity.ItemPedido;
import gabrielcoelho.vendasapi.pedido.entity.Pedido;
import gabrielcoelho.vendasapi.pedido.entity.StatusPedido;
import gabrielcoelho.vendasapi.pedido.mapper.PedidoMapper;
import gabrielcoelho.vendasapi.pedido.repository.PedidoRepository;
import gabrielcoelho.vendasapi.pedido.repository.PedidoSpecification;
import gabrielcoelho.vendasapi.produto.entity.Produto;
import gabrielcoelho.vendasapi.produto.repository.ProdutoRepository;
import gabrielcoelho.vendasapi.shared.exception.BusinessRuleException;
import gabrielcoelho.vendasapi.shared.exception.ResourceNotFoundException;
import gabrielcoelho.vendasapi.transportadora.dto.request.AtualizarTransportadoraRequest;
import gabrielcoelho.vendasapi.transportadora.entity.Transportadora;
import gabrielcoelho.vendasapi.transportadora.repository.TransportadoraRepository;
import gabrielcoelho.vendasapi.vendedor.entity.Vendedor;
import gabrielcoelho.vendasapi.vendedor.repository.VendedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Implementação das regras de negócio do módulo de Pedidos.
 *
 * Decisões de design (documentadas aqui por não terem um precedente
 * idêntico nos outros módulos):
 *
 * 1) "codigo" NÃO é informado pelo cliente da API (diferente de
 *    Transportadora/Vendedor). Pedir que o cliente digite um código
 *    de pedido manualmente não faz sentido de negócio e só geraria
 *    conflitos de duplicidade; o código é gerado pelo próprio sistema.
 *
 * 2) O preço de cada item é sempre obtido do Produto no momento da
 *    criação/atualização do pedido — nunca aceito vindo da requisição
 *    — para não permitir que o cliente da API manipule valores.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final VendedorRepository vendedorRepository;
    private final TransportadoraRepository transportadoraRepository;
    private final ProdutoRepository produtoRepository;
    private final PedidoMapper mapper;

    @Override
    public PedidoResponse criar(PedidoCreateRequest request) {
        Cliente cliente = buscarClienteOuFalhar(request.getClienteId());
        Vendedor vendedor = buscarVendedorOuFalhar(request.getVendedorId());
        Transportadora transportadora = request.getTransportadoraId() != null
                ? buscarTransportadoraOuFalhar(request.getTransportadoraId())
                : null;

        Pedido pedido = Pedido.builder()
                .codigo(gerarCodigo())
                .cliente(cliente)
                .vendedor(vendedor)
                .transportadora(transportadora)
                .formaPagamento(request.getFormaPagamento())
                .status(StatusPedido.ABERTO)
                .build();

        montarItens(pedido, request.getItens());

        return mapper.toResponse(pedidoRepository.save(pedido));
    }

    @Override
    @Transactional(readOnly = true)
    public PedidoResponse buscarPorId(Long id) {
        return mapper.toResponse(buscarPedidoOuFalhar(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PedidoResponse> listar(String codigo, Long clienteId, Long vendedorId,
                                       Long transportadoraId, StatusPedido status, Pageable pageable) {
        Specification<Pedido> filtro = Specification
                .where(PedidoSpecification.comCodigo(codigo))
                .and(PedidoSpecification.comCliente(clienteId))
                .and(PedidoSpecification.comVendedor(vendedorId))
                .and(PedidoSpecification.comTransportadora(transportadoraId))
                .and(PedidoSpecification.comStatus(status));

        return pedidoRepository.findAll(filtro, pageable).map(mapper::toResponse);
    }

    @Override
    public PedidoResponse atualizar(Long id, PedidoUpdateRequest request) {
        Pedido pedido = buscarPedidoOuFalhar(id);
        garantirQueEstaAberto(pedido, "atualizado");

        Vendedor vendedor = buscarVendedorOuFalhar(request.getVendedorId());

        pedido.setVendedor(vendedor);
        pedido.setFormaPagamento(request.getFormaPagamento());

        pedido.getItens().clear();
        montarItens(pedido, request.getItens());

        return mapper.toResponse(pedidoRepository.save(pedido));
    }

    @Override
    public void excluir(Long id) {
        Pedido pedido = buscarPedidoOuFalhar(id);
        garantirQueEstaAberto(pedido, "excluído");
        pedidoRepository.delete(pedido);
    }

    @Override
    public PedidoResponse alterarTransportadora(Long id, AtualizarTransportadoraRequest request) {
        Pedido pedido = buscarPedidoOuFalhar(id);

        if (pedido.getStatus() != StatusPedido.ABERTO) {
            throw new BusinessRuleException(
                    "Não é possível alterar a transportadora de um pedido faturado, entregue ou cancelado");
        }

        Transportadora transportadora = buscarTransportadoraOuFalhar(request.getTransportadoraId());
        pedido.setTransportadora(transportadora);

        return mapper.toResponse(pedidoRepository.save(pedido));
    }

    @Override
    public PedidoResponse faturar(Long id) {
        Pedido pedido = buscarPedidoOuFalhar(id);

        if (pedido.getStatus() != StatusPedido.ABERTO) {
            throw new BusinessRuleException("Apenas pedidos abertos podem ser faturados");
        }
        if (pedido.getTransportadora() == null) {
            throw new BusinessRuleException("O pedido precisa de uma transportadora definida para ser faturado");
        }

        pedido.setStatus(StatusPedido.FATURADO);
        return mapper.toResponse(pedidoRepository.save(pedido));
    }

    @Override
    public PedidoResponse entregar(Long id) {
        Pedido pedido = buscarPedidoOuFalhar(id);

        if (pedido.getStatus() != StatusPedido.FATURADO) {
            throw new BusinessRuleException("Apenas pedidos faturados podem ser marcados como entregues");
        }

        pedido.setStatus(StatusPedido.ENTREGUE);
        return mapper.toResponse(pedidoRepository.save(pedido));
    }

    @Override
    public PedidoResponse cancelar(Long id) {
        Pedido pedido = buscarPedidoOuFalhar(id);

        if (pedido.getStatus() == StatusPedido.ENTREGUE) {
            throw new BusinessRuleException("Um pedido já entregue não pode ser cancelado");
        }
        if (pedido.getStatus() == StatusPedido.CANCELADO) {
            throw new BusinessRuleException("Este pedido já está cancelado");
        }

        pedido.setStatus(StatusPedido.CANCELADO);
        return mapper.toResponse(pedidoRepository.save(pedido));
    }

    // ---------------------------------------------------------------
    // métodos auxiliares privados
    // ---------------------------------------------------------------

    private void montarItens(Pedido pedido, List<ItemPedidoRequest> itensRequest) {
        BigDecimal total = BigDecimal.ZERO;

        for (ItemPedidoRequest itemRequest : itensRequest) {
            Produto produto = buscarProdutoOuFalhar(itemRequest.getProdutoId());

            BigDecimal precoUnitario = produto.getPreco();
            BigDecimal subtotal = precoUnitario.multiply(BigDecimal.valueOf(itemRequest.getQuantidade()));

            ItemPedido item = ItemPedido.builder()
                    .produto(produto)
                    .quantidade(itemRequest.getQuantidade())
                    .precoUnitario(precoUnitario)
                    .subtotal(subtotal)
                    .build();

            pedido.adicionarItem(item);
            total = total.add(subtotal);
        }

        pedido.setTotal(total);
    }

    private void garantirQueEstaAberto(Pedido pedido, String acao) {
        if (pedido.getStatus() != StatusPedido.ABERTO) {
            throw new BusinessRuleException(
                    "Apenas pedidos com status ABERTO podem ser " + acao + ". Status atual: " + pedido.getStatus());
        }
    }

    private String gerarCodigo() {
        return "PED-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private Pedido buscarPedidoOuFalhar(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado para o id: " + id));
    }

    private Cliente buscarClienteOuFalhar(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado para o id: " + id));
    }

    private Vendedor buscarVendedorOuFalhar(Long id) {
        return vendedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendedor não encontrado para o id: " + id));
    }

    private Transportadora buscarTransportadoraOuFalhar(Long id) {
        return transportadoraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transportadora não encontrada para o id: " + id));
    }

    private Produto buscarProdutoOuFalhar(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado para o id: " + id));
    }
}

