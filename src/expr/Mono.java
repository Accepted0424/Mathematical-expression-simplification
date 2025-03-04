package expr;

import java.math.BigInteger;

public class Mono extends AtomicElement {

    public Mono(BigInteger coe, int varsPow) {
        super(coe, varsPow);
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
