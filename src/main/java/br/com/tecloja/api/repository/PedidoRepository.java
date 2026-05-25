package br.com.tecloja.api.repository;

import br.com.tecloja.api.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // Evita o problema N+1 carregando Pedido, Cliente e Itens em um único SELECT
    @Query("SELECT DISTINCT p FROM Pedido p JOIN FETCH p.cliente JOIN FETCH p.itens ip JOIN FETCH ip.produto WHERE p.cliente.id = :clienteId")
    List<Pedido> findPedidosCompletosPorCliente(@Param("clienteId") Long clienteId);
}
