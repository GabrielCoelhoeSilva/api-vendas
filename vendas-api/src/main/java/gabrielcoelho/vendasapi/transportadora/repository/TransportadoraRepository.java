package gabrielcoelho.vendasapi.transportadora.repository;

import gabrielcoelho.vendasapi.transportadora.entity.Transportadora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface TransportadoraRepository extends
        JpaRepository<Transportadora, Long>,
        JpaSpecificationExecutor<Transportadora> {

    boolean existsByCodigo(String codigo);

    boolean existsByCnpj(String cnpj);

    boolean existsByEmail(String email);

    Optional<Transportadora> findByCodigo(String codigo);

    Optional<Transportadora> findByCnpj(String cnpj);

    Optional<Transportadora> findByEmail(String email);
}
