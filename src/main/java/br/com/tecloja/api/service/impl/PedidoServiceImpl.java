package br.com.tecloja.api.service.impl;

import br.com.tecloja.api.dto.ItemPedidoFormDTO;
import br.com.tecloja.api.dto.PedidoDTO;
import br.com.tecloja.api.dto.PedidoFormDTO;
import br.com.tecloja.api.exception.BusinessException;
import br.com.tecloja.api.exception.ResourceNotFoundException;
import br.com.tecloja.api.mapper.PedidoMapper;
import br.com.tecloja.api.model.*;
import br.com.tecloja.api.repository.*;
import br.com.tecloja.api.service.PedidoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    public PedidoServiceImpl(PedidoRepository pedidoRepository, ClienteRepository clienteRepository,
                             ProdutoRepository produtoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
    }

    @Override
    @Transactional // Inicia uma transação ativa. Qualquer erro causará rollback automático do estoque!
    public PedidoDTO realizarPedido(PedidoFormDTO form) {
        log.info("Iniciando faturamento de pedido para o cliente ID: {}", form.clienteId());

        // 1. Validar Cliente
        Cliente cliente = clienteRepository.findById(form.clienteId())
            .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com o ID: " + form.clienteId()));

        // 2. Instanciar Novo Pedido
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setStatus("PAGO"); // Venda simplificada com pagamento imediato aprovado
        pedido.setDataPedido(LocalDateTime.now());

        // 3. Processar Itens e Dar Baixa de Estoque
        for (ItemPedidoFormDTO itemForm : form.itens()) {
            Produto produto = produtoRepository.findById(itemForm.produtoId())
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o ID: " + itemForm.produtoId()));

            // Regra de Negócio: produto inativo não pode ser vendido (Soft Delete)
            if (!produto.isAtivo()) {
                log.warn("Falha no checkout: tentativa de comprar produto inativo '{}' (ID: {}).", produto.getNome(), produto.getId());
                throw new BusinessException(String.format(
                    "O produto '%s' não está mais disponível para venda no catálogo.",
                    produto.getNome()
                ));
            }

            // Regra Crítica de Banco de Dados/Negócio: Validação de Estoque
            if (produto.getEstoque() < itemForm.quantidade()) {
                log.warn("Falha no checkout: estoque insuficiente para o produto '{}' (ID: {}). Solicitado: {}, Disponível: {}", 
                    produto.getNome(), produto.getId(), itemForm.quantidade(), produto.getEstoque());
                throw new BusinessException(String.format(
                    "Estoque insuficiente para o produto '%s'. Disponível: %d, Solicitado: %d.",
                    produto.getNome(), produto.getEstoque(), itemForm.quantidade()
                ));
            }

            // Reduzir estoque físico do produto e salvar
            produto.setEstoque(produto.getEstoque() - itemForm.quantidade());
            produtoRepository.save(produto);

            // Criar ItemPedido com cópia de preço unitário histórico
            ItemPedido item = new ItemPedido();
            item.setProduto(produto);
            item.setQuantidade(itemForm.quantidade());
            item.setPrecoUnitario(produto.getPreco()); // Copia o preço ATUAL do produto

            // Vincula o item ao pedido de forma bidirecional
            pedido.adicionarItem(item);
        }

        // 4. Salvar Pedido e Itens em cascata
        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        log.info("Pedido ID: {} faturado com sucesso para o cliente ID: {}.", pedidoSalvo.getId(), cliente.getId());

        // 5. Retornar DTO faturado
        return PedidoMapper.toDTO(pedidoSalvo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoDTO> listarPedidosPorCliente(Long clienteId) {
        // Valida se o cliente existe antes
        if (!clienteRepository.existsById(clienteId)) {
            throw new ResourceNotFoundException("Cliente não encontrado com o ID: " + clienteId);
        }
        
        return pedidoRepository.findPedidosCompletosPorCliente(clienteId).stream()
            .map(PedidoMapper::toDTO)
            .collect(Collectors.toList());
    }
}
