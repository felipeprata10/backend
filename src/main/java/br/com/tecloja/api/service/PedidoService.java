package br.com.tecloja.api.service;

import br.com.tecloja.api.dto.PedidoDTO;
import br.com.tecloja.api.dto.PedidoFormDTO;
import java.util.List;

public interface PedidoService {
    PedidoDTO realizarPedido(PedidoFormDTO form);
    List<PedidoDTO> listarPedidosPorCliente(Long clienteId);
}
