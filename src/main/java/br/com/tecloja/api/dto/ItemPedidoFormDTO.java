package br.com.tecloja.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

// Item do carrinho recebido no formulário de checkout
public record ItemPedidoFormDTO(
    @NotNull(message = "O ID do produto é obrigatório")
    Long produtoId,

    @Min(value = 1, message = "A quantidade mínima por produto é 1")
    int quantidade
) {}
