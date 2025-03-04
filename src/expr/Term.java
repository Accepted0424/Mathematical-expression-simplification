package expr;

import tools.Operate;
import java.util.ArrayList;

public class Term implements MonoArrayConvertible {
    // example: (x+1)*(2*x+6) (x+1)^2*2 (x+1)^3

    private ArrayList<Factor> factors = new ArrayList<>();
    private final String term;
    private boolean isNegative;

    public Term(String term) {
        //化简重复+-再赋值
        this.term = Operate.mergeSymbol(term);
        this.isNegative = false;
        this.factors = extractFactors();
    }

    public String getTerm() {
        return term;
    }

    private ArrayList<Factor> extractFactors() {
        //以不被括号包裹的*为分界线提取factor
        int inBracket = 0; //是否在括号外
        int start = 0;

        //term可能为负
        if (term.charAt(0) == '-') {
            isNegative = true;
            start = 1; //跳过负号
        }

        for (int i = 0; i < term.length(); i++) {
            char ch = term.charAt(i);

            if (ch == '(') {
                inBracket++;
            } else if (ch == ')') {
                inBracket--;
            }

            if (inBracket == 0) {
                //subString函数是[x,y)的
                if (ch == '*') {
                    String subString = term.substring(start, i);
                    start = i + 1;
                    factors.add(FactorFactory.getFactor(subString));
                }
                if (i == term.length() - 1) {
                    String subString = term.substring(start, i + 1);
                    factors.add(FactorFactory.getFactor(subString));
                }
            }
        }
        return factors;
    }

    @Override
    public ArrayList<Mono> getMonos() {
        //factor的单项式相乘
        ArrayList<Mono> monos = factors.get(0).getMonos();
        for (int i = 1; i < factors.size(); i++) {
            monos = Operate.mul(monos, factors.get(i).getMonos());
        }

        if (isNegative) {
            for (Mono mono : monos) {
                mono.inverseCoe();
            }
        }
        return monos;
    }
}
