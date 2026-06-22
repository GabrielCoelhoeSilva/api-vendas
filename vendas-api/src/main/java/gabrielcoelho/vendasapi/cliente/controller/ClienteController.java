package gabrielcoelho.vendasapi.cliente.controller;


import gabrielcoelho.vendasapi.cliente.entity.Cliente;
import gabrielcoelho.vendasapi.cliente.dto.ClienteDto;
import gabrielcoelho.vendasapi.cliente.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteRepository repository;

    @PostMapping
    public ResponseEntity salvar (@RequestBody ClienteDto request) {
        Cliente cliente = request.toModel();
        repository.save(cliente);
        return ResponseEntity.ok(ClienteDto.fromModel(cliente));
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar (@PathVariable Long id, @RequestBody ClienteDto cliente) {
        Optional<Cliente> clienteExistente = repository.findById(id);
        if (clienteExistente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Cliente entidade = cliente.toModel();
        entidade.setId(id);
        repository.save(entidade);

        return ResponseEntity.ok().build();

    }

    @GetMapping("{id}")
    public ResponseEntity<ClienteDto> getById (@PathVariable Long id) {
        return repository.findById(id).map( ClienteDto :: fromModel)
                .map( clienteDto -> ResponseEntity.ok(clienteDto))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete (@PathVariable Long id) {
        return repository.findById(id).map(cliente -> {
            repository.delete(cliente);
            return ResponseEntity.noContent().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

}
