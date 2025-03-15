package expr;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DerivativeFactor extends Factor {
    private static final Pattern derivativeRe = Pattern.compile("dx\\((.*)\\)");
    private ArrayList<AtomicElement> cachedAtoms = new ArrayList();
    private ArrayList<AtomicElement> cachedDerivatives = new ArrayList();

    public DerivativeFactor(String factor) {
        super(factor);
    }

    @Override
    public ArrayList<AtomicElement> getAtomicElements() {
        if (!cachedAtoms.isEmpty()) {
            return cachedAtoms;
        }
        cachedAtoms = derive();
        return cachedAtoms;
    }

    @Override
    public ArrayList<AtomicElement> derive() {
        if (!cachedDerivatives.isEmpty()) {
            return cachedDerivatives;
        }
        Matcher m = derivativeRe.matcher(getFactor());
        if (m.matches()) {
            String s = m.group(1);
            Expr innerExpr = new Expr(s);
            cachedDerivatives = innerExpr.derive();
            return cachedDerivatives;
        } else {
            System.err.println("Invalid derivative factor: " + getFactor());
        }
        return null;
    }
}
