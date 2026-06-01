package br.com.tecloja.api.service;

import br.com.tecloja.api.dto.ProdutoDTO;
import br.com.tecloja.api.dto.ProdutoFormDTO;
import java.util.List;

public interface ProdutoService {
    List<ProdutoDTO> listarTodos();
    ProdutoDTO buscarPorId(Long id);
    List<ProdutoDTO> buscarPorCategoria(Long categoriaId);
    List<ProdutoDTO> pesquisarPorNome(String busca);
    ProdutoDTO criar(ProdutoFormDTO form);
    ProdutoDTO atualizar(Long id, ProdutoFormDTO form);
    void deletar(Long id);
}
