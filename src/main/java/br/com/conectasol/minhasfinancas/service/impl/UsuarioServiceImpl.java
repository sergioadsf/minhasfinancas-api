package br.com.conectasol.minhasfinancas.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.conectasol.minhasfinancas.exception.ErroAutenticacao;
import br.com.conectasol.minhasfinancas.exception.RegraNegocioException;
import br.com.conectasol.minhasfinancas.model.entity.Usuario;
import br.com.conectasol.minhasfinancas.model.repository.UsuarioRepository;
import br.com.conectasol.minhasfinancas.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	private UsuarioRepository repository;

	@Autowired
	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> optUsuario = repository.findByEmail(email); 
		
		if(!optUsuario.isPresent()) {
			throw new ErroAutenticacao("Usúario não encontrado para o email informado.");
		}
		
		Usuario usuario = optUsuario.get();
		if(!usuario.getSenha().equals(senha)) {
			throw new ErroAutenticacao("Senha inválida.");
		}
		return usuario;
	}

	@Override
	@Transactional
	public Usuario salvar(Usuario usuario) {
		this.validarEmail(usuario.getEmail());
		return repository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		boolean exists = this.repository.existsByEmail(email);
		if(exists) {
			throw new RegraNegocioException("Já existe um usuário cadastrado com este email!");
		}
	}

}
