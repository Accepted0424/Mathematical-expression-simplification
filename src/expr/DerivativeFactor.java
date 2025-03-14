package expr;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DerivativeFactor extends Factor {
    private static final Pattern derivativeRe = Pattern.compile("dx\\((.*)\\)");

    public DerivativeFactor(String factor) {
        super(factor);
    }

    @Override
    public ArrayList<AtomicElement> getAtomicElements() {
        return derive();
    }

    @Override
    public ArrayList<AtomicElement> derive() {
        Matcher m = derivativeRe.matcher(getFactor());
        if (m.matches()) {
            String s = m.group(1);
            Expr innerExpr = new Expr(s);
            return innerExpr.derive();
        } else {
            System.err.println("Invalid derivative factor: " + getFactor());
        }
        return null;
    }
}
