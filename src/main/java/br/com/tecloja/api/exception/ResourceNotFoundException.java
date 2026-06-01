package br.com.tecloja.api.exception;

// Erros 404 (Recurso Não Encontrado)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
