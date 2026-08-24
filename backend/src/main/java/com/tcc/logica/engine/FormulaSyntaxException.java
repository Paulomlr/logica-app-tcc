package com.tcc.logica.engine;

public class FormulaSyntaxException extends RuntimeException {

    private final int position;

    public FormulaSyntaxException(String message, int position) {
        super(message);
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
