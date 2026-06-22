package gabrielcoelho.vendasapi.cliente.controller;


import gabrielcoelho.vendasapi.cliente.entity.Cliente;
import gabrielcoelho.vendasapi.cliente.dto.ClienteDto;
import gabrielcoelho.vendasapi.cliente.repository.ClienteRepository;
import gabrielcoelho.vendasapi.cliente.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService service;

    @PostMapping
    public ResponseEntity<ClienteDto> salvar (@RequestBody ClienteDto request) {
        Cliente cliente = service.salvar(request.toModel());
        return ResponseEntity.ok(ClienteDto.fromModel(cliente));
    }

    @PutMapping("{id}")
    public ResponseEntity<ClienteDto> atualizar(@PathVariable Long id, @RequestBody ClienteDto request) {
        Cliente clienteAtualizado = service.atualizar(id, request.toModel());
        return ResponseEntity.ok(ClienteDto.fromModel(clienteAtualizado));
    }

    @GetMapping("{id}")
    public ResponseEntity<ClienteDto> getById (@PathVariable Long id) {
        Cliente cliente = service.buscarPorIdOuFalhar(id);
        return ResponseEntity.ok(ClienteDto.fromModel(cliente));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete (@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public Page<ClienteDto> getLista(
            @RequestParam(value = "nome", required = false, defaultValue = "") String nome,
            @RequestParam(value = "cpf", required = false, defaultValue = "") String cpf,
            Pageable pageable
    ) {
        return service.buscarPorNomeCpf(nome, cpf, pageable).map(ClienteDto::fromModel);
    }

}
