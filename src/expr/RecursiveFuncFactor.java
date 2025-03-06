package expr;

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
    private static final Pattern re = Pattern.compile("f\\{(\\d+\\)}\\(([xy],?[xy]?)\\)=(.*)");
    private static final Map<String, String> funcRule = new HashMap<>();
    private static String formalParam = "";

    public RecursiveFuncFactor(String func) {
        super(func);
    }

    public static void addRule (String func) {

        Matcher m = re.matcher(func);
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
        Matcher m = re.matcher(getFactor());
        if (m.matches()) {
            String arguments = m.group(1);
            String actualParam = m.group(2);
            int key = Integer.parseInt(arguments);
            if (funcRule.containsKey(key)) {

            }
        } else {
            System.err.println("Invalid recursive function: " + getFactor());
        }
        return null;
    }
}
