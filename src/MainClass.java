import expr.Expr;
import expr.RecursiveFuncFactor;
import tools.Operate;

import java.util.Scanner;
import java.util.regex.Pattern;

public class MainClass {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String n = sc.nextLine();
        for (int i = 0; i < Integer.parseInt(n)*3; i++) {
            String rule = sc.nextLine().replaceAll("[ \\t]", "");
            RecursiveFuncFactor.addRule(rule);
        }

        String input = sc.nextLine();
        String s = Pattern.compile("[ \\t]").matcher(input).replaceAll("");
        s = Operate.mergeSymbol(s);

        Expr expr = new Expr(s);
        System.out.println(expr);
    }
}

// test case
// 3*sin(x+1) + 2 * sin(1+x)                    Accepted
// 3*sin(1+(x+1)^2) + 2*sin((1+x)^2+1)          Accepted
// 3*sin(1+(x+1)^2)*sin(x+1) + 2*sin((1+x)^2+1) Accepted
// sin(1)*x*sin(2) + sin(2-1)*sin(1+1)*x        Accepted
// sin(1)*x*sin(2) + sin(2-1)*sin(1+0)*x        Accepted

/*
1
f{0}(x)=x
f{1}(x)=x^2
f{n}(x)=2*f{n-1}(x)-1*f{n-2}(x)
f{2}(x)+1
Accepted
 */

/*
1
f{0}(x)=x^2
f{1}(x)=x+1
f{n}(x)=f{n-1}(x^2)+f{n-2}(x-1)
f{2}(x*2)
result:8*x^2-4*x+2
Accepted
 */

/*
1
f{0}(x,y)=y
f{1}(x,y)=x
f{n}(x,y)=3*f{n-1}(x,y)+2*f{n-2}(x,y)
f{2}(x,x^2)
 */

/*
1
f{0}(x,y)=x+y
f{1}(x,y)=x-y
f{n}(x,y)=f{n-1}(x^2,y)+f{n-2}(y-x^2,x)
f{2}(2*x,3*x)
(2*x)^2-3*y+3*y-(2*x)^2+2*x
 */

/*
1
f{0}(x)=1
f{1}(x)=x
f{n}(x)=2*f{n-1}(x^2)+3*f{n-2}(x^2)+-1 = 4+4*sin(x)^4 + 3*sin(x)^2 -1 =5*sin(x)^2+1-cos(x)^2
f{3}(sin(x))-cos(x)^2 = 4*sin(x)^4 + 3*sin(x)^2 - cos(x)^2 + 3
f{2}(sin(x)^2) = 2 + 2*sin(x)^4
 */