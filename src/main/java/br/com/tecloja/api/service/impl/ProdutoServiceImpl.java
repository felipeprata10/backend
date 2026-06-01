package br.com.tecloja.api.service.impl;

import br.com.tecloja.api.dto.ProdutoDTO;
import br.com.tecloja.api.dto.ProdutoFormDTO;
import br.com.tecloja.api.exception.ResourceNotFoundException;
import br.com.tecloja.api.mapper.ProdutoMapper;
import br.com.tecloja.api.model.Categoria;
import br.com.tecloja.api.model.Produto;
import br.com.tecloja.api.repository.CategoriaRepository;
import br.com.tecloja.api.repository.ProdutoRepository;
import br.com.tecloja.api.service.ProdutoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProdutoServiceImpl(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoDTO> listarTodos() {
        return produtoRepository.findAll().stream()
            .filter(Produto::isAtivo)
            .map(ProdutoMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProdutoDTO buscarPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
            .filter(Produto::isAtivo)
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado ou inativo com o ID: " + id));
        return ProdutoMapper.toDTO(produto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoDTO> buscarPorCategoria(Long categoriaId) {
        return produtoRepository.findByCategoriaId(categoriaId).stream()
            .filter(Produto::isAtivo)
            .map(ProdutoMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoDTO> pesquisarPorNome(String busca) {
        return produtoRepository.pesquisarPorNome(busca).stream()
            .filter(Produto::isAtivo)
            .map(ProdutoMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProdutoDTO criar(ProdutoFormDTO form) {
        Categoria categoria = categoriaRepository.findById(form.categoriaId())
            .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com o ID: " + form.categoriaId()));
        Produto produto = ProdutoMapper.toEntity(form, categoria);
        Produto salvo = produtoRepository.save(produto);
        return ProdutoMapper.toDTO(salvo);
    }

    @Override
    @Transactional
    public ProdutoDTO atualizar(Long id, ProdutoFormDTO form) {
        Produto produto = produtoRepository.findById(id)
            .filter(Produto::isAtivo)
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado ou inativo com o ID: " + id));
        
        Categoria categoria = categoriaRepository.findById(form.categoriaId())
            .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com o ID: " + form.categoriaId()));

        produto.setNome(form.nome());
        produto.setDescricao(form.descricao());
        produto.setPreco(form.preco());
        produto.setEstoque(form.estoque());
        produto.setCategoria(categoria);

        Produto atualizado = produtoRepository.save(produto);
        return ProdutoMapper.toDTO(atualizado);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        Produto produto = produtoRepository.findById(id)
            .filter(Produto::isAtivo)
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado ou inativo com o ID: " + id));
        
        // Em vez de deletar fisicamente, inativamos o produto (Soft Delete)
        produto.setAtivo(false);
        produtoRepository.save(produto);
    }
}
