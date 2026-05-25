package br.com.tecloja.api.repository;

import br.com.tecloja.api.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // Utiliza Query Methods (Geração automática baseada em nomenclatura)
    List<Produto> findByCategoriaId(Long categoriaId);

    // Consulta customizada JPQL com busca parcial insensível a maiúsculas
    @Query("SELECT p FROM Produto p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :busca, '%'))")
    List<Produto> pesquisarPorNome(@Param("busca") String busca);
}
