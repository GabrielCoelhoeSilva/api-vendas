package gabrielcoelho.vendasapi.pedido.service;

import gabrielcoelho.vendasapi.pedido.dto.request.PedidoCreateRequest;
import gabrielcoelho.vendasapi.pedido.dto.request.PedidoUpdateRequest;
import gabrielcoelho.vendasapi.pedido.dto.response.PedidoResponse;
import gabrielcoelho.vendasapi.pedido.entity.StatusPedido;
import gabrielcoelho.vendasapi.transportadora.dto.request.AtualizarTransportadoraRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Contrato de regras de negócio do módulo de Pedidos.
 * Toda validação de negócio deve ser implementada exclusivamente
 * nesta camada — mesmo padrão de TransportadoraService/VendedorService.
 */
public interface PedidoService {

    PedidoResponse criar(PedidoCreateRequest request);

    PedidoResponse buscarPorId(Long id);

    Page<PedidoResponse> listar(String codigo, Long clienteId, Long vendedorId,
                                Long transportadoraId, StatusPedido status, Pageable pageable);

    PedidoResponse atualizar(Long id, PedidoUpdateRequest request);

    void excluir(Long id);

    PedidoResponse alterarTransportadora(Long id, AtualizarTransportadoraRequest request);

    PedidoResponse faturar(Long id);

    PedidoResponse entregar(Long id);

    PedidoResponse cancelar(Long id);
}
