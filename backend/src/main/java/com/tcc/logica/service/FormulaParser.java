package com.tcc.logica.service;

import com.tcc.logica.exception.FormulaSyntaxException;
import com.tcc.logica.model.BinaryOperator;
import com.tcc.logica.model.Expr;
import com.tcc.logica.model.Token;
import com.tcc.logica.model.TokenType;
import java.util.List;

/**
 * Recursive-descent parser. Precedence, highest to lowest: NOT, AND, OR, IMPLIES, IFF.
 * All binary operators are left-associative.
 */
public final class FormulaParser {

    /**
     * Parsing and evaluation both recurse once per operator, so an unbounded
     * formula risks a StackOverflowError under deep/wide nesting (e.g. thousands
     * of chained "&"). Rejecting oversized input up front turns that into a
     * cheap, predictable 400 instead of relying on a JVM stack crash being
     * caught gracefully.
     */
    public static final int MAX_FORMULA_LENGTH = 200;

    private final List<Token> tokens;
    private int pos = 0;

    private FormulaParser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public static Expr parse(String formula) {
        if (formula.length() > MAX_FORMULA_LENGTH) {
            throw new FormulaSyntaxException(
                    "Fórmula excede o limite de " + MAX_FORMULA_LENGTH + " caracteres.", MAX_FORMULA_LENGTH);
        }
        List<Token> tokens = new Lexer(formula).tokenize();
        FormulaParser parser = new FormulaParser(tokens);
        Expr expr = parser.parseIff();
        parser.expect(TokenType.EOF);
        return expr;
    }

    private Expr parseIff() {
        Expr left = parseImplies();
        while (peek().type() == TokenType.IFF) {
            advance();
            Expr right = parseImplies();
            left = new Expr.Binary(BinaryOperator.IFF, left, right);
        }
        return left;
    }

    private Expr parseImplies() {
        Expr left = parseOr();
        while (peek().type() == TokenType.IMPLIES) {
            advance();
            Expr right = parseOr();
            left = new Expr.Binary(BinaryOperator.IMPLIES, left, right);
        }
        return left;
    }

    private Expr parseOr() {
        Expr left = parseAnd();
        while (peek().type() == TokenType.OR) {
            advance();
            Expr right = parseAnd();
            left = new Expr.Binary(BinaryOperator.OR, left, right);
        }
        return left;
    }

    private Expr parseAnd() {
        Expr left = parseNot();
        while (peek().type() == TokenType.AND) {
            advance();
            Expr right = parseNot();
            left = new Expr.Binary(BinaryOperator.AND, left, right);
        }
        return left;
    }

    private Expr parseNot() {
        if (peek().type() == TokenType.NOT) {
            advance();
            return new Expr.Not(parseNot());
        }
        return parsePrimary();
    }

    private Expr parsePrimary() {
        Token t = peek();
        if (t.type() == TokenType.VAR) {
            advance();
            return new Expr.Var(t.text());
        }
        if (t.type() == TokenType.LPAREN) {
            advance();
            Expr e = parseIff();
            expect(TokenType.RPAREN);
            return e;
        }
        if (t.type() == TokenType.EOF) {
            throw new FormulaSyntaxException("Fórmula incompleta.", t.position());
        }
        throw new FormulaSyntaxException("Token inesperado: '" + t.text() + "'", t.position());
    }

    private Token peek() {
        return tokens.get(pos);
    }

    private Token advance() {
        return tokens.get(pos++);
    }

    private void expect(TokenType type) {
        if (peek().type() != type) {
            throw new FormulaSyntaxException(
                    "Esperado " + type + " mas encontrado '" + peek().text() + "'", peek().position());
        }
        advance();
    }
}
