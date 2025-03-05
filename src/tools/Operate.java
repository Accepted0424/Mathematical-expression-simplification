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
        return new AtomicElement(leftCoe.multiply(rightCoe), leftPow + rightPow, null);
    }

    private static AtomicElement simpleAdd(AtomicElement left, AtomicElement right) {
        if (left.getVarsPow() == right.getVarsPow()) {
            return new AtomicElement(left.getCoe().add(right.getCoe()), left.getVarsPow());
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
            mergedMonos.add(new AtomicElement(totalCoefficient, power)); // 添加合并后的结果
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

    public static boolean hashSameElement(ArrayList<Object> left, ArrayList<Object> right) {
        if (left.size() != right.size()) {
            return false;
        }
        if (left.getClass() != right.getClass()) {
            return false;
        }

        return true;
    }
}
