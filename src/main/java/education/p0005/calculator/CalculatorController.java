package education.p0005.calculator;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CalculatorController {
    @GetMapping(path = "/calc")
    public String index() {
        return "calculator/index";
    }
}
