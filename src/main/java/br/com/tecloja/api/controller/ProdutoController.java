package br.com.tecloja.api.controller;

import br.com.tecloja.api.dto.ProdutoDTO;
import br.com.tecloja.api.dto.ProdutoFormDTO;
import br.com.tecloja.api.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public ResponseEntity<List<ProdutoDTO>> listarTodos() {
        return ResponseEntity.ok(produtoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }

    @GetMapping("/categoria/{id}")
    public ResponseEntity<List<ProdutoDTO>> buscarPorCategoria(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.buscarPorCategoria(id));
    }

    @GetMapping("/pesquisa")
    public ResponseEntity<List<ProdutoDTO>> pesquisarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(produtoService.pesquisarPorNome(nome));
    }

    @PostMapping
    public ResponseEntity<ProdutoDTO> criar(@Valid @RequestBody ProdutoFormDTO form) {
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.criar(form));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoDTO> atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoFormDTO form) {
        return ResponseEntity.ok(produtoService.atualizar(id, form));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
