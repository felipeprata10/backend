package br.com.tecloja.api.controller;

import br.com.tecloja.api.dto.CategoriaDTO;
import br.com.tecloja.api.repository.CategoriaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Endpoint público para listagem de categorias.
 * Não requer autenticação JWT (liberado no SecurityConfig para GET /api/v1/categorias/**).
 * Acessa o repository diretamente: query simples sem regra de negócio (padrão CQRS).
 */
@RestController
@RequestMapping("/api/v1/categorias")
public class CategoriaController {

    private final CategoriaRepository categoriaRepository;

    public CategoriaController(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> listarTodas() {
        List<CategoriaDTO> categorias = categoriaRepository.findAll().stream()
            .map(c -> new CategoriaDTO(c.getId(), c.getNome()))
            .collect(Collectors.toList());
        return ResponseEntity.ok(categorias);
    }
}
