package expr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SinCosFactor extends Factor {
    // example: sin(x) cos(x) sin(x^2)^2 cos(x+1)^2 sin((x+1)*2)^2
    private final static String pattern = "(sin)\\^?(\\d+)?|(cos)\\^?(\\d+)?";
    private final static Pattern re = Pattern.compile(pattern);
    private String innerFactor;

    public SinCosFactor(String factor) {
        super(factor);
    }

    @Override
    public ArrayList<AtomicElement> getAtomicElement() {
        ArrayList<AtomicElement> monos = new ArrayList<>();
        // 获取最外层括号内的内容
        String s = getFactor();
        int start = 0;
        int inBracket = 0;
        for (int i = 0; i <= s.length() - 1; i++) {
            char c = s.charAt(i);

            if (c == '(' && inBracket == 0) {
                inBracket++;
                start = i;
            } else if (c == ')') {
                inBracket--;
            }

            if (inBracket == 0) {
                String remaining = s.substring(0, start) + s.substring(i + 1);
                Matcher m = re.matcher(remaining);
                if (m.matches()) {
                    Factor innerFactor = FactorFactory.getFactor(s);

                    Mono mono = new Mono(BigInteger.ONE, 0);
                    monos.add(mono);
                    return monos;
                }
            }
        }
        System.err.println("Invalid TrigonometryFactor: " + getFactor());
        return null;
    }
}
