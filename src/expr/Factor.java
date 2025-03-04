package expr;

public abstract class Factor implements AtomicArrayConvertible {
    private final String factor;

    public Factor(String factor) {
        this.factor = factor;
    }

    public String getFactor() {
        return factor;
    }
}
