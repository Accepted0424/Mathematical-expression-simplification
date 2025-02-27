package expr;

import tools.Operate;

import java.util.ArrayList;

public class Expr implements MonoArrayConvertible {
    private final String expr;
    private ArrayList<Term> terms = new ArrayList<>();

    public Expr(String expr) {
        this.expr = expr;
        terms = extractTerms();
    }

    public ArrayList<Term> extractTerms() {
        //以不被括号包裹的+为分界线提取term
        boolean escaped = true; //是否在括号外
        int start = 0;
        for (int i = 0; i < expr.length(); i++) {
            char ch = expr.charAt(i);

            if (ch == '(') {
                escaped = false;
            } else if (ch == ')') {
                escaped = true;
            }

            if (escaped) {
                //subString函数是[x,y)的
                if (ch == '+') {
                    String subString = expr.substring(start, i);
                    //针对+-的情况
                    if (subString.isEmpty()) {
                        continue;
                    }
                    start = i + 1;
                    terms.add(new Term(subString));
                }
                if (i == expr.length() - 1) {
                    String subString = expr.substring(start, i + 1);
                    terms.add(new Term(subString));
                }
            }
        }
        return terms;
    }

    @Override
    public ArrayList<Mono> getMonos() {
        ArrayList<Mono> monos = new ArrayList<>(terms.get(0).getMonos());
        for (int i = 1; i < terms.size(); i++) {
            monos = Operate.add(monos, terms.get(i).getMonos());
        }
        return Operate.merge(monos);
    }

    @Override
    public String toString() {
        ArrayList<Mono> monos = getMonos();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < monos.size() - 1; i++) {
            if (!monos.get(i).toString().isEmpty()) {
                sb.append(monos.get(i));
                sb.append("+");
            }
        }

        //最后一个mono为空不需要添加括号
        if (monos.get(monos.size() - 1).toString().isEmpty()) {
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
        }
        sb.append(monos.get(monos.size() - 1));

        if (sb.length() == 0) {
            return "0";
        }
        //化简+-和-+为-
        return Operate.mergeSymbol(sb.toString());
    }

}
