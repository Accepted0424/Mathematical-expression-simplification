package expr;

import java.util.regex.Pattern;

public class FactorFactory {
    public static Factor getFactor(String s) {
        Pattern constRe = Pattern.compile("\\+?-?\\d+");
        Pattern exprRe = Pattern.compile("-?\\(([^)]+)\\)\\^?(\\d+)?");
        Pattern powerRe = Pattern.compile("(\\+?\\-?)x\\^?(\\d+)?");
        if (constRe.matcher(s).matches()) {
            return new ConstFactor(s);
        } else if (exprRe.matcher(s).matches()) {
            return new ExprFactor(s);
        } else if (powerRe.matcher(s).matches()) {
            return new PowerFactor(s);
        } else {
            System.err.println("Invalid factor in FactorFactory: " + s);
            return null;
        }
    }
}
