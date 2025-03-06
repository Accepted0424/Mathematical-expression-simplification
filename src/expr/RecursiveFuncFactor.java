package expr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// f{0}(x) = x^2                        f{0}(x,y) = x+y
// f{1}(x) = sin(x)                     f{1}(x,y) = sin(x+y)
// f{n}(x) = cos(x)*f{n-1} + x*f{n-2}   f{n}(x,y) = x*f{n-1} + y*f{n-2}

public class RecursiveFuncFactor extends Factor implements AtomicArrayConvertible{
    // 一个表达式只包含0或1个递归函数，使用static变量存储递归函数的规则
    private static final Pattern ruleRe = Pattern.compile("f\\{(\\d+|n)}\\(([xy],?[xy]?)\\)=(.*)");
    private static final Pattern factorRe = Pattern.compile("f\\{(\\d+|n)}\\(([xy],?[xy]?)\\)");
    private static final Map<String, String> funcRule = new HashMap<>();
    private static String formalParam = "";

    public RecursiveFuncFactor(String func) {
        super(func);
    }

    public static void addRule (String func) {

        Matcher m = ruleRe.matcher(func);
        if (m.matches()) {
            String arguments = m.group(1);
            formalParam = m.group(3).replaceAll(",", "");
            String expression = m.group(3);
            funcRule.put(arguments, expression);
        } else {
            System.err.println("Invalid recursive rule: " + func);
        }
    }

    public String getFormalParam() {
        return formalParam;
    }

    @Override
    public ArrayList<AtomicElement> getAtomicElements() {
        ArrayList<AtomicElement> atoms = new ArrayList<>();
        Matcher m = factorRe.matcher(getFactor());
        if (m.matches()) {
            String args = m.group(1);
            int argsInt = Integer.parseInt(args);
            String actualParam = m.group(2);
            if (funcRule.containsKey(args)) {
                Expr expr = new Expr(funcRule.get(args));
                atoms.addAll(expr.getAtomicElements());
                return atoms;
            } else {
                // 解析递推规则
                // f{3} = f{2} + f{1} = f{1} + f{0} + f{1}
                // f{4} = f{3} + f{2} = f{2} + f{1} + f{1} + f{0} = f{1} + f{0} + f{1} + f{1} + f{0}
                String recSub1 = String.format("f{%d}", argsInt - 1);
                String recSub2 = String.format("f{%d}", argsInt - 2);
                String s = funcRule.get("n").replaceAll("f\\{n-1}", recSub1);
                s = s.replaceAll("f\\{n-2}", recSub2);
                Expr expr = new Expr(s);
                atoms.addAll(expr.getAtomicElements());
                return atoms;
            }
        } else {
            System.err.println("Invalid recursive function: " + getFactor());
        }
        return null;
    }
}
