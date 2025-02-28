package expr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PowerFactor extends Factor {
    //带x的均为变量因子
    //example: x x^2
    private static final String patternTerm = "([+-]{0,2})x\\^?\\+?(\\d+)?";
    public static final Pattern re = Pattern.compile(patternTerm);

    public PowerFactor(String factor) {
        super(factor);
    }

    @Override
    public ArrayList<Mono> getMonos() {
        ArrayList<Mono> monos = new ArrayList<>();
        Matcher mc = re.matcher(getFactor());
        if (mc.matches()) {
            int pow = 1;
            if (mc.group(2) != null) {
                pow = Integer.parseInt(mc.group(2));
            }

            BigInteger coe = BigInteger.ONE;
            if (mc.group(1).equals("-") || mc.group(1).equals("+-")) {
                coe = BigInteger.valueOf(-1);
            }

            monos.add(new Mono(coe, pow));
            return monos;
        } else {
            System.err.println("Invalid PowerFactor: " + getFactor());
            return null;
        }
    }
}
