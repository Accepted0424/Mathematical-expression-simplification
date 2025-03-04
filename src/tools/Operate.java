package tools;

import expr.Factor;
import expr.FactorFactory;
import expr.Mono;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

public class Operate {
    public static ArrayList<Mono> mul(ArrayList<Mono> left, ArrayList<Mono> right) {
        //先合并同类相
        ArrayList<Mono> mergedLeft = merge(left);
        ArrayList<Mono> mergedRight = merge(right);

        ArrayList<Mono> monos = new ArrayList<>();
        for (Mono l: mergedLeft) {
            for (Mono r: mergedRight) {
                monos.add(simpleMul(l,r));
            }
        }
        return monos;
    }

    public static ArrayList<Mono> add(ArrayList<Mono> left, ArrayList<Mono> right) {
        ArrayList<Mono> addedMonos = new ArrayList<>();
        addedMonos.addAll(left);
        addedMonos.addAll(right);
        return merge(addedMonos);
    }

    private static Mono simpleMul(Mono left, Mono right) {
        BigInteger leftCoe = left.getCoe();
        BigInteger rightCoe = right.getCoe();
        int leftPow = left.getVarsPow();
        int rightPow = right.getVarsPow();
        return new Mono("", leftCoe.multiply(rightCoe), leftPow + rightPow);
    }

    private static Mono simpleAdd(Mono left, Mono right) {
        if (left.getVarsPow() == right.getVarsPow()) {
            return new Mono("", left.getCoe().add(right.getCoe()), left.getVarsPow());
        } else {
            return null;
        }
    }

    //合并同类项
    public static ArrayList<Mono> merge(ArrayList<Mono> monos) {
        ArrayList<Mono> mergedMonos = new ArrayList<>();
        Map<Integer, List<Mono>> map = monos.stream()
            .collect(Collectors.groupingBy(Mono::getVarsPow));
        map.forEach((power, monoList) -> {
            // 对每一组的 Mono 按幂次进行合并
            BigInteger totalCoefficient = monoList.stream()
                .map(Mono::getCoe)
                .reduce(BigInteger.ZERO, BigInteger::add);
            mergedMonos.add(new Mono("", totalCoefficient, power)); // 添加合并后的结果
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

    public static Mono buildTrigonometryMono(String trigonometry) {
        Factor innerFactor = FactorFactory.getFactor(getStrInOutermostBracket(trigonometry));
        //解析指数
        String pattern = "(sin)\\(" + innerFactor.getFactor() + "\\)\\^?(\\d+)?|(cos)\\(" + innerFactor.getFactor() + "\\)\\^?(\\d+)?";
        Pattern re = Pattern.compile(pattern);
        int expoent = 1;
        Matcher m = re.matcher(trigonometry);
        if (m.matches()) {
            if (m.group(1) != null) {
                expoent = Integer.parseInt(m.group(1));
            }
        } else {
            expoent = 0;
        }

        ArrayList<Mono> innerMonos = innerFactor.getMonos();
        StringBuilder sb = new StringBuilder();
        for (Mono mono : innerMonos) {
            sb.append(mono);
            sb.append("+");
        }
        //最后一个mono为空不需要添加括号
        if (innerMonos.getLast().toString().isEmpty()) {
            if (!sb.isEmpty()) {
                sb.deleteCharAt(sb.length() - 1);
            }
        }
        sb.append(innerMonos.getLast());

        if (sb.isEmpty()) {
            sb.append("0");
        }
        //化简+-和-+为-
        sb.append(Operate.mergeSymbol(sb.toString()));
        return new Mono(sb.toString(), BigInteger.ONE, expoent);
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
