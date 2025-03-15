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
    private String cachedString = null;
    private ArrayList<AtomicElement> cachedAtoms = new ArrayList<>();
    private ArrayList<AtomicElement> cachedDerivatives = new ArrayList<>();

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
        ArrayList<AtomicElement> thisAes = this.getAtomicElements();
        ArrayList<AtomicElement> thatAes = that.getAtomicElements();
        for (AtomicElement thisAtom : thisAes) {
            boolean has = false;
            for (AtomicElement thatAtom : thatAes) {
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
        if (!cachedAtoms.isEmpty()) {
            return cachedAtoms;
        }
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
                if (exponent == 0) {
                    atoms.clear();
                    atoms.add(new AtomicElement(BigInteger.ONE, 0, 0, null));
                }
                cachedAtoms = atoms;
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
        if (cachedString != null) {
            return  cachedString;
        }
        if (innerMonos.isEmpty()) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            if (getTriType() == 1) {
                sb.append("sin");
            } else if (getTriType() == 2) {
                sb.append("cos");
            }
            sb.append("(");
            sb.append("(");
            for (AtomicElement atom : innerMonos) {
                if (atom.getCoe().equals(BigInteger.ZERO)) {
                    sb.append("0");
                } else {
                    sb.append(atom);
                }
                sb.append("+");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(")");
            sb.append(")");
            cachedString = sb.toString();
            return sb.toString();
        }
    }

    @Override
    public ArrayList<AtomicElement> derive() {
        if (!cachedDerivatives.isEmpty()) {
            return cachedDerivatives;
        }
        Map.Entry<String, String> extracted = Operate.getStrInOutermostBracket(getFactor());
        String outerStr = "";
        if (extracted != null) {
            outerStr = extracted.getValue();
        }
        String innerStr = "";
        if (extracted != null) {
            innerStr = extracted.getKey();
        }
        Matcher sinMatcher = sinRe.matcher(outerStr);
        Matcher cosMatcher = cosRe.matcher(outerStr);
        if (sinMatcher.matches()) {
            cachedDerivatives = getDerivatives(sinMatcher, innerStr, false);
            return cachedDerivatives;
        } else if (cosMatcher.matches()) {
            cachedDerivatives = getDerivatives(cosMatcher, innerStr, true);
            return cachedDerivatives;
        } else {
            System.err.println("Invalid SinCosFactor: " + getFactor());
        }
        return null;
    }

    private ArrayList<AtomicElement> getDerivatives(Matcher matcher, String inner, Boolean isCos) {
        ArrayList<AtomicElement> derivatives = new ArrayList<>();
        if (matcher.group(1) == null) {
            SinCosFactor newSinCosFactor = new SinCosFactor(funcNameFixedFactor());
            derivatives.addAll(newSinCosFactor.getAtomicElements());
            Factor innerFactor = FactorFactory.getFactor(inner);
            derivatives = Operate.mul(derivatives, innerFactor.derive());
            return derivatives;
        }
        int exponent = Integer.parseInt(matcher.group(1));
        if (exponent == 0) {
            derivatives.add(new AtomicElement(BigInteger.ZERO, 0, 0, null));
            return derivatives;
        }
        derivatives.add(new AtomicElement(BigInteger.valueOf(exponent), 0, 0, null));
        if (isCos) {
            ArrayList<AtomicElement> tmp = new ArrayList<>();
            tmp.add(new AtomicElement(BigInteger.valueOf(-1), 0, 0, null));
            derivatives = Operate.mul(derivatives, tmp);
        }
        if (exponent > 1) {
            SinCosFactor newSinCosFactor = new SinCosFactor(expoFixedFactor(exponent - 1));
            derivatives = Operate.mul(derivatives, newSinCosFactor.getAtomicElements());
        }
        SinCosFactor newSinCosFactor = new SinCosFactor(funcNameFixedFactor());
        derivatives = Operate.mul(derivatives,newSinCosFactor.getAtomicElements());
        Factor innerFactor = FactorFactory.getFactor(inner);
        derivatives = Operate.mul(derivatives,innerFactor.derive());
        return derivatives;
    }

    private String expoFixedFactor(int exponent) {
        Pattern pt = Pattern.compile("([+-]?(sin|cos)\\(.*\\)\\^?)[+-]?(\\d+)?");
        Matcher m = pt.matcher(getFactor());
        if (m.matches()) {
            String withoutExponent = m.group(1);
            String exponentStr = String.valueOf(exponent);
            return withoutExponent + exponentStr;
        } else {
            System.err.println("Invalid SinCosFactor: " + getFactor());
            return null;
        }
    }

    private String funcNameFixedFactor() {
        Pattern pt = Pattern.compile("[+-]?(sin|cos)(\\(.*\\))\\^?[+-]?(\\d+)?");
        Matcher m = pt.matcher(getFactor());
        if (m.matches()) {
            String funcName = m.group(1);
            if (funcName.equals("sin")) {
                return "cos" + m.group(2);
            } else if (funcName.equals("cos")) {
                return "sin" + m.group(2);
            }
        } else {
            System.err.println("Invalid SinCosFactor: " + getFactor());
        }
        return null;
    }
}
