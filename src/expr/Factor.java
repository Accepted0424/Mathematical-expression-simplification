package expr;

public abstract class Factor implements MonoArrayConvertible {
    private final String factor;

    public Factor(String factor) {
        this.factor = factor;
    }

    public String getFactor() {
        return factor;
    }
}
