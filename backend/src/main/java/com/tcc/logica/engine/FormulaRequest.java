package com.tcc.logica.engine;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FormulaRequest(
        @NotBlank(message = "A fórmula não pode estar vazia.")
        @Size(max = FormulaParser.MAX_FORMULA_LENGTH,
                message = "A fórmula não pode ter mais de " + FormulaParser.MAX_FORMULA_LENGTH + " caracteres.")
        String formula) {
}
