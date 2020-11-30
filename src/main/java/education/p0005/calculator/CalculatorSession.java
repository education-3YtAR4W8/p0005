package education.p0005.calculator;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value="session", proxyMode= ScopedProxyMode.TARGET_CLASS)
public class CalculatorSession {
    private static final long serialVersionUID = 1L;

    CalcResult calcResult;
}
