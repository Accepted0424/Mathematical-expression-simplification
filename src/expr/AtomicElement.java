package expr;

import tools.Operate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

public class AtomicElement {
    // coe * x^varsPow * sin(expr) * ... * cos(expr) * ...
    private BigInteger coe;
    private final int varsPow;
    // 已化简的三角函数因子
    private final ArrayList<SinCosFactor> triFactors = new ArrayList<>();

    public AtomicElement(BigInteger coe, int varsPow, ArrayList<SinCosFactor> triFactors) {
        this.coe = coe;
        this.varsPow = varsPow;
        if (triFactors != null) {
            this.triFactors.addAll(triFactors);
        }
    }

    public void inverseCoe(){
        this.coe = coe.negate();
    }

    public BigInteger getCoe() {
        return coe;
    }

    public int getVarsPow() {
        return varsPow;
    }

    public ArrayList<SinCosFactor> getTriFactors() {
        return triFactors;
    }

    // 便于作为map的key
    public String getTriFactorsStr() {
        StringBuilder sb = new StringBuilder();
        for (SinCosFactor sinCosFactor : triFactors) {
            sb.append(sinCosFactor);
            if (triFactors.indexOf(sinCosFactor) != triFactors.size() - 1) {
                sb.append("*");
            }
        }
        return sb.toString();
    }

    @Override
    public boolean equals (Object x) {
        if (x == null) {
            return false;
        }
        if (this == x) {
            return true;
        }
        if (this.getClass() != x.getClass()) {
            return false;
        }
        AtomicElement that = (AtomicElement) x;
        if (!this.getCoe().equals(that.getCoe())) {
            return false;
        }
        if (this.getVarsPow() != that.getVarsPow()) {
            return false;
        }
        if (this.triFactors.size() != that.triFactors.size()) {
            return false;
        }
        return getTriFactorsStr().equals(that.getTriFactorsStr());
    }

    @Override
    public String toString() {
        String powString = "";
        String coeString = "";
        StringBuilder triString = new StringBuilder();

        if (getCoe().equals(BigInteger.ZERO)) {
            return "";
        } else if (getCoe().equals(BigInteger.valueOf(-1)) || getCoe().equals(BigInteger.valueOf(1))) {
            coeString = "";
            if (getCoe().equals(BigInteger.valueOf(-1))) {
                coeString = "-";
            }
            if (getVarsPow() == 0) {
                coeString = String.valueOf(getCoe());
            }
        } else {
            coeString = getCoe() + "*";
            if (getVarsPow() == 0) {
                coeString = getCoe() + "";
            }
        }

        if (getVarsPow() == 0) {
            if(!getTriFactors().isEmpty() && !getCoe().equals(BigInteger.ONE)) {
                powString = "*";
            } else {
                powString = "";
            }
        } else if (getVarsPow() == 1) {
            powString = "x";
            if (!getTriFactors().isEmpty()) {
                powString = "x*";
            }
        } else {
            powString = "x^" + getVarsPow();
        }

        if (!triFactors.isEmpty()) {
            for (int i = 0; i < triFactors.size(); i++) {
                triString.append(triFactors.get(i).toString());
                if (i != triFactors.size() - 1) {
                    triString.append("*");
                }
            }
        }
        return coeString + powString + triString.toString();
    }

}
