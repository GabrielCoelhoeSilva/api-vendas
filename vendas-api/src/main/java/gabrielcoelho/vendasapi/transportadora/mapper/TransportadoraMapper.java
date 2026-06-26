package gabrielcoelho.vendasapi.transportadora.mapper;

import gabrielcoelho.vendasapi.transportadora.entity.Transportadora;
import gabrielcoelho.vendasapi.transportadora.dto.response.TransportadoraResponse;
import org.springframework.stereotype.Component;

/**
 * Responsável por converter a entidade Transportadora em seu
 * respectivo DTO de resposta, mantendo a camada de domínio isolada
 * da API.
 */
@Component
public class TransportadoraMapper {

    public TransportadoraResponse toResponse(Transportadora transportadora) {
        if (transportadora == null) {
            return null;
        }

        return TransportadoraResponse.builder()
                .id(transportadora.getId())
                .codigo(transportadora.getCodigo())
                .razaoSocial(transportadora.getRazaoSocial())
                .nomeFantasia(transportadora.getNomeFantasia())
                .cnpj(transportadora.getCnpj())
                .inscricaoEstadual(transportadora.getInscricaoEstadual())
                .telefone(transportadora.getTelefone())
                .email(transportadora.getEmail())
                .cep(transportadora.getCep())
                .logradouro(transportadora.getLogradouro())
                .numero(transportadora.getNumero())
                .complemento(transportadora.getComplemento())
                .bairro(transportadora.getBairro())
                .cidade(transportadora.getCidade())
                .estado(transportadora.getEstado())
                .ativo(transportadora.getAtivo())
                .dataCadastro(transportadora.getDataCadastro())
                .build();
    }
}

