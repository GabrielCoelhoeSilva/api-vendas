package gabrielcoelho.vendasapi.vendedor.service;

import gabrielcoelho.vendasapi.vendedor.dto.request.VendedorCreateRequest;
import gabrielcoelho.vendasapi.vendedor.dto.request.VendedorUpdateRequest;
import gabrielcoelho.vendasapi.vendedor.dto.response.VendedorResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VendedorService {

    VendedorResponse criar(VendedorCreateRequest request);

    VendedorResponse buscarPorId(Long id);

    Page<VendedorResponse> listar(String codigo, String nome, Boolean ativo, Pageable pageable);

    VendedorResponse atualizar(Long id, VendedorUpdateRequest request);

    VendedorResponse ativar(Long id);

    VendedorResponse inativar(Long id);
}

