package br.com.tecloja.api.controller;

import br.com.tecloja.api.dto.PedidoDTO;
import br.com.tecloja.api.dto.PedidoFormDTO;
import br.com.tecloja.api.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<PedidoDTO> realizarPedido(@Valid @RequestBody PedidoFormDTO form) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.realizarPedido(form));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PedidoDTO>> listarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(pedidoService.listarPedidosPorCliente(clienteId));
    }
}
