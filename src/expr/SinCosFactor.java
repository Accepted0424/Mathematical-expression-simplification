package expr;

import tools.Operate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SinCosFactor extends Factor {
    // example: sin(x) cos(x) sin(x^2)^2 cos(x+1)^2 sin((x+1)*2)^2
    private static final Pattern sinRe = Pattern.compile("[+-]?sin\\^?[+-]?(\\d+)?");
    private static final Pattern cosRe = Pattern.compile("[+-]?cos\\^?[+-]?(\\d+)?");
    private ArrayList<AtomicElement> innerMonos = new ArrayList<>();
    private int pow = 1;

    public SinCosFactor(String factor) {
        super(factor);
        extractInnerMono();
    }

    public int getPow() {
        return pow;
    }

    public ArrayList<AtomicElement> getInnerMono() {
        return innerMonos;
    }

    private void extractInnerMono() {
        Map.Entry<String, String> extracted = Operate.getStrInOutermostBracket(getFactor());
        Expr expr = null;
        if (extracted != null) {
            expr = new Expr(extracted.getKey());
            innerMonos = expr.getAtomicElements();
            innerMonos.sort(Comparator.comparing(AtomicElement::getCoe));
            innerMonos.sort(Comparator.comparing(AtomicElement::getXPow));
            innerMonos.sort(Comparator.comparing(AtomicElement::getYPow));
        } else {
            System.err.println("Invalid SinCosFactor: " + getFactor());
        }
    }

    private boolean hasSameAtomElement(SinCosFactor that) {
        for (AtomicElement thisAtom : this.getAtomicElements()) {
            boolean has = false;
            for (AtomicElement thatAtom : that.getAtomicElements()) {
                if (thisAtom.equals(thatAtom)) {
                    has = true;
                    break;
                }
            }
            if (!has) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object x) {
        if (x == null) {
            return false;
        }
        if (this == x) {
            return true;
        }
        if (this.getClass() != x.getClass()) {
            return false;
        }
        SinCosFactor that = (SinCosFactor) x;
        if (this.getTriType() != that.getTriType()) {
            return false;
        }
        if (this.getPow() != that.getPow()) {
            return false;
        }
        return this.hasSameAtomElement(that);
    }

    @Override
    public ArrayList<AtomicElement> getAtomicElements() {
        ArrayList<AtomicElement> atoms = new ArrayList<>();
        Map.Entry<String, String> extracted = Operate.getStrInOutermostBracket(getFactor());
        if (extracted != null) {
            String outerStr = extracted.getValue();
            Matcher sinMc = sinRe.matcher(outerStr);
            Matcher cosMc = cosRe.matcher(outerStr);
            if (sinMc.matches() || cosMc.matches()) {
                int exponent = 1;
                if (sinMc.matches() && sinMc.group(1) != null) {
                    exponent = Integer.parseInt(sinMc.group(1));
                    pow = exponent;
                } else if (cosMc.matches() && cosMc.group(1) != null) {
                    exponent = Integer.parseInt(cosMc.group(1));
                    pow = exponent;
                }
                ArrayList<SinCosFactor> triFactors = new ArrayList<>();
                triFactors.add(this);
                for (int i = 0; i < exponent - 1; i++) {
                    triFactors.add(this);
                }
                atoms.add(new AtomicElement(BigInteger.ONE, 0,0, triFactors));
                return atoms;
            } else {
                System.err.println("Invalid SinCosFactor: " + getFactor());
                return null;
            }
        } else {
            System.err.println("Invalid SinCosFactor: " + getFactor());
            return null;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (getTriType() == 1) {
            sb.append("sin");
        } else if (getTriType() == 2) {
            sb.append("cos");
        }
        sb.append("(");
        for (AtomicElement atom : innerMonos) {
            sb.append(atom);
            if (innerMonos.indexOf(atom) != innerMonos.size() - 1) {
                sb.append("+");
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
