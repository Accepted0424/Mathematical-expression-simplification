package expr;

import tools.Operate;

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
        Matcher m = derivativeRe.matcher(getFactor());
        if (m.matches()) {
            String s = m.group(1);
            Expr innerExpr = new Expr(s);
            cachedAtoms = innerExpr.derive();
            return cachedAtoms;
        } else {
            System.err.println("Invalid derivative factor: " + getFactor());
        }
        return null;
    }

    @Override
    public ArrayList<AtomicElement> derive() {
        if (!cachedDerivatives.isEmpty()) {
            return cachedDerivatives;
        }
        Expr newExpr = new Expr(Operate.addAtomicsString(getAtomicElements()));
        cachedDerivatives = newExpr.derive();
        return cachedDerivatives;
    }
}
