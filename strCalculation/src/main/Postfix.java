package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Function;

import static main.ReservedWords.*;

public class Postfix {

    // 연산자 우선순위 반환
    private static Function<Object, Integer> precedence = op -> {
        switch ((String) op) {
            case "(":
                return 0;
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            case "^":
                return 3;
            default:
                return 4;
        }
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
                    while (!"(".equals(stack.peek())) {
                        postfixToken.add(stack.pop());
                    }
                    stack.pop(); // remove '('

                } else if (isOperation.test(token) || functions.containsKey(String.valueOf(token))) {   // operation
                    while (stack.peek() != null && precedence.apply(stack.peek()) >= precedence.apply(token)) {    // stack에 들어있는 것이 우선순위가 높으면 pop
                        postfixToken.add(stack.pop());
                    }
                    stack.push(token);

                } else {    // Other (Tag Alias, '[', ']' etc...)
                    postfixToken.add(token);
                }
            }
        }
        while (stack.peek() != null) {
            if (!isComma.test(String.valueOf(stack.peek()))) {
                postfixToken.add(stack.pop());
            }
        }
        return postfixToken;
    };
}
