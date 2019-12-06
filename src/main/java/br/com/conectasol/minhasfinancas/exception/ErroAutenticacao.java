package br.com.conectasol.minhasfinancas.exception;

@SuppressWarnings("serial")
public class ErroAutenticacao extends RuntimeException {

	public ErroAutenticacao(String msg) {
		super(msg);
	}
}
