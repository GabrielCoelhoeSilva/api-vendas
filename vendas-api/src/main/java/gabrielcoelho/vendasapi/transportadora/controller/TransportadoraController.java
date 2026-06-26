package gabrielcoelho.vendasapi.transportadora.controller;

import gabrielcoelho.vendasapi.transportadora.dto.request.TransportadoraCreateRequest;
import gabrielcoelho.vendasapi.transportadora.dto.request.TransportadoraUpdateRequest;
import gabrielcoelho.vendasapi.transportadora.dto.response.PageResponse;
import gabrielcoelho.vendasapi.transportadora.dto.response.TransportadoraResponse;
import gabrielcoelho.vendasapi.transportadora.service.TransportadoraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

/**
 * Endpoints REST do módulo de Transportadoras.
 */
@RestController
@RequestMapping("/api/v1/transportadoras")
@RequiredArgsConstructor
public class TransportadoraController {

    private final TransportadoraService transportadoraService;

    @PostMapping
    public ResponseEntity<TransportadoraResponse> criar(@Valid @RequestBody TransportadoraCreateRequest request) {
        TransportadoraResponse response = transportadoraService.criar(request);
        URI location = URI.create("/api/v1/transportadoras/" + response.getId());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransportadoraResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(transportadoraService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<PageResponse<TransportadoraResponse>> listar(
            @RequestParam(required = false) String codigo,
            @RequestParam(required = false) String razaoSocial,
            @RequestParam(required = false) String cnpj,
            @RequestParam(required = false) Boolean ativo,
            @PageableDefault(size = 20, sort = "razaoSocial") Pageable pageable) {

        Page<TransportadoraResponse> pagina = transportadoraService.listar(codigo, razaoSocial, cnpj, ativo, pageable);
        return ResponseEntity.ok(PageResponse.of(pagina));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransportadoraResponse> atualizar(@PathVariable Long id,
                                                            @Valid @RequestBody TransportadoraUpdateRequest request) {
        return ResponseEntity.ok(transportadoraService.atualizar(id, request));
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<TransportadoraResponse> ativar(@PathVariable Long id) {
        return ResponseEntity.ok(transportadoraService.ativar(id));
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<TransportadoraResponse> inativar(@PathVariable Long id) {
        return ResponseEntity.ok(transportadoraService.inativar(id));
    }
}

