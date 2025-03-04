package expr;

import java.math.BigInteger;

public class AtomicSinCos extends AtomicElement {
    private String triExpr;

    public boolean isSin() {
        return triExpr.contains("sin");
    }

    public boolean isCos() {
        return triExpr.contains("cos");
    }

    public AtomicSinCos(BigInteger coe, int varsPow) {
        super(coe, varsPow);
    }

    @Override
    public String toString() {
        return "";
    }

}
