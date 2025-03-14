package expr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NormalFuncFactor extends Factor {
    private static final Pattern funcRe = Pattern.compile("([gh])\\(([xy],?[xy]?)\\)=(.*)");
    private static final Pattern factorRe = Pattern.compile("[+-]?([gh])\\((.*)\\)");
    private static final HashMap<String, String> funcMap = new HashMap<>();
    private static final HashMap<String, ArrayList<String>> formalParamMap = new HashMap<>();
    private ArrayList<AtomicElement> cachedAtoms = new ArrayList<>();

    public NormalFuncFactor(String factor) {
        super(factor);
    }

    public static void addFunc(String func) {
        Matcher m = funcRe.matcher(func);
        if (m.matches()) {
            String key = m.group(1);
            String formalParam = m.group(2);
            String expression = m.group(3);
            funcMap.put(key, expression);
            ArrayList<String> fmParamList = new ArrayList<>(Arrays.asList(formalParam.split(",")));
            formalParamMap.put(key, fmParamList);
        } else {
            System.err.println("Invalid customize normal function: " + func);
        }
    }

    @Override
    public ArrayList<AtomicElement> getAtomicElements() {
        if (!cachedAtoms.isEmpty()) {
            return cachedAtoms;
        }
        Matcher m = factorRe.matcher(getFactor());
        if (m.matches()) {
            String funcName = m.group(1);
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

            if (funcMap.containsKey(funcName)) {
                String s = funcMap.get(funcName);
                s = replaceParam(funcName, s, actualParamsList);
                Expr expr = new Expr(s);
                ArrayList<AtomicElement> atoms = new ArrayList<>(expr.getAtomicElements());
                cachedAtoms = atoms;
                return atoms;
            } else {
                System.err.println("Invalid function name: " + funcName);
            }
        } else {
            System.err.println("Invalid normal function: " + getFactor());
        }
        return null;
    }

    private String replaceParam(String funcName, String expr, ArrayList<String> actParamList) {
        int idx = 0;
        String secondParam = "";
        String result = expr;
        if (formalParamMap.get(funcName).size() > 1) {
            secondParam = formalParamMap.get(funcName).get(1);
            result = result.replaceAll(secondParam, "z");
            formalParamMap.get(funcName).set(1, "z");
        }
        for (String param: formalParamMap.get(funcName)) { // 代入实参
            result = result.replaceAll(param, "(" + actParamList.get(idx) + ")");
            idx++;
        }
        result = result.replaceAll("z", secondParam);
        if (formalParamMap.get(funcName).size() > 1) {
            formalParamMap.get(funcName).set(1, secondParam);
        }
        return result;
    }

    @Override
    public String toString() {
        ArrayList<AtomicElement> atoms = getAtomicElements();
        StringBuilder sb = new StringBuilder();
        for (AtomicElement atom: atoms) {
            sb.append(atom.toString()).append("+");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    @Override
    public ArrayList<AtomicElement> derive() {
        Expr expr = new Expr(toString());
        return expr.derive();
    }
}
