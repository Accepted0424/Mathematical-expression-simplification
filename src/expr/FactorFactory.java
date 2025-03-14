package expr;

import java.util.regex.Pattern;

public class FactorFactory {
    public static Factor getFactor(String s) {
        Pattern constRe = Pattern.compile("[+-]{0,2}\\d+");
        Pattern powerRe = Pattern.compile("([+-]{0,2})[xy]\\^?\\+?(\\d+)?");
        Pattern exprNoBracketRe = Pattern.compile("[+-]?\\^?\\+?(\\d+)?");
        Pattern sinRe = Pattern.compile("[+-]?sin\\(.*\\)\\^?[+-]?(\\d+)?");
        Pattern cosRe = Pattern.compile("[+-]?cos\\(.*\\)\\^?[+-]?(\\d+)?");
        Pattern recursiveFuncRe = Pattern.compile("[+-]?f\\{\\d+}\\(.*\\)");
        Pattern normalFuncRe = Pattern.compile("[+-]?([gh])\\((.*)\\)");
        Pattern derivativeRe = Pattern.compile("dx\\((.*)\\)");

        if (constRe.matcher(s).matches()) {
            return new ConstFactor(s);
        } else if (powerRe.matcher(s).matches()) {
            return new PowerFactor(s);
        } else if (sinRe.matcher(s).matches() || cosRe.matcher(s).matches()) {
            return new SinCosFactor(s);
        } else if (recursiveFuncRe.matcher(s).matches()) {
            return new RecursiveFuncFactor(s);
        } else if (normalFuncRe.matcher(s).matches()) {
            return new NormalFuncFactor(s);
        } else if (derivativeRe.matcher(s).matches()) {
            return new DerivativeFactor(s);
        } else {
            // 匹配 ExprFactor
            // 加入嵌套括号之后匹配表达式因子需要特殊处理，不能直接使用正则表达式
            int start = 0;
            int inBracket = 0;
            for (int i = 0; i <= s.length(); i++) {
                char c = s.charAt(i);

                if (c == '(') {
                    if (inBracket == 0) {
                        start = i;
                    }
                    inBracket++;
                } else if (c == ')') {
                    inBracket--;
                }
                // 删去最外层括号内的所有内容之后，判断是否符合表达式因子的格式
                if (inBracket == 0) {
                    String remaining = s.substring(0,start) + s.substring(i + 1);
                    if (exprNoBracketRe.matcher(remaining).matches()) {
                        return new ExprFactor(s);
                    }
                }
            }
            System.err.println("Error in FactorFactory: Invalid factor format in " + s);
            return new ExprFactor(s);
        }
    }
}
