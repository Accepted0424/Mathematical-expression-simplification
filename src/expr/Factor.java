package expr;

public abstract class Factor implements AtomicArrayConvertible {
    private final String factor;
    private int triType = 0;

    public Factor(String factor) {
        this.factor = factor;
        this.triType = factor.contains("cos")?2:factor.contains("sin")?1:0;
    }

    public String getFactor() {
        return factor;
    }

    /**
     * 0 for nothing<br>
     * 1 for sin<br>
     * 2 for cos
     * @return type of trigonometry function
     */
    public int getTriType() {
        return triType;
    }
}
