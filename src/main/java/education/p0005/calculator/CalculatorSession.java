package education.p0005.calculator;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope(value="session", proxyMode= ScopedProxyMode.TARGET_CLASS)
public class CalculatorSession implements Serializable {
    private static final long serialVersionUID = 1L;

    String displayNum;
    String calculationNum;
    String memoryNum;
    String alertText;
    String savedCalculationButton;
}
