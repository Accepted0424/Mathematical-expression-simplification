package expr;

import tools.Operate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExprFactor extends Factor implements AtomicArrayConvertible{
    //带括号的均为表达式因子
    //example: (x+2) (x+1)^2 (x*2+x^2)

    private static final String patternTerm = "\\^?\\+?(\\d+)?"; //捕获括号内内容和指数
    private static final Pattern re = Pattern.compile(patternTerm);

    public ExprFactor(String factor) {
        super(factor);
    }

    @Override
    public ArrayList<AtomicElement> getAtomicElements() {

        String s = getFactor();
        int start = 0;
        int inBracket = 0;
        for (int i = 0; i <= s.length(); i++) {
            char c = s.charAt(i);
            // 获取最外层括号内的内容
            if (c == '(') {
                inBracket++;
            } else if (c == ')') {
                inBracket--;
            }
            // 判断是否符合表达式因子的格式
            if (inBracket == 0) {
                String remaining = s.substring(0, start) + s.substring(i + 1);
                String innerExpr = s.substring(start+1, i);
                Matcher m = re.matcher(remaining);
                if (m.matches()) {
                    Expr expr = new Expr(innerExpr);
                    ArrayList<AtomicElement> monos = new ArrayList<>(expr.getAtomicElements());

                    if (m.group(1) != null) {
                        // 含有指数
                        int exponent = Integer.parseInt(m.group(1));
                        if (exponent != 0) {
                            for (int j = 0; j < exponent - 1; j++) {
                                monos = Operate.mul(monos, new Expr(innerExpr).getAtomicElements());
                            }
                        } else {
                            // 指数为0直接返回1
                            monos.clear();
                            monos.add(new AtomicElement(BigInteger.ONE, 0,null));
                        }
                    }
                    return Operate.merge(monos);
                }
            }
        }
        System.err.println("Error in ExprFactor: Invalid factor format in " + s);
        return null;
    }

    @Override
    public String toString() {
        return getFactor();
    }
}
