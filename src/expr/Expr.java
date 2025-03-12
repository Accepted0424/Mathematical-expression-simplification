package expr;

import tools.Operate;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Expr implements AtomicArrayConvertible {
    private String expr;
    private ArrayList<Term> terms = new ArrayList<>();

    public Expr(String expr) {
        this.expr = expr;
        terms = extractTerms();
    }

    public ArrayList<Term> extractTerms() {
        //以不被括号包裹的+为分界线提取term

        //便于以加号分割term
        expr = Pattern.compile("-").matcher(expr).replaceAll("+-");

        int inBracket = 0; //是否在括号外
        int start = 0;
        for (int i = 0; i < expr.length(); i++) {
            char ch = expr.charAt(i);

            if (ch == '(') {
                inBracket++;
            } else if (ch == ')') {
                inBracket--;
            }

            if (inBracket == 0) {
                //subString函数是[x,y)的
                if (ch == '+') {
                    String subString = expr.substring(start, i);
                    //针对+-开头的情况
                    if (subString.isEmpty()) {
                        continue;
                    }
                    //针对+5*+-07的情况
                    if (i > 1 && (expr.charAt(i - 1) == '*' || expr.charAt(i - 1) == '^')) {
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
    public ArrayList<AtomicElement> getAtomicElements() {
        ArrayList<AtomicElement> atoms = new ArrayList<>(terms.get(0).getAtomicElements());
        for (int i = 1; i < terms.size(); i++) {
            atoms = Operate.add(atoms, terms.get(i).getAtomicElements());
        }
        return atoms;
    }

    @Override
    public String toString() {
        ArrayList<AtomicElement> atoms = getAtomicElements();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < atoms.size() - 1; i++) {
            String atomString = atoms.get(i).toString();
            if (!atomString.isEmpty()) {
                sb.append(atomString);
                if (i != atoms.size() - 1) {
                    sb.append("+");
                }
            }
        }

        //最后一个mono为空不需要添加加号
        String lastAtomString = atoms.get(atoms.size() - 1).toString();
        if (lastAtomString.isEmpty()) {
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
        }
        sb.append(atoms.get(atoms.size() - 1).toString());

        if (sb.toString().isEmpty()) {
            return "0";
        }
        //化简+-和-+为-
        return Operate.mergeSymbol(sb.toString());
    }

}
