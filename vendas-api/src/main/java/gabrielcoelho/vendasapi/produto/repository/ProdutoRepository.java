package gabrielcoelho.vendasapi.produto.repository;

import gabrielcoelho.vendasapi.produto.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository <Produto, Long> {
}
