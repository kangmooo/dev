package main.calcExpValidation;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Function;

import static main.calcExpValidation.ReservedWords.*;

public class Postfix {

    // 스택의 최상위 값을 조회함.
    static Function<List<Object>, Object> getTopStack = stack -> stack.size() == 0 ? null : stack.get(stack.size() - 1);

    public static Function<String, String> trimAndUpperCase = calcExp ->
            calcExp
                    .replaceAll(" |\t|\n|\r|System.getProperty(\"line.separator\")", "") // 공백, 탭, 줄바꿈 제거 (trim)
                    .toUpperCase(); // 대문자로 변환

    /**
     * 계산식 String을 토큰화하여 리스트로 반환함.
     *
     * @param calcExp 계산식
     * @return 계산식 String을 토큰화하 된 리스트
     */
    public static Function<String, List<Object>> makeTextToToken = calcExp -> {
        String sb = "";
        int length = calcExp.length();
        List<Object> tokenList = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            String c = String.valueOf(calcExp.charAt(i));
            if (isMinus.test(calcExp, i)) { //음수 의 '-' 이면
                sb = sb.concat(c);
            } else if (isOperation.test(c) || isSquareBracket.test(c) || isBracket.test(c) || isComma.test(c)) {
                if (sb.length() > 0) {
                    tokenList.add(sb);
                }
                sb = "";
                tokenList.add(c);
            } else {
                sb = sb.concat(c);
            }
        }
        if (!isOperation.test(sb) && !isSquareBracket.test(sb) && !isBracket.test(sb) && !isComma.test(sb) && !"".equals(sb)) {
            tokenList.add(sb);
        }
        return tokenList;
    };

    // 연산자 우선순위 반환
    private static Function<Object, Integer> precedence = op -> switch ((String) op) {
        case "(" -> 0;
        case "+", "-" -> 1;
        case "*", "/" -> 2;
        case "^" -> 3;
        default -> 4;
    };

    /**
     * 중위연산식의 토큰리스트를 후위연산식으로 변환
     *
     * @param tokenList String 계산식-> 토큰화된 리스트
     * @return 후위연산식으로 변환된 리스트
     */
    public static Function<List<Object>, List<Object>> toPostfix = tokenList -> {
        List<Object> postfixToken = new ArrayList<>();
        Stack<Object> stack = new Stack<>();
        for (Object token : tokenList) {
            if (!isComma.test(String.valueOf(token))) {
                if ("(".equals(token)) {                // "("
                    stack.push(token);
                } else if (")".equals(token)) {        // ")"
                    while (!"(".equals(getTopStack.apply(stack))) {
                        postfixToken.add(stack.pop());
                    }
                    stack.pop(); // remove '('

                } else if (isOperation.test(token) || functions.containsKey(String.valueOf(token))) {   // operation
                    while (getTopStack.apply(stack) != null && precedence.apply(getTopStack.apply(stack)) >= precedence.apply(token)) {    // stack에 들어있는 것이 우선순위가 높으면 pop
                        postfixToken.add(stack.pop());
                    }
                    stack.push(token);

                } else {    // Other (Tag Alias, '[', ']' etc...)
                    postfixToken.add(token);
                }
            }
        }
        while (getTopStack.apply(stack) != null) {
            if (!isComma.test(String.valueOf(getTopStack.apply(stack)))) {
                postfixToken.add(stack.pop());
            }
        }
        return postfixToken;
    };
}
