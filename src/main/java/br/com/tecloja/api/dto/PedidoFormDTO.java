package br.com.tecloja.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

// Formulário de finalização de compra (Checkout)
public record PedidoFormDTO(
    @NotNull(message = "O ID do cliente é obrigatório")
    Long clienteId,

    @NotEmpty(message = "O pedido precisa conter pelo menos um item")
    @Valid
    List<ItemPedidoFormDTO> itens
) {}
