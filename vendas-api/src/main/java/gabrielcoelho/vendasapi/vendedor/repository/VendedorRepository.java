package gabrielcoelho.vendasapi.vendedor.repository;


import gabrielcoelho.vendasapi.vendedor.entity.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface VendedorRepository extends
        JpaRepository<Vendedor, Long>,
        JpaSpecificationExecutor<Vendedor> {

    boolean existsByCodigo(String codigo);

    boolean existsByEmail(String email);

    Optional<Vendedor> findByCodigo(String codigo);

    Optional<Vendedor> findByEmail(String email);
}

