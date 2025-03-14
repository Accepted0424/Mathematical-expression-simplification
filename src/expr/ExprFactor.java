package expr;

import tools.Operate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExprFactor extends Factor {
    //带括号的均为表达式因子
    //example: (x+2) (x+1)^2 (x*2+x^2)

    private static final String patternTerm = "[+-]?\\^?\\+?(\\d+)?"; //捕获括号内内容和指数
    private static final Pattern re = Pattern.compile(patternTerm);

    public ExprFactor(String factor) {
        super(factor);
    }

    @Override
    public ArrayList<AtomicElement> getAtomicElements() {

        String s = getFactor();
        int start = 0;
        int inBracket = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            // 获取最外层括号内的内容
            if (c == '(') {
                if (inBracket == 0) {
                    start = i;
                }
                inBracket++;
            } else if (c == ')') {
                inBracket--;
            }
            // 判断是否符合表达式因子的格式
            if (inBracket == 0 && i > start + 1) {
                String remaining = s.substring(0, start) + s.substring(i + 1);
                String innerExpr = s.substring(start + 1, i);

                Matcher m = re.matcher(remaining);
                if (m.matches()) {
                    Expr expr = new Expr(innerExpr);
                    ArrayList<AtomicElement> atoms = new ArrayList<>(expr.getAtomicElements());
                    ArrayList<AtomicElement> newAtoms = new ArrayList<>(atoms);

                    if (m.group(1) != null) {
                        // 含有指数
                        int exponent = Integer.parseInt(m.group(1));
                        if (exponent != 0) {
                            for (int j = 0; j < exponent - 1; j++) {
                                atoms = Operate.mul(atoms, newAtoms);
                            }
                        } else {
                            // 指数为0直接返回1
                            atoms.clear();
                            atoms.add(new AtomicElement(BigInteger.ONE, 0,0,null));
                        }
                    }
                    return atoms;
                }
            }
        }
        System.err.println("Error in ExprFactor: Invalid factor format in " + s);
        return null;
    }

    @Override
    public ArrayList<AtomicElement> derive() {
        // (x^2+1)^2 2*(x^2+1)*2*x
        ArrayList<AtomicElement> derivatives = new ArrayList<>();
        Pattern p = Pattern.compile("[+-]?\\((.*)\\)\\^?\\+?(\\d+)?");
        Matcher m = p.matcher(getFactor());
        if (m.matches()) {
            if (m.group(2) == null) {
                Expr innerExpr = new Expr(m.group(1));
                derivatives.addAll(innerExpr.derive());
                return derivatives;
            }
            int exponent = Integer.parseInt(m.group(2));
            if (exponent == 0) {
                derivatives.add(new AtomicElement(BigInteger.ZERO, 0,0,null));
                return derivatives;
            } else if (exponent == 1) {
                Expr innerExpr = new Expr(m.group(1));
                derivatives.addAll(innerExpr.derive());
                return derivatives;
            } else {
                derivatives.add(new AtomicElement(BigInteger.valueOf(exponent), 0,0,null));
                Expr innerExpr = new Expr(m.group(1));
                derivatives = Operate.mul(derivatives, innerExpr.derive());
                Expr descend = new Expr(expoFixedFactor(exponent - 1));
                derivatives = Operate.mul(derivatives, descend.getAtomicElements());
                return derivatives;
            }
        } else {
            System.err.println("Error in ExprFactor: Invalid factor format in " + getFactor());
            return null;
        }
    }

    private String expoFixedFactor(int expo) {
        Pattern p = Pattern.compile("[+-]?(\\(.*\\)\\^?)\\+?(\\d+)?");
        Matcher m = p.matcher(getFactor());
        if (m.matches()) {
            return m.group(1) + expo;
        } else {
            System.err.println("Error in ExprFactor: Invalid factor format in " + getFactor());
            return null;
        }
    }

    @Override
    public String toString() {
        return getFactor();
    }
}
