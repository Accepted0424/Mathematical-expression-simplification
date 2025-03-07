package expr;

import java.math.BigInteger;
import java.util.ArrayList;

public class AtomicElement {
    // coe * x^xPow * sin(expr) * ... * cos(expr) * ...
    private BigInteger coe;
    private final int xPow;
    private final int yPow;
    // 已化简的三角函数因子
    private final ArrayList<SinCosFactor> triFactors = new ArrayList<>();

    public AtomicElement(BigInteger coe, int xPow, int yPow, ArrayList<SinCosFactor> triFactors) {
        this.coe = coe;
        this.xPow = xPow;
        this.yPow = yPow;
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

    public int getXPow() {
        return xPow;
    }

    public int getYPow() {
        return yPow;
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
        if (this.getXPow() != that.getXPow()) {
            return false;
        }
        if (this.getYPow() != that.getYPow()) {
            return false;
        }
        if (this.triFactors.size() != that.triFactors.size()) {
            return false;
        }
        return getTriFactorsStr().equals(that.getTriFactorsStr());
    }

    @Override
    public String toString() {
        String powXString = "";
        String powYString = "";
        String coeString = "";
        StringBuilder triString = new StringBuilder();

        if (getCoe().equals(BigInteger.ZERO)) {
            return "";
        } else if (getCoe().equals(BigInteger.valueOf(-1)) || getCoe().equals(BigInteger.valueOf(1))) {
            coeString = "";
            // -1*x*y*sin(x) = -x*y*sin(x)
            if (getCoe().equals(BigInteger.valueOf(-1))) {
                coeString = "-";
            }
            // -1*_*_=-1
            if (getXPow() == 0 && getYPow() == 0 && getTriFactors().isEmpty()) {
                coeString = String.valueOf(getCoe());
            }
        } else {
            coeString = getCoe() + "*";
            // 2*_*_*_
            if (getXPow() == 0 && getYPow() == 0 && getTriFactors().isEmpty()) {
                coeString = getCoe() + "";
            }
        }

        if (getXPow() == 0) {
            // 后面为空
            if (getYPow() == 0 && getTriFactors().isEmpty()) {
                powXString = "";
                // 前面为空
                if (coeString.isEmpty()) {
                    powXString = "1";
                }
            } else {
                powXString = "";
            }
        } else if (getXPow() == 1) {
            powXString = "x*";
            if (getTriFactors().isEmpty() && getYPow() == 0) {
                powXString = "x";
            }
        } else {
            powXString = "x^" + getXPow();
        }

        if (getYPow() == 0) {
            // 后面为空
            if(getTriFactors().isEmpty()) {
                powYString = "";
                if (coeString.isEmpty() && powXString.isEmpty()) {
                    powYString = "1";
                }
            } else {
                powYString = "";
            }
        } else if (getYPow() == 1) {
            powYString = "y*";
            if (getTriFactors().isEmpty()) {
                powYString = "y";
            }
        } else {
            powYString = "y^" + getYPow();
        }

        if (!triFactors.isEmpty()) {
            for (int i = 0; i < triFactors.size(); i++) {
                triString.append(triFactors.get(i).toString());
                if (i != triFactors.size() - 1) {
                    triString.append("*");
                }
            }
        }
        return coeString + powXString + powYString + triString.toString();
    }

}
