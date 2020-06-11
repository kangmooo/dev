package main.calcExpValidation;

import org.assertj.core.util.TriFunction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiFunction;

import static main.calcExpValidation.Formulas.callFunction;
import static main.calcExpValidation.Messages.*;
import static main.calcExpValidation.Postfix.getTopStack;
import static main.calcExpValidation.Postfix.toPostfix;
import static main.calcExpValidation.ReservedWords.*;

/**
 * validation check 를 위한 function 들
 *
 * @author SungTae, Kang
 */
public class ValidCheckFuncs {

    // arguments 체크
    public static TriFunction<String, Stack<Object>, Integer, String> checkFuncAgrs = (func, stack, checkCnt) -> {
        String msg = "";
        if (!(stack.size() == checkCnt)) msg += getArgsNumberErrMsg.apply(func);
        for (int i = 0; i < checkCnt; i++) {
            Object obj = stack.pop();
            if (!isNumber.test(obj)) {
                msg += makeMsg.apply(func + "  -  '" + obj + "' arguments type mismatch");
            }
        }
        return msg;
    };

    // 괄호 pair check
    public static BiFunction<String, List<String>, List<String>> checkBracket = (calcExp, msg) -> {
        int length = calcExp.length();
        int squaredPair = 0, roundPair = 0;
        for (int i = 0; i < length; i++) {
            String token = String.valueOf(calcExp.charAt(i));
            if (isSquareBracket.test(token)) squaredPair += ("[".equals(token)) ? 1 : -1;
            if (isBracket.test(token)) roundPair += ("(".equals(token)) ? 1 : -1;
        }
        if (squaredPair != 0)
            msg.add("There is(are) " + squaredPair + " square brackets('" + ((squaredPair > 0) ? "[" : "]") + "') not closed");
        if (roundPair != 0)
            msg.add("There is(are) " + roundPair + " square brackets('" + ((roundPair > 0) ? "(" : ")") + "') not closed");
        return msg;
    };

    public static List<Object> executeCalc(List<Object> list, int start, int end, Map<String, Object> aliasDataMap, List<String> msg) {
        List<Object> subList = list.subList(start, end);                            // 3) 필요 부분 잘라서
        List<Object> postfixToken = toPostfix.apply(subList);                       // 4) 자른 String 후위연산 list 로 변환

        Stack<Object> stack = new Stack<>();
        List<String> errorMsg = new ArrayList<>();
        if (postfixToken.size() == 0) {
//            return null;
        }
        for (Object token : postfixToken) {
            if (isOperation.test(token)) {                                       // 연산자 [+, -, *, /, ^(제곱근)]
                try {
                    Object last = getAliasData(aliasDataMap, stack.pop(), errorMsg);
                    Object first = getAliasData(aliasDataMap, stack.pop(), errorMsg);
                    stack.push(executeOperation.apply(first, last, token));
                } catch (Exception e) {
                    errorMsg.add(getOperationErrMsg.apply("[" + token + "]"));
                    stack.push(1.0);
                }
                continue;
            } else if (isInActive.test(token)) {                                 // inactive
                stack.push(null);
                continue;
            } else if (Arrays.asList("PREVIOUS").contains(token)) {
                // TODO list data 만들기
//                getAliasDataList(pamTagMap, stack, errorMsg, (String) token);
                continue;
            } else if (functions.containsKey(String.valueOf(token))) {// function
                // TODO alias 값
//                setAliasData(stack, pamTagMap, errorMsg);
                String result = callFunction((String) token, stack);
                if (!"".equals(result)) {
                    errorMsg.add(result);
                }
                stack.push(1.0);
                continue;
            } else {
                stack.push(token);
                continue;
            }
        }
        if (errorMsg.size() > 0) {
            msg.addAll(errorMsg);
        }
        List<Object> result = new ArrayList<>();
        result.addAll(list.subList(0, start));
        result.addAll(stack);
        result.addAll(list.subList(end, list.size()));

        return result;
    }

    private static TriFunction<Object, Object, Object, Double> executeOperation = (a, b, op) -> {
        if (a == null || b == null) {
            return null;
        }
        Double first = Double.parseDouble(String.valueOf(a));
        Double last = Double.parseDouble(String.valueOf(b));
        if ("+".equals(op)) {
            return (first + last);
        } else if ("-".equals(op)) {
            return (first) - (last);
        } else if ("*".equals(op)) {
            return first * last;
        } else if ("/".equals(op)) {
            if (!(last == 0)) {
                return first / last;
            }
            return Math.pow(first, last);
        } else if ("^".equals(op)) {
            return Math.pow(first, last);
        }
        return null;
    };

    // TODO alias 데이터 변환 부분
//    private static void getAliasDataList(Map<String, PamTagMapping> pamTagMap, List<Object> stack, List<String> msg, String token) {
//        try {
//            int size = Integer.parseInt(String.valueOf(stack.pop()));
//            String alias = (String) stack.pop();
//            if (!pamTagMap.containsKey(alias)) {
//                msg.add(makeMsg.apply(token + " does not exist"));
//            } else if (!(size > 0)) {
//                msg.add(makeMsg.apply(token + " previous sizasdfe was not set"));
//            }
//            if (size == 1) {
//                pushStack.accept(stack, 1.0);
//            } else {
//                pushStack.accept(stack, "[");
//                pushStack.accept(stack, 1.0);
//                pushStack.accept(stack, "]");
//            }
//        } catch (Exception e) {
//            log.warn(" === " + e.getMessage());
//        }
//    }

    // alias, threshold, 의 경우 값을 찾아서 반환 하고 그외의 값들은 다시 리턴
    private static Object getAliasData(Map<String, Object> aliasDataMap, Object obj, List<String> msg) {
        if (isNumber.test(obj)) {   // 숫자 이면 바로 리턴
            return Double.parseDouble(String.valueOf(obj));
        } else {
            return aliasDataMap.get(obj);
        }

//        if (isNumber.test(obj)) {   // 숫자 이면 바로 리턴
//            return Double.parseDouble(String.valueOf(obj));
//        } else {
//            String str = String.valueOf(obj);
//            if (pamTagMap.containsKey(str)) {       // Alias 이면
//                return 1.0;
//            } else if (str.contains(".")) {         // threshold 이면
//                String[] strings = str.split("\\.");
//                String tagAlias = strings[0], thresholdType = strings[1];
//                if (!pamTagMap.containsKey(tagAlias)) {   // tagAlias 가 존재 하지 않으면
//                    msg.add("\"" + tagAlias + "\" " + ALIAS_DOES_NOT_EXIST.getMsg());
//                    return 1.0;
//                } else {
//                    return Optional.ofNullable(getComparisonValue.apply(thresholdType, pamTagMap.get(tagAlias), msg))
//                            .orElseGet(() -> {
//                                // ptm의 threshold 가 null 이면
//                                msg.add(" \"" + obj + "\"  ptm_seq = " + pamTagMap.get(tagAlias).getPam_tag_mapping_seq() + " " + ThresholdColumn.get(thresholdType) + IS_NOT_SET_MSG.getMsg() + " ");
//                                return 1.0;
////                                return obj;
//                            });
//                }
//            } else {  // Alias or threshold 가 아닐때
//
//                if (functions.containsKey(str) || isBoolean.test(str) || isInActive.test(str) || isSquareBracket.test(str) || "null".equals(str)) {
//                    return obj;
//                } else {
//                    // 함수, boolean, inactive, [], null 아니면
//                    msg.add("\"" + str + "\" " + ALIAS_DOES_NOT_EXIST.getMsg());
//                    return 1.0;
//                }
//            }
//        }
    }

    // function(Object... list) 함수에서 사용 할 argument 들의 alias  들을 값으로 변환 하는 매소드 (즉 alias 가 존재 하는 지 여부)
//    private static void setAliasData(List<Object> stack, Map<String, PamTagMapping> pamTagMap, List<String> msg) {
//        IntStream.range(0, stack.size()).forEach(i -> {
//            Object obj = stack.get(i);
//            stack.set(i, getAliasData(pamTagMap, obj, msg));
//        });
//    }

    /**
     * "AVERAGE", "MEDIAN", "SPREAD", "MAXIMUM", "SUM" 의 function 들은
     * function([],min,max,boolean) 의 함수로 min max 값을 넘지 않는 data 들만 계산 하도록 하기 위해 임계치를 벗어 나지 않는 값만 return 한다.
     * 임계 값 범위 외부에 존재 할 때
     * ignorable == true or  ignorable == null 이면 값을 제외 하고 진행
     * ignorable == false 이면  NaN 으로 리턴
     *
     * @param stack 토큰 리스트
     * @return 임계치를 벗어 나지 않는 data 만  return
     */
    public static String thresholdCheck(String func, Stack<Object> stack) {
        String msg = "";

        Stack<Object> tmpList = new Stack<>();
        while (!"]".equals(getTopStack.apply(stack))) {
            tmpList.add(stack.pop());
        }
        int argsCnt = tmpList.size();
        if (argsCnt > 0) {
            if (argsCnt == 3 || argsCnt == 2) {
                if (tmpList.peek() instanceof Double) { // 최대 최소 저장
                    Object thLower = tmpList.pop();
                    Object thUpper = tmpList.pop();
                    if (isNumber.test(thUpper) && isNumber.test(thLower)) {
                        if ((Double) thUpper <= (Double) thLower) {
                            msg += makeMsg.apply(func + " -  The Upper must be greater than Lower");
                        }
                    } else {
                        msg += makeMsg.apply(func + " -  thUpper, thLower arguments must be number");
                    }
                }
                if (argsCnt == 3) {
                    Object obj = tmpList.pop();
                    if (!"TRUE".equals(obj) && !"FALSE".equals(obj)) {
                        msg += makeMsg.apply(func + " -  3rd argument type must be Boolean type.");
                    }
                }
            } else {
                msg += makeMsg.apply(func + " -  arguments error");
            }
        }
        if ("]".equals(getTopStack.apply(stack))) { // 계산 배열
            stack.pop(); // remove ']'
            while (getTopStack.apply(stack) instanceof Double || !("[".equals(getTopStack.apply(stack)))) {
                Double value = (Double) stack.pop();
                if (!isNumber.test(value)) {
                    // TODO 오류
                    //  return '배열 내부에 오류가 있음
                    msg += makeMsg.apply(func + " -  array  arguments error");
                }
            }
            if ("[".equals(stack.peek())) {
                stack.pop(); // remove '['
            }
        } else {
            // TODO 오류
            //  return '배열 "]" braket 필요합니다. '
        }
        return msg;
    }

    private void writeFile(String writePath, Long pamMappingSeq, String str) {              // String write and make file
        File dir = new File(writePath);
        if (!dir.isDirectory()) {
            dir.mkdirs();
        }
        Path path = Paths.get(writePath + "pam_mapping_" + pamMappingSeq + ".log");   // Get the file reference

        try (BufferedWriter writer = Files.newBufferedWriter(path)) {                       // Use try-with-resource to get auto-closeable writer instance
            writer.write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
