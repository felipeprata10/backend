package br.com.tecloja.api.mapper;

import br.com.tecloja.api.dto.ProdutoDTO;
import br.com.tecloja.api.dto.ProdutoFormDTO;
import br.com.tecloja.api.model.Categoria;
import br.com.tecloja.api.model.Produto;

public class ProdutoMapper {

    public static ProdutoDTO toDTO(Produto produto) {
        if (produto == null) return null;
        return new ProdutoDTO(
            produto.getId(),
            produto.getNome(),
            produto.getDescricao(),
            produto.getPreco(),
            produto.getEstoque(),
            produto.isAtivo(),
            produto.getCategoria().getId(),
            produto.getCategoria().getNome()
        );
    }

    public static Produto toEntity(ProdutoFormDTO form, Categoria categoria) {
        if (form == null) return null;
        Produto produto = new Produto();
        produto.setNome(form.nome());
        produto.setDescricao(form.descricao());
        produto.setPreco(form.preco());
        produto.setEstoque(form.estoque());
        produto.setCategoria(categoria);
        return produto;
    }
}
