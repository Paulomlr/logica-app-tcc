package com.tcc.logica.service;

import com.tcc.logica.exception.FormulaSyntaxException;
import com.tcc.logica.model.Token;
import com.tcc.logica.model.TokenType;
import java.util.ArrayList;
import java.util.List;

/**
 * Accepts both ASCII (!, &, |, ->, <->) and Unicode (¬, ∧, ∨, →, ↔) operator
 * notations, since students may type either depending on their keyboard.
 * Variables must start with a lowercase letter (a-z), matching classroom convention.
 */
class Lexer {

    private final String input;
    private int pos = 0;

    Lexer(String input) {
        this.input = input;
    }

    List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        while (pos < input.length()) {
            char c = input.charAt(pos);

            if (Character.isWhitespace(c)) {
                pos++;
                continue;
            }
            if (c == '(') {
                tokens.add(new Token(TokenType.LPAREN, "(", pos));
                pos++;
                continue;
            }
            if (c == ')') {
                tokens.add(new Token(TokenType.RPAREN, ")", pos));
                pos++;
                continue;
            }
            if (c == '!' || c == '~' || c == '¬') {
                tokens.add(new Token(TokenType.NOT, String.valueOf(c), pos));
                pos++;
                continue;
            }
            if (c == '&' || c == '∧') {
                tokens.add(new Token(TokenType.AND, String.valueOf(c), pos));
                pos++;
                continue;
            }
            if (c == '|' || c == '∨') {
                tokens.add(new Token(TokenType.OR, String.valueOf(c), pos));
                pos++;
                continue;
            }
            if (c == '→') {
                tokens.add(new Token(TokenType.IMPLIES, "→", pos));
                pos++;
                continue;
            }
            if (c == '↔') {
                tokens.add(new Token(TokenType.IFF, "↔", pos));
                pos++;
                continue;
            }
            if (c == '<' && peek(1) == '-' && peek(2) == '>') {
                tokens.add(new Token(TokenType.IFF, "<->", pos));
                pos += 3;
                continue;
            }
            if (c == '-' && peek(1) == '>') {
                tokens.add(new Token(TokenType.IMPLIES, "->", pos));
                pos += 2;
                continue;
            }
            if (c >= 'a' && c <= 'z') {
                int start = pos;
                pos++;
                while (pos < input.length() && Character.isLetterOrDigit(input.charAt(pos))) {
                    pos++;
                }
                tokens.add(new Token(TokenType.VAR, input.substring(start, pos), start));
                continue;
            }
            if (Character.isLetter(c)) {
                throw new FormulaSyntaxException(
                        "Variáveis devem começar com letra minúscula (ex: p, q, r). Encontrado: '" + c + "'", pos);
            }

            throw new FormulaSyntaxException("Caractere inválido: '" + c + "'", pos);
        }
        tokens.add(new Token(TokenType.EOF, "", pos));
        return tokens;
    }

    private char peek(int offset) {
        int i = pos + offset;
        return i < input.length() ? input.charAt(i) : '\0';
    }
}
