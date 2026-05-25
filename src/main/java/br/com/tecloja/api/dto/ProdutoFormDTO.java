package br.com.tecloja.api.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

// DTO para cadastrar/atualizar produtos (Dados de Entrada)
public record ProdutoFormDTO(
    @NotBlank(message = "O nome do produto é obrigatório")
    String nome,

    @Size(max = 500, message = "A descrição não pode exceder 500 caracteres")
    String descricao,

    @NotNull(message = "O preço é obrigatório")
    @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero")
    BigDecimal preco,

    @Min(value = 0, message = "O estoque não pode ser negativo")
    int estoque,

    @NotNull(message = "A categoria é obrigatória")
    Long categoriaId
) {}
