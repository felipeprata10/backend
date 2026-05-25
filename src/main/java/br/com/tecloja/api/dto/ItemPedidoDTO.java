package br.com.tecloja.api.dto;

import java.math.BigDecimal;

public record ItemPedidoDTO(
    Long id,
    Long produtoId,
    String produtoNome,
    int quantidade,
    BigDecimal precoUnitario,
    BigDecimal subtotal
) {}
