package expr;

import tools.Operate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExprFactor extends Factor {
    //带括号的均为表达式因子
    //example: (x+2) (x+1)^2 (x*2+x^2)

    private static final String patternTerm = "\\(([^)]+)\\)\\^?\\+?(\\d+)?"; //捕获括号内内容和指数
    private static final Pattern re = Pattern.compile(patternTerm);

    public ExprFactor(String factor) {
        super(factor);
    }

    @Override
    public ArrayList<Mono> getMonos() {
        ArrayList<Mono> monos = new ArrayList<>();
        Matcher m = re.matcher(getFactor());

        if (m.find()) {
            String innerContext = m.group(1);
            //处理括号内表达式
            Expr innerExpr = new Expr(innerContext);
            ArrayList<Mono> innerMonos = innerExpr.getMonos();
            monos.addAll(innerMonos);

            if (m.group(2) != null) {
                //含有指数
                int exponent = Integer.parseInt(m.group(2));
                if (exponent != 0) {
                    for (int i = 0; i < exponent - 1; i++) {
                        monos = Operate.mul(monos, innerMonos);
                    }
                } else {
                    //指数为0直接返回1
                    monos.clear();
                    monos.add(new Mono(BigInteger.ONE, 0));
                }
            }
            return Operate.merge(monos);
        } else {
            System.err.println("No match found in ExprFactor");
            return null;
        }
    }

    @Override
    public String toString() {
        return getFactor();
    }
}
