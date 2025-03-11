package expr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// f{0}(x) = x^2                        f{0}(x,y) = x+y
// f{1}(x) = sin(x)                     f{1}(x,y) = sin(x+y)
// f{n}(x) = cos(x)*f{n-1} + x*f{n-2}   f{n}(x,y) = x*f{n-1} + y*f{n-2}

public class RecursiveFuncFactor extends Factor implements AtomicArrayConvertible {
    // 一个表达式只包含0或1个递归函数，使用static变量存储递归函数的规则
    private static final Pattern ruleRe = Pattern.compile("f\\{(\\d+|n)}\\(([xy],?[xy]?)\\)=(.*)");
    private static final Pattern factorRe = Pattern.compile("[+-]?f\\{(\\d+|n)}\\((.*)\\)");
    private static final Map<String, String> funcRule = new HashMap<>();
    private static ArrayList<String> formalParamList = new ArrayList<>();

    public RecursiveFuncFactor(String func) {
        super(func);
    }

    public static void addRule(String func) {
        Matcher m = ruleRe.matcher(func);
        if (m.matches()) {
            String arguments = m.group(1);
            String formalParam = m.group(2);
            for (String param: formalParam.split(",")) {
                if (!param.isEmpty() && !formalParamList.contains(param)) {
                    formalParamList.add(param);
                }
            }
            String expression = m.group(3);
            funcRule.put(arguments, expression);
        } else {
            System.err.println("Invalid recursive rule: " + func);
        }
    }

    @Override
    public ArrayList<AtomicElement> getAtomicElements() {
        ArrayList<AtomicElement> atoms = new ArrayList<>();
        Matcher m = factorRe.matcher(getFactor());
        if (m.matches()) {
            String args = m.group(1);
            int argsInt = Integer.parseInt(args);
            String actualParam = m.group(2);
            int start = 0;
            int inBracket = 0;
            ArrayList<String> actualParamsList = new ArrayList<>();
            for (int i = 0; i < actualParam.length(); i++) {
                char c = actualParam.charAt(i);
                if (c == '(') {
                    inBracket++;
                } else if (c == ')') {
                    inBracket--;
                }
                if (inBracket == 0 && c == ',') {
                    actualParamsList.add(actualParam.substring(start, i));
                    actualParamsList.add(actualParam.substring(i + 1));
                    break;
                } else if (i == actualParam.length() - 1) {
                    actualParamsList.add(actualParam);
                }
            }
            ArrayList<String> actualParamList = new ArrayList<>();
            for (String param: actualParamsList) {
                Expr expr = new Expr(param);
                actualParamList.add(expr.toString());
            }
            if (funcRule.containsKey(args)) {
                String s = funcRule.get(args);
                int idx = 0;
                if (formalParamList.size() > 1) {
                    String secondParam = formalParamList.get(1);
                    s = s.replaceAll(secondParam, "z");
                    formalParamList.set(1, "z");
                }
                for (String param: formalParamList) { // 代入实参
                    s = s.replaceAll(param, "(" + actualParamList.get(idx) + ")");
                    idx++;
                }
                s = s.replaceAll("z", "x");
                Expr expr = new Expr(s);
                atoms.addAll(expr.getAtomicElements());
                return atoms;
            } else {
                // 解析递推规则
                String recSub1 = String.format("f{%d}", argsInt - 1);
                String recSub2 = String.format("f{%d}", argsInt - 2);
                String s = funcRule.get("n").replaceAll("f\\{n-1}", recSub1); // 替换参数
                s = s.replaceAll("f\\{n-2}", recSub2);
                int idx = 0;
                if (formalParamList.size() > 1) {
                    String secondParam = formalParamList.get(1);
                    s = s.replaceAll(secondParam, "z");
                    formalParamList.set(1, "z");
                }
                for (String param : formalParamList) {
                    s = s.replaceAll(param, "(" + actualParamList.get(idx) + ")"); // 代入实参
                    idx++;
                }
                s = s.replaceAll("z", "x");
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
