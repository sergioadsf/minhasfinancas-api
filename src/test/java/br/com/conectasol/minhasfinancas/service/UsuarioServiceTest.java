package br.com.conectasol.minhasfinancas.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.conectasol.minhasfinancas.exception.ErroAutenticacao;
import br.com.conectasol.minhasfinancas.exception.RegraNegocioException;
import br.com.conectasol.minhasfinancas.model.entity.Usuario;
import br.com.conectasol.minhasfinancas.model.repository.UsuarioRepository;
import br.com.conectasol.minhasfinancas.service.impl.UsuarioServiceImpl;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

	@SpyBean
	UsuarioServiceImpl service;
	
	@MockBean
	UsuarioRepository repository;
	
	@Test(expected = Test.None.class)
	public void deveSalvarUmUsuario( ) {
		// cenário
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		Usuario usuario = Usuario.builder()
				.id(1L)
				.nome("nome")
				.email("email@gmail.com")
				.senha("senha").build();
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		// ação
		Usuario usuarioSalvo = service.salvar(new Usuario());
		
		// verificação
		Assertions.assertThat(usuarioSalvo).isNotNull();
		Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1L);
		Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("nome");
		Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("email@gmail.com");
		Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");
	}
	
	@Test(expected = RegraNegocioException.class)
	public void naoDeveSalvarUmUsuarioComEmailJaCadastrado( ) {
		// cenário
		String email = "email@gmail.com";
		Usuario usuario = Usuario.builder()
				.email(email)
				.build();
		Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);
		
		// ação
		service.salvar(usuario);
		
		// verificação
		Mockito.verify(repository, Mockito.never()).save(usuario);
	}
	
	@Test(expected = Test.None.class)
	public void deveAutenticarUmUsuarioComSucesso() {
		// cenário
		String email = "email@gmail.com";
		String senha = "senha";
		
		Usuario usuario = Usuario.builder().email(email).senha(senha).id(1L).build();
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));
		
		// ação 
		Usuario result = service.autenticar(email, senha);
		
		// verificação
		Assertions.assertThat(result).isNotNull();
	}
	
	@Test
	public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComEmailInformado() {
		
		// cenário
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		
		// ação
		Throwable throwable = Assertions.catchThrowable(() -> service.autenticar("email@gmail.com", "123") );
		
		// verificação
		Assertions.assertThat(throwable).isInstanceOf(ErroAutenticacao.class).hasMessage("Usúario não encontrado para o email informado.");
	}
	
	@Test
	public void deveLancarErroQuandoSenhaNaoBater() {
		String senha = "senha";
		// cenário
		Usuario usuario = Usuario.builder().email("email@gmail.com").senha(senha).build();
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
		
		// ação
		Throwable throwable = Assertions.catchThrowable(() -> service.autenticar("email@gmail.com", "123") );
		Assertions.assertThat(throwable).isInstanceOf(ErroAutenticacao.class).hasMessage("Senha inválida.");
	}

	@Test(expected = Test.None.class)
	public void deveValidarEmail() {
		// cenário
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);

		// ação
		service.validarEmail("email@gmail.com");
	}

	@Test(expected = RegraNegocioException.class)
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		// cenário
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
		
		// ação
		service.validarEmail("email@gmail.com");
	}
}
