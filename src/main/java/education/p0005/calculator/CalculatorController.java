package education.p0005.calculator;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

@Controller
public class CalculatorController {

    @Autowired
    CalculatorSession calculatorSession;

    @GetMapping(path = "/calc")
    public String index(Model model) {
        CalcResult page = calculatorSession.calcResult;
        calculatorSession.calcResult.calcError = false;
        model.addAttribute("page", page);
        return "calculator/index";
    }

    @PostMapping(path = "/calc/operator")
    public String setOperator(@RequestParam("inputTxt")String inputTxt, @RequestParam("inputOperator")String inputOperator, Model model) {
        calculatorSession.calcResult.formula += inputTxt + inputOperator;
        return "redirect:/calc";
    }

    @PostMapping(path = "/calc/calculation")
    public String calculation(@RequestParam("inputTxt")String inputTxt, Model model) {
        String calcFormula = calculatorSession.calcResult.formula + inputTxt;
        CalcResult calcResult = new CalcResult();

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");

        try {
            calcResult.result = (Integer) engine.eval(calcFormula);
        } catch(Exception e) {
            calcResult.calcError = true;
        }

        calcResult.memory = calculatorSession.calcResult.memory;
        calculatorSession.calcResult = calcResult;
        return "redirect:/calc";
    }

    @PostMapping(path = "/calc/memory")
    public String calcMemory(@RequestParam("inputTxt")String inputTxt, @RequestParam("calcMemory")String calcMemory, Model model) {
        if (calcMemory.equals("MC")) {
            calculatorSession.calcResult.memory = 0;
            return "redirect:/calc";
        } else if (calcMemory.equals("MR")) {
            calculatorSession.calcResult.result = calculatorSession.calcResult.memory;
            return "redirect:/calc";
        }

        try {
            int num = Integer.parseInt(inputTxt);

            if (calcMemory.equals("M+") || calcMemory.equals("MS")) {
                calculatorSession.calcResult.memory += num;
            } else if (calcMemory.equals("M-")) {
                calculatorSession.calcResult.memory -= num;
            }

        } catch (Exception e) {
            calculatorSession.calcResult.calcError = true;
        }
        return "redirect:/calc";
    }

    @PostMapping(path = "/calc/clearFormula")
    public String clearFormula(Model model) {
        CalcResult calcResult = new CalcResult();
        calcResult.memory = calculatorSession.calcResult.memory;
        calculatorSession.calcResult = calcResult;
        return "redirect:/calc";
    }
}
