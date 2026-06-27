package gabrielcoelho.vendasapi.vendedor.repository;

import gabrielcoelho.vendasapi.vendedor.entity.Vendedor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

/**
 * Especificações JPA utilizadas para compor dinamicamente os filtros
 * de listagem de vendedores (código, nome e status).
 */
public final class VendedorSpecification {

    private VendedorSpecification() {
    }

    public static Specification<Vendedor> comCodigo(String codigo) {
        return (root, query, cb) -> StringUtils.hasText(codigo)
                ? cb.equal(cb.lower(root.get("codigo")), codigo.toLowerCase())
                : cb.conjunction();
    }

    public static Specification<Vendedor> comNome(String nome) {
        return (root, query, cb) -> StringUtils.hasText(nome)
                ? cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%")
                : cb.conjunction();
    }

    public static Specification<Vendedor> comStatus(Boolean ativo) {
        return (root, query, cb) -> ativo != null
                ? cb.equal(root.get("ativo"), ativo)
                : cb.conjunction();
    }
}

