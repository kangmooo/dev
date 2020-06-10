package main;


import org.assertj.core.util.TriFunction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.lang.Boolean.FALSE;
import static main.Postfix.toPostfix;
import static main.ReservedWords.*;

/**
 * validation check 를 위한 function 들
 *
 * @author SungTae, Kang
 */
public class ValidCheckFuncs {

    // string buffer에 값이 있으면 토큰으로 추가하고, string buffer를 비운다.
    public static BiFunction<String, List<Object>, String> emptyStringBuffer = (stringBuffer, tokenList) -> {
        if (stringBuffer.length() > 0) {
            tokenList.add(stringBuffer);
        }
        return "";
    };

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
                continue;
            }
            if (isOperation.test(c) || isSquareBracket.test(c) || isBracket.test(c) || isComma.test(c)) {
                sb = emptyStringBuffer.apply(sb, tokenList);
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

    public static boolean isFunction(List<Object> list, int start, List<String> msg) {
        boolean result = FALSE;
        if (start > 0) {
            Object obj = list.get(start - 1);
            if (obj instanceof String
                    && !isOperation.test(obj)
                    && !isSquareBracket.test(String.valueOf(obj))
                    && !isBracket.test(obj)
                    && !isComma.test(String.valueOf(obj))) {
                if (!functions.containsKey(obj)) {                                            // 2) ( 앞에 function 인지 확인 한다. 있으면 앞 index 까지 포함 하려고..
                    msg.add("\"" + obj + "\"" + "is not function ");
                }
                result = obj instanceof String;
            }
        }
        return result;
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



    // 줄바꿈 제거 (trim), 대문자
    public static Function<String, String> trimAndUpperCase = calcExp -> calcExp.replaceAll(" |\t|\n|\r|System.getProperty(\"line.separator\")", "").toUpperCase();

    private static Function<String, String> makeMsg = str -> str + ". \r\n";
    public static Function<String, String> getArgsNumberErrMsg = func -> makeMsg.apply(func + " arguments must be " + functions.get(func));
    public static Function<String, String> getArgsErrMsg = func -> makeMsg.apply(func + " arguments type mismatch");
    public static Function<String, String> getOperationErrMsg = func -> makeMsg.apply("Arithmetic operation problem " + func);

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
    public static Function<String, List<String>> checkBracket = calcExp -> {
        int length = calcExp.length();
        List<String> msg = new ArrayList<>();
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


    public static List<Object> executeCalc(List<Object> list, int start, int end, Map<String, Object> pamTagMap, List<String> msg) {
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
                    // TODO 사칙연산 arggument 받아오기기
//                   Object last = getAliasData(pamTagMap, stack.pop(), errorMsg);
//                    Object first = getAliasData(pamTagMap, stack.pop(), errorMsg);
                    Object last = 1.0;
                    Object first = 1.0;
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
//    private static Object getAliasData(Map<String, PamTagMapping> pamTagMap, Object obj, List<String> msg) {
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
//    }

    // function(Object... list) 함수에서 사용 할 argument 들의 alias  들을 값으로 변환 하는 매소드 (즉 alias 가 존재 하는 지 여부)
//    private static void setAliasData(List<Object> stack, Map<String, PamTagMapping> pamTagMap, List<String> msg) {
//        IntStream.range(0, stack.size()).forEach(i -> {
//            Object obj = stack.get(i);
//            stack.set(i, getAliasData(pamTagMap, obj, msg));
//        });
//    }

    /**
     * calculation function 을 호출하여 연산을 수행
     *
     * @param func  31개의 function 중의 수행해야 될 function
     * @param stack 수행될 data가 포함된 stack
     * @return calculation function 결과값
     */
    private static String callFunction(String func, Stack<Object> stack) {
        try {

            switch (func) {
                case "ABS":
                case "SQRT":
                case "SATURATION_PRESSURE":
                case "SATURATION_TEMPERATURE":
                case "SPECIFIC_VOLUME_LIQUID":
                    return checkFuncAgrs.apply(func, stack, 1);

                case "ISENTROPIC_EFFICIENCY_GT_COMPRESSOR":
                case "COMPRESSOR_TEMPERATURE_RATIO":
                case "ISENTROPIC_EFFICIENCY_GT_TURBINE":
                case "AH_LEAKAGE":
                case "OR_EXIST":
                case "AND_EXIST":
                case "PREVIOUS":
                    return checkFuncAgrs.apply(func, stack, 2);

                case "ENTHALPY":
                case "AH_GSE_NO_LEAKAGE":
                case "AH_THERMAL_POWER":
                case "STEAM_TURBINE_FLOW":
                case "PUMP_TDH":
                case "SELECT":
                    return checkFuncAgrs.apply(func, stack, 3);

                case "AH_X_RATIO_NO_LEAKAGE":
                case "HEAT_EX_THERMAL_POWER":
                case "ISENTROPIC_EFFICIENCY_ST_HP":
                case "ISENTROPIC_EFFICIENCY_ST_LP":
                case "ISENTROPIC_EFFICIENCY_ST_PUMP":
                case "IGV_ANTI_ICING_SP":
                    return checkFuncAgrs.apply(func, stack, 4);

                case "AH_GSE_LEAKAGE":
                    return checkFuncAgrs.apply(func, stack, 5);

                case "AH_X_RATIO_LEAKAGE":
                    return checkFuncAgrs.apply(func, stack, 6);

                case "AVERAGE":
                case "MEDIAN":
                case "SPREAD":
                case "MAXIMUM":
                case "SUM":
                case "MINIMUM":
                    return thresholdCheck(func, stack);

                case "PUMP_HYDRAULIC_POWER":
                    return pump_hydraulic_power(stack);

                case "HEAT_EX_LMTD":
                    return heat_ex_lmtd(stack);

                case "COMPRESSOR_PRESSURE_RATIO":
                    return compressor_pressure_ratio(stack);

                case "THERMAL_EFFICIENCY_GT":
                    return thermal_efficiency_gt.apply(stack);

                case "GT_MWI":
                    return gt_mwi.apply(stack);

                default:
                    return "";
            }
        } catch (Exception e) {
            System.out.println("    error function = " + func + " |  error message = " + e.getMessage());
            return "    error function = " + func + " |  error message = " + e.getMessage() + "\n";
        }
    }

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
    private static String thresholdCheck(String func, Stack<Object> stack) {
        String msg = "";

        Stack<Object> tmpList = new Stack<>();
        while (!"]".equals(stack.peek())) {
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
        if ("]".equals(stack.peek())) { // 계산 배열
            stack.pop(); // remove ']'
            while (stack.peek() instanceof Double || !("[".equals(stack.peek()))) {
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

    /* 12 */
    private static String pump_hydraulic_power(Stack<Object> stack) {
        Object sg = stack.pop(), t = stack.pop(), pSuction = stack.pop(), pDischarge = stack.pop(), vPump = stack.pop(), mPump = stack.pop();
        return (!isNumber.test(pDischarge) || !isNumber.test(pSuction) || !isNumber.test(t) || !isNumber.test(sg) || (!isNumber.test(mPump) && !isNumber.test(vPump)))
                ? getArgsErrMsg.apply("pump_hydraulic_power")
                : "";
    }

    /* 20 */
    private static String heat_ex_lmtd(Stack<Object> stack) {
        Object direction = stack.pop();
        Object tCl = stack.pop(), tCe = stack.pop(), tHl = stack.pop(), tHe = stack.pop();
        boolean result = (!isNumber.test(tHe) || !isNumber.test(tHl) || !isNumber.test(tCe) || !isNumber.test(tCl) || !isBoolean.test(String.valueOf(direction)));
        return result
                ? getArgsErrMsg.apply("heat_ex_lmtd")
                : "";
    }

    /* 27 */
    private static String compressor_pressure_ratio(Stack<Object> stack) {
        Object pDiff = stack.pop(), pA = stack.pop(), pS = stack.pop(), pD = stack.pop();
        return (!isNumber.test(pD))
                ? getArgsErrMsg.apply("compressor_pressure_ratio")
                : "";
    }

    /* 30 */
    private static Function<Stack<Object>, String> thermal_efficiency_gt = stack -> {
        Object hvDefault = stack.pop(), hvMeasured = stack.pop(), mFuel = stack.pop(), vFuel = stack.pop(), power = stack.pop();
        Object hv = (null != hvMeasured) ? hvMeasured : hvDefault;
        return (!isNumber.test(power) || (!isNumber.test(vFuel) && !isNumber.test(mFuel)) || (!isNumber.test(hvMeasured) && !isNumber.test(hvDefault)))
                ? getArgsErrMsg.apply("thermal_efficiency_gt")
                : "";
    };

    /* 31 */
    private static Function<Stack<Object>, String> gt_mwi = stack -> {
        Object hvDefault = stack.pop(), hvMeasured = stack.pop(), t = stack.pop();
        return (!isNumber.test(t) || (!isNumber.test(hvMeasured) && !isNumber.test(hvDefault)))
                ? getArgsErrMsg.apply("gt_mwi")
                : "";
    };
}
