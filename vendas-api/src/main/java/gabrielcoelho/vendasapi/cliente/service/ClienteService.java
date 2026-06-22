package gabrielcoelho.vendasapi.cliente.service;

import gabrielcoelho.vendasapi.cliente.exception.ClienteNotFoundException;
import gabrielcoelho.vendasapi.cliente.entity.Cliente;
import gabrielcoelho.vendasapi.cliente.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;

    @Transactional
    public Cliente salvar(Cliente cliente) {
        return repository.save(cliente);
    }

    @Transactional
    public Cliente atualizar(Long id, Cliente clienteAtualizado) {
        Cliente clienteExistente = buscarPorIdOuFalhar(id);
        clienteAtualizado.setId(clienteExistente.getId());
        return repository.save(clienteAtualizado);
    }

    public Cliente buscarPorIdOuFalhar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException(id));
    }

    @Transactional
    public void deletar(Long id) {
        Cliente cliente = buscarPorIdOuFalhar(id);
        repository.delete(cliente);
    }

    public Page<Cliente> buscarPorNomeCpf(String nome, String cpf, Pageable pageable) {
        return repository.buscarPorNomeCpf("%" + nome + "%", "%" + cpf + "%", pageable);
    }

}
