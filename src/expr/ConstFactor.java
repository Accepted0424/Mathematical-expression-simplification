package expr;

import tools.Operate;

import java.math.BigInteger;
import java.util.ArrayList;

public class ConstFactor extends Factor {
    //example: 1 12 123

    public ConstFactor(String factor) {
        super(factor);
    }

    @Override
    public ArrayList<AtomicElement> getAtomicElement() {
        ArrayList<AtomicElement> monos = new ArrayList<>();
        BigInteger coe = myParseBigInt(getFactor());
        monos.add(new Mono(coe, 0));
        return monos;
    }

    private BigInteger myParseBigInt(String input) {
        String fmt = Operate.mergeSymbol(input);
        return new BigInteger(fmt);
    }

}
