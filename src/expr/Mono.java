package expr;

import java.math.BigInteger;

public class Mono {
    private final String trigonometry;
    private BigInteger coe;
    private final int varsPow;

    public Mono(String trigonometry, BigInteger coe, int varsPow) {
        this.trigonometry = trigonometry;
        this.coe = coe;
        this.varsPow = varsPow;
    }

    public String getTrigonometry() {
        return trigonometry;
    }

    public boolean isSin() {
        return trigonometry.contains("sin");
    }

    public boolean isCos() {
        return trigonometry.contains("cos");
    }

    public BigInteger getCoe() {
        return coe;
    }

    public int getVarsPow() {
        return varsPow;
    }

    public void inverseCoe() {
        this.coe = coe.negate();
    }

    @Override
    public String toString() {
        String powString = "";
        String coeString = "";
        if (varsPow == 0) {
            powString = "";
        } else if (varsPow == 1) {
            powString = "x";
        } else {
            powString = "x^" + varsPow;
        }

        if (coe.equals(BigInteger.ZERO)) {
            coeString = "";
            powString = "";
        } else if (coe.equals(BigInteger.valueOf(-1)) || coe.equals(BigInteger.valueOf(1))) {
            coeString = "";
            if (coe.equals(BigInteger.valueOf(-1))) {
                coeString = "-";
            }
            if (varsPow == 0) {
                coeString = String.valueOf(coe);
            }
        } else {
            coeString = coe + "*";
            if (varsPow == 0) {
                coeString = coe + "";
            }
        }
        return coeString + powString;
    }

}
