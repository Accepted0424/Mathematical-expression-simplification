package expr;

import java.math.BigInteger;

public abstract class AtomicElement {
    public abstract String toString();
    public abstract void inverseCoe();
    public abstract int getVarsPow();
    public abstract BigInteger getCoe();
}
