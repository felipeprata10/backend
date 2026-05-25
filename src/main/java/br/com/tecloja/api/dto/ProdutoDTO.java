package br.com.tecloja.api.dto;

import java.math.BigDecimal;

// DTO para retornar produtos (Dados de Saída)
public record ProdutoDTO(
    Long id,
    String nome,
    String descricao,
    BigDecimal preco,
    int estoque,
    Long categoriaId,
    String categoriaNome
) {}
