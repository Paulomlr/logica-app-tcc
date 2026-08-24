package com.tcc.logica.engine;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/formulas")
public class FormulaController {

    @PostMapping("/table")
    public TruthTable table(@Valid @RequestBody FormulaRequest request) {
        Expr expr = FormulaParser.parse(request.formula());
        return TruthTableGenerator.generate(expr);
    }
}
