package gabrielcoelho.vendasapi.vendedor.controller;

import gabrielcoelho.vendasapi.vendedor.dto.request.VendedorCreateRequest;
import gabrielcoelho.vendasapi.vendedor.dto.request.VendedorUpdateRequest;
import gabrielcoelho.vendasapi.vendedor.dto.response.PageResponse;
import gabrielcoelho.vendasapi.vendedor.dto.response.VendedorResponse;
import gabrielcoelho.vendasapi.vendedor.service.VendedorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/vendedores")
@RequiredArgsConstructor
public class VendedorController {

    private final VendedorService vendedorService;

    @PostMapping
    public ResponseEntity<VendedorResponse> criar(@Valid @RequestBody VendedorCreateRequest request) {
        VendedorResponse response = vendedorService.criar(request);
        URI location = URI.create("/api/v1/vendedores/" + response.getId());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendedorResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(vendedorService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<PageResponse<VendedorResponse>> listar(
            @RequestParam(required = false) String codigo,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Boolean ativo,
            @PageableDefault(size = 20, sort = "nome") Pageable pageable) {

        Page<VendedorResponse> pagina = vendedorService.listar(codigo, nome, ativo, pageable);
        return ResponseEntity.ok(PageResponse.of(pagina));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VendedorResponse> atualizar(@PathVariable Long id,
                                                      @Valid @RequestBody VendedorUpdateRequest request) {
        return ResponseEntity.ok(vendedorService.atualizar(id, request));
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<VendedorResponse> ativar(@PathVariable Long id) {
        return ResponseEntity.ok(vendedorService.ativar(id));
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<VendedorResponse> inativar(@PathVariable Long id) {
        return ResponseEntity.ok(vendedorService.inativar(id));
    }
}
