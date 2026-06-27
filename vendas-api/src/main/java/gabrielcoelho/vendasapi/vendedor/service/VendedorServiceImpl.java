package gabrielcoelho.vendasapi.vendedor.service;

import gabrielcoelho.vendasapi.pedido.entity.StatusPedido;
import gabrielcoelho.vendasapi.pedido.repository.PedidoRepository;
import gabrielcoelho.vendasapi.shared.exception.BusinessRuleException;
import gabrielcoelho.vendasapi.shared.exception.DuplicateResourceException;
import gabrielcoelho.vendasapi.shared.exception.ResourceNotFoundException;
import gabrielcoelho.vendasapi.vendedor.dto.request.VendedorCreateRequest;
import gabrielcoelho.vendasapi.vendedor.dto.request.VendedorUpdateRequest;
import gabrielcoelho.vendasapi.vendedor.dto.response.VendedorResponse;
import gabrielcoelho.vendasapi.vendedor.entity.Vendedor;
import gabrielcoelho.vendasapi.vendedor.mapper.VendedorMapper;
import gabrielcoelho.vendasapi.vendedor.repository.VendedorRepository;
import gabrielcoelho.vendasapi.vendedor.repository.VendedorSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementação das regras de negócio do módulo de Vendedores.
 *
 * Esta classe estava ausente no projeto: a interface VendedorService
 * existia e era usada pelo VendedorController, mas sem implementação
 * o Spring não conseguia montar o contexto da aplicação (nenhum bean
 * do tipo VendedorService era encontrado). Implementada aqui seguindo
 * exatamente o mesmo padrão de TransportadoraServiceImpl.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class VendedorServiceImpl implements VendedorService {

    private final VendedorRepository vendedorRepository;
    private final VendedorMapper mapper;
    private final PedidoRepository pedidoRepository;

    @Override
    public VendedorResponse criar(VendedorCreateRequest request) {
        validarCodigoUnico(request.getCodigo());
        validarEmailUnico(request.getEmail());

        Vendedor vendedor = Vendedor.builder()
                .codigo(request.getCodigo())
                .nome(request.getNome())
                .email(request.getEmail())
                .telefone(request.getTelefone())
                .percentualComissao(request.getPercentualComissao())
                .ativo(true)
                .dataCadastro(LocalDateTime.now())
                .build();

        Vendedor salvo = vendedorRepository.save(vendedor);
        return mapper.toResponse(salvo);
    }

    @Override
    @Transactional(readOnly = true)
    public VendedorResponse buscarPorId(Long id) {
        return mapper.toResponse(buscarVendedorOuFalhar(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VendedorResponse> listar(String codigo, String nome, Boolean ativo, Pageable pageable) {
        Specification<Vendedor> filtro = Specification
                .where(VendedorSpecification.comCodigo(codigo))
                .and(VendedorSpecification.comNome(nome))
                .and(VendedorSpecification.comStatus(ativo));

        return vendedorRepository.findAll(filtro, pageable).map(mapper::toResponse);
    }

    @Override
    public VendedorResponse atualizar(Long id, VendedorUpdateRequest request) {
        Vendedor vendedor = buscarVendedorOuFalhar(id);

        boolean emailAlterado = !vendedor.getEmail().equalsIgnoreCase(request.getEmail());
        if (emailAlterado) {
            validarEmailUnico(request.getEmail());
        }

        // O campo "codigo" nunca é tocado aqui: VendedorUpdateRequest não
        // o possui, garantindo a mesma regra de imutabilidade do código
        // usada em Transportadora.
        vendedor.setNome(request.getNome());
        vendedor.setEmail(request.getEmail());
        vendedor.setTelefone(request.getTelefone());
        vendedor.setPercentualComissao(request.getPercentualComissao());

        return mapper.toResponse(vendedorRepository.save(vendedor));
    }

    @Override
    public VendedorResponse ativar(Long id) {
        Vendedor vendedor = buscarVendedorOuFalhar(id);
        vendedor.setAtivo(true);
        return mapper.toResponse(vendedorRepository.save(vendedor));
    }

    @Override
    public VendedorResponse inativar(Long id) {
        Vendedor vendedor = buscarVendedorOuFalhar(id);

        boolean possuiPedidosEmAberto = pedidoRepository
                .existsByVendedorIdAndStatusNotIn(
                        vendedor.getId(),
                        List.of(StatusPedido.ENTREGUE, StatusPedido.CANCELADO));

        if (possuiPedidosEmAberto) {
            throw new BusinessRuleException(
                    "Não é possível inativar o vendedor pois existem pedidos em aberto associados a ele");
        }

        vendedor.setAtivo(false);
        return mapper.toResponse(vendedorRepository.save(vendedor));
    }

    private Vendedor buscarVendedorOuFalhar(Long id) {
        return vendedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendedor não encontrado para o id: " + id));
    }

    private void validarCodigoUnico(String codigo) {
        if (vendedorRepository.existsByCodigo(codigo)) {
            throw new DuplicateResourceException("Já existe um vendedor cadastrado com o código: " + codigo);
        }
    }

    private void validarEmailUnico(String email) {
        if (vendedorRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("Já existe um vendedor cadastrado com o e-mail: " + email);
        }
    }
}

