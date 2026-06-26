package gabrielcoelho.vendasapi.transportadora.repository;

import gabrielcoelho.vendasapi.transportadora.entity.Transportadora;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

/**
 * Especificações JPA utilizadas para compor dinamicamente os filtros
 * de listagem de transportadoras (código, razão social, CNPJ e status).
 */
public final class TransportadoraSpecification {

    private TransportadoraSpecification() {
    }

    public static Specification<Transportadora> comCodigo(String codigo) {
        return (root, query, cb) -> StringUtils.hasText(codigo)
                ? cb.equal(cb.lower(root.get("codigo")), codigo.toLowerCase())
                : cb.conjunction();
    }

    public static Specification<Transportadora> comRazaoSocial(String razaoSocial) {
        return (root, query, cb) -> StringUtils.hasText(razaoSocial)
                ? cb.like(cb.lower(root.get("razaoSocial")), "%" + razaoSocial.toLowerCase() + "%")
                : cb.conjunction();
    }

    public static Specification<Transportadora> comCnpj(String cnpj) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(cnpj)) {
                return cb.conjunction();
            }
            String cnpjSomenteNumeros = cnpj.replaceAll("\\D", "");
            return cb.equal(root.get("cnpj"), cnpjSomenteNumeros);
        };
    }

    public static Specification<Transportadora> comStatus(Boolean ativo) {
        return (root, query, cb) -> ativo != null
                ? cb.equal(root.get("ativo"), ativo)
                : cb.conjunction();
    }
}

