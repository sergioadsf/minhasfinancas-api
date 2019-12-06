package br.com.conectasol.minhasfinancas.exception;

@SuppressWarnings("serial")
public class RegraNegocioException extends RuntimeException {

	public RegraNegocioException(String msg) {
		super(msg);
	}
}
