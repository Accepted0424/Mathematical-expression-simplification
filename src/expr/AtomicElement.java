package expr;

import java.math.BigInteger;

public abstract class AtomicElement {
    private BigInteger coe;
    private final int varsPow;

    protected AtomicElement(BigInteger coe, int varsPow) {
        this.coe = coe;
        this.varsPow = varsPow;
    }

    public abstract String toString();

    public void inverseCoe(){
        this.coe = coe.negate();
    }

    public BigInteger getCoe() {
        return coe;
    }

    public int getVarsPow() {
        return varsPow;
    }
}
