package com.tcc.logica.controller;

import com.tcc.logica.dto.FormulaRequest;
import com.tcc.logica.model.Expr;
import com.tcc.logica.model.TruthTable;
import com.tcc.logica.service.FormulaParser;
import com.tcc.logica.service.TruthTableGenerator;
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
