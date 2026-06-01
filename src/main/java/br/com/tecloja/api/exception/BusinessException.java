package br.com.tecloja.api.exception;

// Erros 400 por violações de lógica de negócios (como estoque insuficiente)
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
