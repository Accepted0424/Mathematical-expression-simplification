package expr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PowerFactor extends Factor implements AtomicArrayConvertible{
    //带x的均为变量因子
    //example: x x^2
    private static final String patternTerm = "([+-]{0,2})x\\^?\\+?(\\d+)?";
    public static final Pattern re = Pattern.compile(patternTerm);

    public PowerFactor(String factor) {
        super(factor);
    }

    @Override
    public ArrayList<AtomicElement> getAtomicElements() {
        ArrayList<AtomicElement> atoms = new ArrayList<>();
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

            atoms.add(new AtomicElement(coe, pow, null));
            return atoms;
        } else {
            System.err.println("Invalid PowerFactor: " + getFactor());
            return null;
        }
    }
}
