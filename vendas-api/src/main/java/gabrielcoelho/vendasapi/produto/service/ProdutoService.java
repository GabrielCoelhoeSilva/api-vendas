package gabrielcoelho.vendasapi.produto.service;

import gabrielcoelho.vendasapi.produto.repository.ProdutoRepository;

public class ProdutoService {

    private ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }
}
