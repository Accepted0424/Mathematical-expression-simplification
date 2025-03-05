package expr;

import tools.Operate;

import java.math.BigInteger;
import java.util.ArrayList;
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
        this.triFactors.addAll(triFactors);
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
        return Operate.hashSameElement(this.triFactors, that.triFactors);
    }

    @Override
    public String toString() {
        String powString = "";
        String coeString = "";
        if (getVarsPow() == 0) {
            powString = "";
        } else if (getVarsPow() == 1) {
            powString = "x";
        } else {
            powString = "x^" + getVarsPow();
        }

        if (getCoe().equals(BigInteger.ZERO)) {
            coeString = "";
            powString = "";
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
        return coeString + powString;
    }

}
