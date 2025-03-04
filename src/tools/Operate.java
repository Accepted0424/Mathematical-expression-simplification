package tools;

import expr.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public class Operate {
    public static ArrayList<AtomicElement> mul(ArrayList<AtomicElement> left, ArrayList<AtomicElement> right) {
        //先合并同类相
        ArrayList<AtomicElement> mergedLeft = merge(left);
        ArrayList<AtomicElement> mergedRight = merge(right);

        ArrayList<AtomicElement> monos = new ArrayList<>();
        for (AtomicElement l: mergedLeft) {
            for (AtomicElement r: mergedRight) {
                monos.add(simpleMul(l,r));
            }
        }
        return monos;
    }

    public static ArrayList<AtomicElement> add(ArrayList<AtomicElement> left, ArrayList<AtomicElement> right) {
        ArrayList<AtomicElement> addedMonos = new ArrayList<>();
        addedMonos.addAll(left);
        addedMonos.addAll(right);
        return merge(addedMonos);
    }

    private static AtomicElement simpleMul(AtomicElement left, AtomicElement right) {
        BigInteger leftCoe = left.getCoe();
        BigInteger rightCoe = right.getCoe();
        int leftPow = left.getVarsPow();
        int rightPow = right.getVarsPow();
        return new Mono(leftCoe.multiply(rightCoe), leftPow + rightPow);
    }

    private static Mono simpleAdd(Mono left, Mono right) {
        if (left.getVarsPow() == right.getVarsPow()) {
            return new Mono(left.getCoe().add(right.getCoe()), left.getVarsPow());
        } else {
            return null;
        }
    }

    //合并同类项
    public static ArrayList<AtomicElement> merge(ArrayList<AtomicElement> monos) {
        ArrayList<AtomicElement> mergedMonos = new ArrayList<>();
        Map<Integer, List<AtomicElement>> map = monos.stream()
            .collect(Collectors.groupingBy(AtomicElement::getVarsPow));
        map.forEach((power, monoList) -> {
            // 对每一组的 Mono 按幂次进行合并
            BigInteger totalCoefficient = monoList.stream()
                .map(AtomicElement::getCoe)
                .reduce(BigInteger.ZERO, BigInteger::add);
            mergedMonos.add(new Mono(totalCoefficient, power)); // 添加合并后的结果
        });
        return mergedMonos;
    }

    public static String mergeSymbol(String s) {
        String merged = "";
        // merge两边确保不会连续出现三个+或-
        merged = s.replaceAll("\\+\\+", "+");
        merged = merged.replaceAll("--", "+");
        merged = merged.replaceAll("\\+-", "-");
        merged = merged.replaceAll("-\\+", "-");
        merged = merged.replaceAll("\\+\\+", "+");
        merged = merged.replaceAll("--", "+");
        merged = merged.replaceAll("\\+-", "-");
        merged = merged.replaceAll("-\\+", "-");

        return merged;
    }

    /**
     * <h2>构建三角函数的单项式</h2>
     * 传入字符串类型的三角函数因子，得到三角函数类型的原子表达式
     */
    public static AtomicSinCos buildTrigonometryAtom(String trigonometryFactor) {
        Factor innerFactor = FactorFactory.getFactor(getStrInOutermostBracket(trigonometryFactor));

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
        return new AtomicSinCos(BigInteger.ONE, expoent);
    }

    public static String getStrInOutermostBracket(String s) {
        int inBracket = 0;
        int start = 0;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == '(') {
                inBracket++;
                if (inBracket == 1) {
                    start = i;
                }
            } else if (ch == ')') {
                inBracket--;
                if (inBracket == 0) {
                    return s.substring(start + 1, i);
                }
            }
        }
        return null;
    }

}
