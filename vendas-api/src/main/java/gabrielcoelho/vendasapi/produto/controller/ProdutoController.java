package gabrielcoelho.vendasapi.produto.controller;

import gabrielcoelho.vendasapi.produto.repository.ProdutoRepository;
import gabrielcoelho.vendasapi.produto.dto.ProdutoDto;
import gabrielcoelho.vendasapi.produto.entity.Produto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoRepository repository;

    @GetMapping
    public List<ProdutoDto> getLista(){
        return repository.findAll().stream()
                .map( ProdutoDto::fromModel )
                .collect(Collectors.toList());
    }

    @GetMapping("{id}")
    public ResponseEntity<ProdutoDto> getById (@PathVariable Long id) {
        Optional<Produto> produtoExistente = repository.findById(id);
        if (produtoExistente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        ProdutoDto produto = produtoExistente.map( ProdutoDto::fromModel ).get();
        return ResponseEntity.ok(produto);
    }

    @PostMapping
    public ProdutoDto salvar(@RequestBody ProdutoDto produto) {
        Produto entidadeProduto = produto.toModel();
        repository.save(entidadeProduto);
        return ProdutoDto.fromModel(entidadeProduto);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> atualizar(@PathVariable Long id, @RequestBody ProdutoDto produto) {

        Optional<Produto> produtoExistente = repository.findById(id);

        if(produtoExistente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Produto entidade = produto.toModel();
        entidade.setId(id);
        repository.save(entidade);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        Optional<Produto> produtoExistente = repository.findById(id) ;

        if (produtoExistente.isEmpty()) {
            return  ResponseEntity.notFound().build();
        }

        repository.delete(produtoExistente.get());
        return ResponseEntity.noContent().build();
    }

}



