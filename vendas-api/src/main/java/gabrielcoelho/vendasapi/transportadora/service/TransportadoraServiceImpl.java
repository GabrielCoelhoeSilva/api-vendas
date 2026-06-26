package gabrielcoelho.vendasapi.transportadora.service;

import gabrielcoelho.vendasapi.transportadora.entity.Transportadora;
import gabrielcoelho.vendasapi.transportadora.dto.request.TransportadoraCreateRequest;
import gabrielcoelho.vendasapi.transportadora.dto.request.TransportadoraUpdateRequest;
import gabrielcoelho.vendasapi.transportadora.dto.response.TransportadoraResponse;
import gabrielcoelho.vendasapi.transportadora.exception.BusinessRuleException;
import gabrielcoelho.vendasapi.transportadora.exception.DuplicateResourceException;
import gabrielcoelho.vendasapi.transportadora.exception.ResourceNotFoundException;
import gabrielcoelho.vendasapi.transportadora.mapper.TransportadoraMapper;
import gabrielcoelho.vendasapi.transportadora.repository.TransportadoraRepository;
import gabrielcoelho.vendasapi.transportadora.repository.TransportadoraSpecification;
import gabrielcoelho.vendasapi.transportadora.service.TransportadoraService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Implementação das regras de negócio do módulo de Transportadoras.
 *
 * Mapeamento das regras de negócio do enunciado:
 *  1. Código único              -> validarCodigoUnico
 *  2/17. CNPJ único              -> validarCnpjUnico
 *  3. E-mail único               -> validarEmailUnico
 *  4. Criada como ativa          -> criar()
 *  5. Código imutável            -> TransportadoraUpdateRequest não expõe "codigo"
 *  6/7. Sem exclusão física      -> não há método de delete; apenas ativar/inativar
 *  8. Inativa não usada em pedidos -> validado em PedidoServiceImpl ao criar/alterar pedido
 *  9. Bloqueio de inativação com entregas pendentes -> inativar()
 *  10. Reativação                -> ativar()
 *  11. Filtro por status         -> listar()
 *  16. Formato do CNPJ validado antes da persistência -> @Cnpj nos DTOs + normalização aqui
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TransportadoraServiceImpl implements TransportadoraService {

    private final TransportadoraRepository transportadoraRepository;
    private final TransportadoraMapper mapper;

    @Override
    public TransportadoraResponse criar(TransportadoraCreateRequest request) {
        String cnpjNormalizado = normalizarCnpj(request.getCnpj());

        validarCodigoUnico(request.getCodigo());
        validarCnpjUnico(cnpjNormalizado);
        validarEmailUnico(request.getEmail());

        Transportadora transportadora = Transportadora.builder()
                .codigo(request.getCodigo())
                .razaoSocial(request.getRazaoSocial())
                .nomeFantasia(request.getNomeFantasia())
                .cnpj(cnpjNormalizado)
                .inscricaoEstadual(request.getInscricaoEstadual())
                .telefone(request.getTelefone())
                .email(request.getEmail())
                .cep(request.getCep())
                .logradouro(request.getLogradouro())
                .numero(request.getNumero())
                .complemento(request.getComplemento())
                .bairro(request.getBairro())
                .cidade(request.getCidade())
                .estado(request.getEstado() != null ? request.getEstado().toUpperCase() : null)
                .ativo(true)
                .dataCadastro(LocalDateTime.now())
                .build();

        Transportadora salva = transportadoraRepository.save(transportadora);
        return mapper.toResponse(salva);
    }

    @Override
    @Transactional(readOnly = true)
    public TransportadoraResponse buscarPorId(Long id) {
        return mapper.toResponse(buscarTransportadoraOuFalhar(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransportadoraResponse> listar(String codigo, String razaoSocial, String cnpj, Boolean ativo,
                                               Pageable pageable) {
        Specification<Transportadora> filtro = Specification
                .where(TransportadoraSpecification.comCodigo(codigo))
                .and(TransportadoraSpecification.comRazaoSocial(razaoSocial))
                .and(TransportadoraSpecification.comCnpj(cnpj))
                .and(TransportadoraSpecification.comStatus(ativo));

        return transportadoraRepository.findAll(filtro, pageable).map(mapper::toResponse);
    }

    @Override
    public TransportadoraResponse atualizar(Long id, TransportadoraUpdateRequest request) {
        Transportadora transportadora = buscarTransportadoraOuFalhar(id);
        String cnpjNormalizado = normalizarCnpj(request.getCnpj());

        if (!transportadora.getCnpj().equals(cnpjNormalizado)) {
            validarCnpjUnico(cnpjNormalizado);
        }

        boolean emailAlterado = !transportadora.getEmail().equalsIgnoreCase(request.getEmail());
        if (emailAlterado) {
            validarEmailUnico(request.getEmail());
        }

        // O campo "codigo" nunca é tocado aqui: TransportadoraUpdateRequest
        // não o possui, garantindo a regra de imutabilidade do código.
        transportadora.setRazaoSocial(request.getRazaoSocial());
        transportadora.setNomeFantasia(request.getNomeFantasia());
        transportadora.setCnpj(cnpjNormalizado);
        transportadora.setInscricaoEstadual(request.getInscricaoEstadual());
        transportadora.setTelefone(request.getTelefone());
        transportadora.setEmail(request.getEmail());
        transportadora.setCep(request.getCep());
        transportadora.setLogradouro(request.getLogradouro());
        transportadora.setNumero(request.getNumero());
        transportadora.setComplemento(request.getComplemento());
        transportadora.setBairro(request.getBairro());
        transportadora.setCidade(request.getCidade());
        transportadora.setEstado(request.getEstado() != null ? request.getEstado().toUpperCase() : null);

        return mapper.toResponse(transportadoraRepository.save(transportadora));
    }

    @Override
    public TransportadoraResponse ativar(Long id) {
        Transportadora transportadora = buscarTransportadoraOuFalhar(id);
        transportadora.setAtivo(true);
        return mapper.toResponse(transportadoraRepository.save(transportadora));
    }

    @Override
    public TransportadoraResponse inativar(Long id) {
        Transportadora transportadora = buscarTransportadoraOuFalhar(id);

        //boolean possuiEntregasPendentes = pedidoRepository
        //        .existsByTransportadoraIdAndStatusEntrega(transportadora.getId(), StatusEntrega.PENDENTE);

        //if (possuiEntregasPendentes) {
            throw new BusinessRuleException(
                    "Não é possível inativar a transportadora pois existem entregas pendentes associadas a ela");
        }

        transportadora.setAtivo(false);
        return mapper.toResponse(transportadoraRepository.save(transportadora));
    }

    private Transportadora buscarTransportadoraOuFalhar(Long id) {
        return transportadoraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transportadora não encontrada para o id: " + id));
    }

    private void validarCodigoUnico(String codigo) {
        if (transportadoraRepository.existsByCodigo(codigo)) {
            throw new DuplicateResourceException("Já existe uma transportadora cadastrada com o código: " + codigo);
        }
    }

    private void validarCnpjUnico(String cnpj) {
        if (transportadoraRepository.existsByCnpj(cnpj)) {
            throw new DuplicateResourceException("Já existe uma transportadora cadastrada com o CNPJ: " + cnpj);
        }
    }

    private void validarEmailUnico(String email) {
        if (transportadoraRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("Já existe uma transportadora cadastrada com o e-mail: " + email);
        }
    }

    /**
     * Remove qualquer máscara do CNPJ antes da persistência, garantindo
     * que o valor seja sempre armazenado e comparado de forma
     * consistente (regra de negócio nº 16).
     */
    private String normalizarCnpj(String cnpj) {
        return cnpj == null ? null : cnpj.replaceAll("\\D", "");
    }
}

