package br.com.tecloja.api.config;

import br.com.tecloja.api.model.*;
import br.com.tecloja.api.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Set;

@Component
public class DataSeeder implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;
    private final PapelRepository papelRepository;
    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;

    public DataSeeder(CategoriaRepository categoriaRepository, ProdutoRepository produtoRepository,
                      PapelRepository papelRepository, UsuarioRepository usuarioRepository,
                      ClienteRepository clienteRepository) {
        this.categoriaRepository = categoriaRepository;
        this.produtoRepository = produtoRepository;
        this.papelRepository = papelRepository;
        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // 1. Criar Papéis de Segurança
        if (papelRepository.count() == 0) {
            papelRepository.save(new Papel(null, "ROLE_USER"));
            papelRepository.save(new Papel(null, "ROLE_ADMIN"));
        }

        Papel papelUser = papelRepository.findByNome("ROLE_USER").orElse(null);
        Papel papelAdmin = papelRepository.findByNome("ROLE_ADMIN").orElse(null);

        // 2. Criar Usuários Administrativos / Clientes Didáticos
        if (usuarioRepository.count() == 0) {
            Usuario admin = new Usuario();
            admin.setUsername("admin@tecloja.com");
            admin.setSenha(encoder.encode("admin123"));
            admin.setPapeis(Set.of(papelAdmin, papelUser));
            usuarioRepository.save(admin);

            Usuario user = new Usuario();
            user.setUsername("usuario@email.com");
            user.setSenha(encoder.encode("usuario123"));
            user.setPapeis(Set.of(papelUser));
            usuarioRepository.save(user);
        }

        if (clienteRepository.count() == 0) {
            Cliente cliente = new Cliente();
            cliente.setNome("Usuário Padrão");
            cliente.setEmail("usuario@email.com");
            cliente.setCpf("123.456.789-00");
            clienteRepository.save(cliente);
        }

        // 3. Criar Categoria e Produtos de Tecnologia
        if (categoriaRepository.count() == 0) {
            Categoria smartphones = categoriaRepository.save(new Categoria(null, "Smartphones"));
            Categoria notebooks = categoriaRepository.save(new Categoria(null, "Notebooks"));
            Categoria acessorios = categoriaRepository.save(new Categoria(null, "Acessórios"));

            // Alimentar Smartphones
            Produto p1 = new Produto();
            p1.setNome("iPhone 15 Pro Max");
            p1.setDescricao("Processador A17 Pro, Câmera tripla de 48MP, 256GB");
            p1.setPreco(new BigDecimal("9499.00"));
            p1.setEstoque(15);
            p1.setCategoria(smartphones);
            produtoRepository.save(p1);

            Produto p2 = new Produto();
            p2.setNome("Samsung Galaxy S24 Ultra");
            p2.setDescricao("Snapdragon 8 Gen 3, Caneta S-Pen, Câmera 200MP, 512GB");
            p2.setPreco(new BigDecimal("7999.00"));
            p2.setEstoque(20);
            p2.setCategoria(smartphones);
            produtoRepository.save(p2);

            // Alimentar Notebooks
            Produto p3 = new Produto();
            p3.setNome("MacBook Air M2");
            p3.setDescricao("Processador Apple M2, Tela Liquid Retina de 13.6, 8GB RAM, SSD 256GB");
            p3.setPreco(new BigDecimal("8200.00"));
            p3.setEstoque(8);
            p3.setCategoria(notebooks);
            produtoRepository.save(p3);

            Produto p4 = new Produto();
            p4.setNome("Notebook Gamer Dell G15");
            p4.setDescricao("Intel Core i7, Nvidia RTX 4050, 16GB RAM, SSD 512GB");
            p4.setPreco(new BigDecimal("5899.00"));
            p4.setEstoque(10);
            p4.setCategoria(notebooks);
            produtoRepository.save(p4);

            // Alimentar Acessórios
            Produto p5 = new Produto();
            p5.setNome("Fone Headphone Sony WH-1000XM5");
            p5.setDescricao("Cancelamento ativo de ruído inteligente, Bateria de 30 horas");
            p5.setPreco(new BigDecimal("2199.00"));
            p5.setEstoque(30);
            p5.setCategoria(acessorios);
            produtoRepository.save(p5);
            
            Produto p6 = new Produto();
            p6.setNome("Teclado Mecânico Logitech MX Mechanical");
            p6.setDescricao("Switches táteis silenciosos de perfil baixo, iluminação inteligente");
            p6.setPreco(new BigDecimal("999.90"));
            p6.setEstoque(25);
            p6.setCategoria(acessorios);
            produtoRepository.save(p6);
        }
    }
}
