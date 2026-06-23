package gabrielcoelho.vendasapi.vendedor.mapper;

import gabrielcoelho.vendasapi.vendedor.entity.Vendedor;
import gabrielcoelho.vendasapi.vendedor.dto.response.VendedorResponse;
import org.springframework.stereotype.Component;

@Component
public class VendedorMapper {

    public VendedorResponse toResponse(Vendedor vendedor) {
        if (vendedor == null) {
            return null;
        }

        return VendedorResponse.builder()
                .id(vendedor.getId())
                .codigo(vendedor.getCodigo())
                .nome(vendedor.getNome())
                .email(vendedor.getEmail())
                .telefone(vendedor.getTelefone())
                .percentualComissao(vendedor.getPercentualComissao())
                .ativo(vendedor.getAtivo())
                .dataCadastro(vendedor.getDataCadastro())
                .build();
    }
}
