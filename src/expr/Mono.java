package expr;

import java.math.BigInteger;

public class Mono extends AtomicElement {
    private BigInteger coe;
    private final int varsPow;

    public Mono(BigInteger coe, int varsPow) {
        this.coe = coe;
        this.varsPow = varsPow;
    }

    public BigInteger getCoe() {
        return coe;
    }

    public int getVarsPow() {
        return varsPow;
    }

    @Override
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
