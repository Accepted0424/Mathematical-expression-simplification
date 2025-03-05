package expr;

import tools.Operate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SinCosFactor extends Factor {
    // example: sin(x) cos(x) sin(x^2)^2 cos(x+1)^2 sin((x+1)*2)^2
    private final static String pattern = "(sin)\\^?(\\d+)?|(cos)\\^?(\\d+)?";
    private final static Pattern re = Pattern.compile(pattern);

    public SinCosFactor(String factor) {
        super(factor);
    }

    /**
     * 0 for nothing<br>
     * 1 for sin<br>
     * 2 for cos
     * @return type of trigonometry function
     */
    public int getTriType() {
        if (getFactor().contains("sin")) {
            return 1;
        } else if (getFactor().contains("cos")) {
            return 2;
        } else {
            return 0;
        }
    }

    @Override
    public boolean equals(Object x) {
        if (x == null) {
            return false;
        }
        if (this == x) {
            return true;
        }
        if (this.getClass() != x.getClass()) {
            return false;
        }
        SinCosFactor that = (SinCosFactor) x;
        if (this.getTriType() != that.getTriType()) {
            return false;
        }
        return Operate.hashSameElement(this.getAtomicElement(), that.getAtomicElement());
    }


    @Override
    public ArrayList<AtomicElement> getAtomicElement() {
        ArrayList<AtomicElement> atoms = new ArrayList<>();
        // 获取最外层括号内的内容
        String s = getFactor();
        int start = 0;
        int inBracket = 0;
        for (int i = 0; i <= s.length() - 1; i++) {
            char c = s.charAt(i);

            if (c == '(') {
                if (inBracket == 0) {
                    start = i;
                }
                inBracket++;
            } else if (c == ')') {
                inBracket--;
            }

            if (inBracket == 0) {
                String remaining = s.substring(0, start) + s.substring(i + 1);
                Matcher m = re.matcher(remaining);
                if (m.matches()) {
                    AtomicElement atom = Operate.buildTrigonometryAtom(s);
                    atoms.add(atom);
                    return atoms;
                }
            }
        }
        System.err.println("Invalid SinCosFactor: " + getFactor());
        return null;
    }

    /**
     * <h2>构建三角函数的单项式</h2>
     * 传入字符串类型的三角函数因子，得到三角函数类型的原子表达式
     */
    private static AtomicElement buildTrigonometryAtom(String trigonometryFactor) {
        Factor innerFactor = FactorFactory.getFactor(Operate.getStrInOutermostBracket(trigonometryFactor));

        // 解析指数
        String pattern = "(sin)\\(" + innerFactor.getFactor() + "\\)\\^?(\\d+)?|(cos)\\(" + innerFactor.getFactor() + "\\)\\^?(\\d+)?";
        Pattern re = Pattern.compile(pattern);
        int expoent = 1;
        Matcher m = re.matcher(trigonometryFactor);
        if (m.matches()) {
            if (m.group(1) != null) {
                expoent = Integer.parseInt(m.group(1));
            }
        } else {
            expoent = 0;
        }

        ArrayList<AtomicElement> innerMonos = innerFactor.getAtomicElement();
        int length = innerMonos.size();
        StringBuilder sb = new StringBuilder();
        for (AtomicElement mono : innerMonos) {
            sb.append(mono);
            sb.append("+");
        }
        //最后一个mono为空不需要添加括号
        if (innerMonos.get(length - 1).toString().isEmpty()) {
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
        }
        sb.append(innerMonos.get(length - 1));

        if (sb.toString().isEmpty()) {
            sb.append("0");
        }
        //化简+-和-+为-
        sb.append(Operate.mergeSymbol(sb.toString()));
        return new AtomicElement(BigInteger.ONE, expoent, null);
    }
}
