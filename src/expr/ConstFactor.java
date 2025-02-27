package expr;

import java.util.ArrayList;

public class ConstFactor extends Factor {
    //example: 1 12 123

    public ConstFactor(String factor) {
        super(factor);
    }

    @Override
    public ArrayList<Mono> getMonos() {
        ArrayList<Mono> monos = new ArrayList<>();
        int coe = myParseInt(getFactor());
        monos.add(new Mono(coe, 0));
        return monos;
    }

    private int myParseInt(String input) {
        // Integer.parserInt无法解析+-, 可以处理前导零
        String fmt = input.replaceAll("\\+-", "-");
        fmt = fmt.replaceAll("-\\+", "*");

        return Integer.parseInt(fmt);
    }

}
