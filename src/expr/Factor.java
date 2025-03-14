package expr;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Factor implements AtomicArrayConvertible, Derivable {
    private String factor;
    private int triType = 0;
    private Pattern sinRe = Pattern.compile("[+-]?sin\\(.*\\)\\^?[+-]?(\\d+)?");
    private Pattern cosRe = Pattern.compile("[+-]?cos\\(.*\\)\\^?[+-]?(\\d+)?");

    public Factor(String factor) {
        this.factor = factor;
        Matcher sinMc = sinRe.matcher(factor);
        Matcher cosMc = cosRe.matcher(factor);
        this.triType = cosMc.matches() ? 2 : sinMc.matches() ? 1 : 0;
    }

    public String getFactor() {
        return factor;
    }

    public void setFactor(String factor) {
        this.factor = factor;
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

    public void setTriType(int triType) {
        this.triType = triType;
    }
}
