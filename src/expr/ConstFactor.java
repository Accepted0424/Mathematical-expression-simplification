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
    public ArrayList<AtomicElement> getAtomicElements() {
        ArrayList<AtomicElement> atoms = new ArrayList<>();
        BigInteger coe = myParseBigInt(getFactor());
        atoms.add(new AtomicElement(coe, 0, 0,null));
        return atoms;
    }

    @Override
    public ArrayList<AtomicElement> derive() {
        ArrayList<AtomicElement> atoms = new ArrayList<>();
        atoms.add(new AtomicElement(BigInteger.ZERO, 0, 0,null));
        return atoms;
    }

    private BigInteger myParseBigInt(String input) {
        String fmt = Operate.mergeSymbol(input);
        return new BigInteger(fmt);
    }

}
