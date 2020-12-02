package education.p0005.calculator;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;

@Controller
public class CalculatorController {

    @Autowired
    CalculatorSession calculatorSession;

    @GetMapping(path = "calc")
    public String index(Model model) {

        model.addAttribute("page", initializePage());

        return "calculator/index";
    }


    @PostMapping(path = "calc")
    public String inputReceive(@RequestParam ("inputNum") String inputNum,
                               @RequestParam ("calculationNum") String calculationNum,
                               @RequestParam ("memoryNum") String memoryNum,
                               @RequestParam ("button") String button,
                               @RequestParam ("savedCalculationButton") String savedCalculationButton,
                               Model model) {

        initializeDisplayText();
        saveParam(calculationNum, memoryNum, savedCalculationButton);

        if(isInputInvalid(inputNum, calculationNum, memoryNum, button)
                || isSavedCalculationButtonInvalid(savedCalculationButton)){

            model.addAttribute("page", getPageWhenInputInvalid());
            return "calculator/index";
        }

        //計算結果はメソッド内でセッションに登録
        calculate(inputNum, calculationNum, memoryNum, button, savedCalculationButton);

        savedCalculationButton(button);

                model.addAttribute("page", getPageWhenInputValid());

        return "calculator/index";
    }

    void savedCalculationButton(String button){
        if(button.matches("[\\+\\-\\*/]")) {
            calculatorSession.savedCalculationButton = button;
        }else if(button.equals("=")){
            calculatorSession.savedCalculationButton = calculatorSession.savedCalculationButton;
        }
    }

    CalculatePage initializePage(){
        CalculatePage page = new CalculatePage();
        page.alertText = null;
        page.calculationNum = null;
        page.displayNum = null;
        page.memoryNum = null;
        page.savedCalculationButton = null;

        return page;
    }

    CalculatePage getPageWhenInputInvalid(){
        CalculatePage page = new CalculatePage();
        page.alertText = "入力値が不正です。数値を入力してください。";
        page.displayNum = calculatorSession.displayNum;
        page.calculationNum = calculatorSession.calculationNum;
        page.memoryNum = calculatorSession.memoryNum;
        page.savedCalculationButton = calculatorSession.savedCalculationButton;
        return page;
    }

    CalculatePage getPageWhenInputValid(){
        CalculatePage page = new CalculatePage();
        page.alertText = calculatorSession.alertText;
        page.calculationNum = calculatorSession.calculationNum;
        page.displayNum = calculatorSession.displayNum;
        page.memoryNum = calculatorSession.memoryNum;
        page.savedCalculationButton = calculatorSession.savedCalculationButton;

        return page;
    };


    void initializeDisplayText(){
        calculatorSession.alertText = null;
        calculatorSession.displayNum = null;
    }

    void saveParam(String calculationNum, String memoryNum, String savedCalculationButton){
        calculatorSession.calculationNum = calculationNum;
        calculatorSession.memoryNum = memoryNum;
        calculatorSession.savedCalculationButton = savedCalculationButton;
    }

    void calculate(String inputNum, String calculationNum, String memoryNum, String button, String savedCalculationButton){

        switch(button) {
            case "+":
                calculateAddition(inputNum, calculationNum);
                break;
            case "-":
                calculateSubtraction(inputNum, calculationNum);
                break;
            case "*":
                calculateMultiplication(inputNum, calculationNum);
                break;
            case "/":
                calculateDivision(inputNum, calculationNum);
                break;
            case "M+":
                calculateMemoryAddition(inputNum, memoryNum);
                break;
            case "M-":
                calculateMemorySubtraction(inputNum, memoryNum);
                break;
            case "MS":
                memoryStore(inputNum);
                break;
            case "MR":
                memoryRecall(memoryNum);
                break;
            case "MC":
                memoryClear();
                break;
            case "CE":
                displayClear();
                break;
            case "C":
                clear();
                break;
            case "=":
                calculateEqual(inputNum, calculationNum, savedCalculationButton);
                break;
        }
    }

    void calculateAddition(String inputNum, String calculationNum){
        if(inputNum.isEmpty() && calculationNum.isEmpty()) {
            calculatorSession.calculationNum = "0";
        }else if(inputNum.isEmpty()){
            calculatorSession.calculationNum = calculationNum;
        }else if(calculationNum.isEmpty()){
            calculatorSession.calculationNum = inputNum;
        }else {
            BigDecimal decimalInputNum = new BigDecimal(inputNum);
            BigDecimal decimalCalculationNum = new BigDecimal(calculationNum);
            calculatorSession.calculationNum = decimalCalculationNum.add(decimalInputNum).toString();
        }
    }

    void calculateSubtraction(String inputNum, String calculationNum){
        if(inputNum.isEmpty() && calculationNum.isEmpty()) {
            calculatorSession.calculationNum = "0";
        }else if(inputNum.isEmpty()){
            calculatorSession.calculationNum = calculationNum;
        }else if(calculationNum.isEmpty()){
            calculatorSession.calculationNum = inputNum;
        }else {
            BigDecimal decimalInputNum = new BigDecimal(inputNum);
            BigDecimal decimalCalculationNum = new BigDecimal(calculationNum);
            calculatorSession.calculationNum = decimalCalculationNum.subtract(decimalInputNum).toString();
        }
    }

    void calculateMultiplication(String inputNum, String calculationNum){
        if(inputNum.isEmpty() && calculationNum.isEmpty()) {
            calculatorSession.calculationNum = "0";
        }else if(inputNum.isEmpty()){
            calculatorSession.calculationNum = calculationNum;
        }else if(calculationNum.isEmpty()){
            calculatorSession.calculationNum = inputNum;
        }else {
            BigDecimal decimalInputNum = new BigDecimal(inputNum);
            BigDecimal decimalCalculationNum = new BigDecimal(calculationNum);
            calculatorSession.calculationNum = decimalCalculationNum.multiply(decimalInputNum).toString();
        }
    }

    void calculateDivision(String inputNum, String calculationNum){
        if(inputNum.isEmpty() && calculationNum.isEmpty()) {
            calculatorSession.calculationNum = "0";
        }else if(inputNum.isEmpty()){
            calculatorSession.alertText = "入力値が不正です。数値を入力してください。";
        }else if(calculationNum.isEmpty()){
            calculatorSession.calculationNum = inputNum;
        }else {
            BigDecimal decimalInputNum = new BigDecimal(inputNum);
            BigDecimal decimalCalculationNum = new BigDecimal(calculationNum);
            if (decimalInputNum.equals(0)) {
                calculatorSession.alertText = "0で割ることはできません。";
                return;
            }
            calculatorSession.calculationNum = decimalCalculationNum.divide(decimalInputNum).toString();
        }
    }


    void calculateEqual(String inputNum, String calculationNum, String savedCalculationButton){
        switch(savedCalculationButton){
            case "":
                calculatorSession.displayNum = calculatorSession.calculationNum;
                break;
            case "+":
                calculateAddition(inputNum, calculationNum);
                break;
            case "-":
                calculateSubtraction(inputNum, calculationNum);
                break;
            case "*":
                calculateMultiplication(inputNum, calculationNum);
                break;
            case "/":
                calculateDivision(inputNum, calculationNum);
                break;
            default:
                if(inputNum.isEmpty() && calculationNum.isEmpty()) {
                    calculatorSession.displayNum = "0";
                }else if(inputNum.isEmpty()){
                    calculatorSession.displayNum = calculationNum;
                }else{
                    calculatorSession.displayNum = inputNum;
                }
        }

        if(calculationNum.isEmpty()){
            calculatorSession.displayNum = "0";
        }else {
            calculatorSession.displayNum = calculatorSession.calculationNum;
        }
    }

    void calculateMemoryAddition(String inputNum, String memoryNum){
        BigDecimal decimalInputNum = new BigDecimal(inputNum);
        BigDecimal decimalMemoryNum = new BigDecimal(memoryNum);

        calculatorSession.memoryNum = decimalMemoryNum.add(decimalInputNum).toString();
    }

    void calculateMemorySubtraction(String inputNum, String memoryNum){
        BigDecimal decimalInputNum = new BigDecimal(inputNum);
        BigDecimal decimalMemoryNum = new BigDecimal(memoryNum);

        calculatorSession.memoryNum = decimalMemoryNum.subtract(decimalInputNum).toString();
    }

    void memoryStore(String inputNum){
        calculatorSession.memoryNum = inputNum;
    }

    void memoryRecall(String memoryNum){
        calculatorSession.displayNum = memoryNum;
    }

    void memoryClear(){
        calculatorSession.memoryNum = null;
    }

    void displayClear(){
        //何もしない
    }

    void clear(){
        calculatorSession.calculationNum = null;
    }

    boolean isInputInvalid(String inputNum, String calculationNum, String memoryNum, String button){
        boolean invalid = true;
        switch(button){
            case "+":
            case "-":
            case "*":
            case "/":
                if(inputNum.matches("^[0-9]+$")
                        || (calculationNum.isEmpty() && calculationNum.matches("^[0-9]+$")) ){
                    invalid = false;
                };
                break;
            case "=":
                if((inputNum.matches("^[0-9]+$") || inputNum.isEmpty())
                        || (calculationNum.isEmpty() && calculationNum.matches("^[0-9]+$")) ){
                    invalid = false;
                };
                break;
            case "M+":
            case "M-":
                if(inputNum.matches("^[0-9]+$") && memoryNum.matches("^[0-9]+$")){
                    invalid = false;
                };
                break;
            case "MS":
                if(inputNum.matches("^[0-9]+$")){
                    invalid = false;
                };
                break;
            case "MR":
                if(memoryNum.matches("^[0-9]+$")){
                    invalid = false;
                };
                break;
            case "MC":
            case "CE":
            case "C":
                invalid = false;
                break;
            default:
                break;
        }
        return invalid;
    }

    boolean isSavedCalculationButtonInvalid(String savedCalculationButton){
        boolean invalid = true;
        switch(savedCalculationButton){
            case "+":
            case "-":
            case "*":
            case "/":
            case "=":
                //初期値を対象外
            case "":
                invalid = false;
                break;
            default:
                break;
        }
        return invalid;
    }

    @Getter
    @Setter
    static public class CalculatePage {
        private String alertText;
        private String displayNum;
        private String calculationNum;
        private String memoryNum;
        private String savedCalculationButton;
    }
}
