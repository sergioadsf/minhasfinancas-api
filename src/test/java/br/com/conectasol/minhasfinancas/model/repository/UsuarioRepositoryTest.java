package br.com.conectasol.minhasfinancas.model.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.conectasol.minhasfinancas.model.entity.Usuario;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryTest {

	@Autowired
	UsuarioRepository repository;

	@Autowired
	TestEntityManager entityManager;

	@Test
	public void deveVeirifcarAExistenciaDeUmEmail() {
		// cenário
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);

		// ação/ execução
		boolean exists = repository.existsByEmail("usuario@gmail.com");

		// verificação
		Assertions.assertThat(exists).isTrue();
	}

	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail() {

		// ação
		boolean exists = repository.existsByEmail("usuario@gmail.com");

		// verificação
		Assertions.assertThat(exists).isFalse();
	}

	@Test
	public void devePersistirUmUsuarioNaBaseDeDados() {
		// cenário
		Usuario usuario = criarUsuario();
		
		// ação
		Usuario usuarioSalvo = repository.save(usuario);
		
		// verificação
		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
	}

	@Test
	public void deveBuscarUmUsuarioPorEmail() {
		// cenário
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);

		// ação
		Optional<Usuario> optUsuario = repository.findByEmail("usuario@gmail.com");
		
		// verificação
		Assertions.assertThat(optUsuario.isPresent()).isTrue();
	}

	@Test
	public void deveRetornarVazioAoBuscarUmUsuarioPorEmailQuandoNaoExisteNaBase() {
		// cenário
		
		// ação
		Optional<Usuario> optUsuario = repository.findByEmail("usuario@gmail.com");
		
		// verificação
		Assertions.assertThat(optUsuario.isPresent()).isFalse();
	}
	
	private static Usuario criarUsuario() {
		Usuario usuario = Usuario
				.builder()
				.nome("usuario")
				.email("usuario@gmail.com")
				.senha("senha")
				.build();
		return usuario;
	}

}
