package br.com.tecloja.api.mapper;

import br.com.tecloja.api.dto.ItemPedidoDTO;
import br.com.tecloja.api.dto.PedidoDTO;
import br.com.tecloja.api.model.Pedido;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class PedidoMapper {

    public static PedidoDTO toDTO(Pedido pedido) {
        if (pedido == null) return null;

        List<ItemPedidoDTO> itensDTO = pedido.getItens().stream()
            .map(item -> new ItemPedidoDTO(
                item.getId(),
                item.getProduto().getId(),
                item.getProduto().getNome(),
                item.getQuantidade(),
                item.getPrecoUnitario(),
                item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade()))
            ))
            .collect(Collectors.toList());

        BigDecimal valorTotal = itensDTO.stream()
            .map(ItemPedidoDTO::subtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new PedidoDTO(
            pedido.getId(),
            pedido.getDataPedido(),
            pedido.getStatus(),
            pedido.getCliente().getId(),
            pedido.getCliente().getNome(),
            itensDTO,
            valorTotal
        );
    }
}
