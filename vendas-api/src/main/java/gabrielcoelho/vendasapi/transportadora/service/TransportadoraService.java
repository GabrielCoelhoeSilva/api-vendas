package gabrielcoelho.vendasapi.transportadora.service;

import gabrielcoelho.vendasapi.transportadora.dto.request.TransportadoraCreateRequest;
import gabrielcoelho.vendasapi.transportadora.dto.request.TransportadoraUpdateRequest;
import gabrielcoelho.vendasapi.transportadora.dto.response.TransportadoraResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Contrato de regras de negócio do módulo de Transportadoras.
 * Toda validação de negócio deve ser implementada exclusivamente
 * nesta camada.
 */
public interface TransportadoraService {

    TransportadoraResponse criar(TransportadoraCreateRequest request);

    TransportadoraResponse buscarPorId(Long id);

    Page<TransportadoraResponse> listar(String codigo, String razaoSocial, String cnpj, Boolean ativo, Pageable pageable);

    TransportadoraResponse atualizar(Long id, TransportadoraUpdateRequest request);

    TransportadoraResponse ativar(Long id);

    TransportadoraResponse inativar(Long id);
}

