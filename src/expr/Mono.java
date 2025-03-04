package expr;

import java.math.BigInteger;

public class Mono {
    private String prefix;
    private BigInteger coe;
    private final int varsPow;

    public Mono(String prefix, BigInteger coe, int varsPow) {
        this.prefix = prefix;
        this.coe = coe;
        this.varsPow = varsPow;
    }

    public String getPrefix() {
        return prefix;
    }

    public boolean isSin() {
        return prefix.equals("sin");
    }

    public boolean isCos() {
        return prefix.equals("cos");
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
