package gabrielcoelho.vendasapi.pedido.controller;

import gabrielcoelho.vendasapi.pedido.dto.request.PedidoCreateRequest;
import gabrielcoelho.vendasapi.pedido.dto.request.PedidoUpdateRequest;
import gabrielcoelho.vendasapi.pedido.dto.response.PedidoResponse;
import gabrielcoelho.vendasapi.pedido.entity.StatusPedido;
import gabrielcoelho.vendasapi.pedido.service.PedidoService;
import gabrielcoelho.vendasapi.shared.dto.response.PageResponse;
import gabrielcoelho.vendasapi.transportadora.dto.request.AtualizarTransportadoraRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

/**
 * Endpoints REST do módulo de Pedidos, seguindo o mesmo padrão de
 * TransportadoraController/VendedorController.
 */
@RestController
@RequestMapping("/api/v1/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<PedidoResponse> criar(@Valid @RequestBody PedidoCreateRequest request) {
        PedidoResponse response = pedidoService.criar(request);
        URI location = URI.create("/api/v1/pedidos/" + response.getId());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<PageResponse<PedidoResponse>> listar(
            @RequestParam(required = false) String codigo,
            @RequestParam(required = false) Long clienteId,
            @RequestParam(required = false) Long vendedorId,
            @RequestParam(required = false) Long transportadoraId,
            @RequestParam(required = false) StatusPedido status,
            @PageableDefault(size = 20, sort = "dataCadastro") Pageable pageable) {

        Page<PedidoResponse> pagina =
                pedidoService.listar(codigo, clienteId, vendedorId, transportadoraId, status, pageable);
        return ResponseEntity.ok(PageResponse.of(pagina));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoResponse> atualizar(@PathVariable Long id,
                                                    @Valid @RequestBody PedidoUpdateRequest request) {
        return ResponseEntity.ok(pedidoService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        pedidoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/transportadora")
    public ResponseEntity<PedidoResponse> alterarTransportadora(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarTransportadoraRequest request) {
        return ResponseEntity.ok(pedidoService.alterarTransportadora(id, request));
    }

    @PatchMapping("/{id}/faturar")
    public ResponseEntity<PedidoResponse> faturar(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.faturar(id));
    }

    @PatchMapping("/{id}/entregar")
    public ResponseEntity<PedidoResponse> entregar(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.entregar(id));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<PedidoResponse> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.cancelar(id));
    }
}

