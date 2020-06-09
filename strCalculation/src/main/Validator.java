//package main;
//
//import java.util.*;
//import java.util.function.*;
//
//public class Validator {
//    // 줄바꿈 제거 (trim), 대문자
//    public static Function<String, String> trimAndUpperCase = calcExp -> calcExp.replaceAll(" |\t|\n|\r|System.getProperty(\"line.separator\")", "").toUpperCase();
//    // 대괄호 '[', ']'
//    public static Predicate<String> isSquareBracket = str -> "[".equals(str) || "]".equals(str);
//    // 괄호 '(', ')'
//    public static Predicate<Object> isBracket = str -> "(".equals(str) || ")".equals(str);
//
//    // 왼쪽 대괄호 '['
//    public static Predicate<String> isLeftSquareBracket = str -> "[".equals(str);
//    // 오른쪽 대괄호 ']'
//    public static Predicate<String> isRightSquareBracket = str -> "]".equals(str);
//    // 왼쪽괄호 '('
//    public static Predicate<Object> isLeftBracket = str -> "(".equals(str);
//    // 오른쪽괄호 ')'
//    public static Predicate<Object> isRightBracket = str -> ")".equals(str);
//    // 사칙연산 및 제곱근 '+' '-' '*' '/' '^'
//    public static Predicate<Object> isOperation = str -> Arrays.asList("+", "-", "*", "/", "^").contains(str);
//    // 숫자 체크
//    public static Predicate<Object> isNumber = str -> {
//        try {
//            Double.parseDouble(String.valueOf(str));
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    };
//    // 쉼표     ','
//    public static Predicate<String> isComma = str -> ",".equals(str);
//    // 마이너스 체크( true:단항의 음수 | false:이항 연산의 "-" 에 따라 체크)
//    // true : 단항의 음수, false 2항의 연산
//    public static BiPredicate<String, Integer> isMinus = (str, i) -> {
//        if (str.charAt(i) == '-' && i == 0) {                       // 단항의 음수 ( '-' 처음 으로 올때에는 계산식이 아님 )
//            return true;
//        } else if (str.charAt(i) == '-' && str.length()-1==i) {     // '-' 가 마지막 일때 이항 연산 error
//            return false;               // 이항 연산
//        } else if (str.charAt(i) == '-' && isNumber.test(str.substring(i + 1, i + 2))) {
//            return (isOperation.test(String.valueOf(str.charAt(i - 1))) || isComma.test(String.valueOf(str.charAt(i - 1))) || str.charAt(i - 1) == '(' || str.charAt(i - 1) == '[');
//        } else {
//            return false;
//        }
//    };
//    // string buffer에 값이 있으면 토큰으로 추가하고, string buffer를 비운다.
//    private static BiFunction<String, List<Object>, String> emptyStringBuffer = (stringBuffer, tokenList) -> {
//        if (stringBuffer.length() > 0) {
//            tokenList.add(stringBuffer);
//        }
//        return "";
//    };
//    // 괄호 pair check
//    public static Function<String, List<String>> checkBracket = calcExp -> {
//        int length = calcExp.length();
//        List<String> msg = new ArrayList<>();
//        int squaredBracesOpenedDepth = 0;
//        int roundBracesOpenedDepth = 0;
//
//        for (int i = 0; i < length; i++) {
//            String token = String.valueOf(calcExp.charAt(i));
//            if (isLeftSquareBracket.test(token)) {
//                squaredBracesOpenedDepth += 1;
//            } else if (isRightSquareBracket.test(token)) {
//                squaredBracesOpenedDepth -= 1;
//            } else if (isLeftBracket.test(token)) {
//                roundBracesOpenedDepth += 1;
//            } else if (isRightBracket.test(token)) {
//                roundBracesOpenedDepth -= 1;
//            }
//        }
//        if (roundBracesOpenedDepth > 0) {
//            msg.add("There is(are) " + roundBracesOpenedDepth + " more open parentheses('(') than the closing parenthesis(')')");
//        } else if (roundBracesOpenedDepth < 0) {
//            msg.add("There is(are) " + (-roundBracesOpenedDepth) + " more closing parentheses(')') than the open parenthesis('(')");
//        }
//        if (squaredBracesOpenedDepth > 0) {
//            msg.add("There is(are) " + squaredBracesOpenedDepth + " more open square brackets('[') than the closing square brackets(']')");
//        } else if (squaredBracesOpenedDepth < 0) {
//            msg.add("There is(are) " + (-squaredBracesOpenedDepth) + " more closing square brackets(']') than the open square brackets('[')");
//        }
//        return msg;
//    };
//
//
//    /**
//     * 계산식 String을 토큰화하여 리스트로 반환함.
//     *
//     * @param calcExp 계산식
//     * @return 계산식 String을 토큰화하 된 리스트
//     */
//    public static Function<String, List<Object>> makeTextToToken = calcExp -> {
//        String sb = "";
//        int length = calcExp.length();
//        List<Object> tokenList = new ArrayList<>();
//
//        for (int i = 0; i < length; i++) {
//            String c = String.valueOf(calcExp.charAt(i));
//            if(isMinus.test(calcExp, i)){ //음수 의 '-' 이면
//                sb = sb.concat(c);
//                continue;
//            }
//            if (isOperation.test(c) || isSquareBracket.test(c) || isBracket.test(c) || isComma.test(c)) {
//                sb = emptyStringBuffer.apply(sb, tokenList);
//                tokenList.add(c);
//            } else {
//                sb = sb.concat(c);
//            }
//        }
//        if (!isOperation.test(sb) && !isSquareBracket.test(sb) && !isBracket.test(sb) && !isComma.test(sb) &&  !"".equals(sb)) {
//            tokenList.add(sb);
//        }
//        return tokenList;
//    };
//    // function 들의 parameter 개수 를 지정 하고 threthold 인 녀석은 0 으로 구분한다.
//    public static HashMap<String, Integer> functions = new HashMap<String, Integer>() {{
//        put("ABS", 1);
//        put("SQRT", 1);
//        put("AVERAGE", 0);
//        put("MEDIAN", 0);
//        put("ENTHALPY", 3);
//        put("SPREAD", 0);
//        put("MAXIMUM", 0);
//        put("SUM", 0);
//        put("SATURATION_PRESSURE", 1);
//        put("SATURATION_TEMPERATURE", 1);
//        put("SPECIFIC_VOLUME_LIQUID", 1);
//        put("PUMP_HYDRAULIC_POWER", 6);
//        put("AH_LEAKAGE", 2);
//        put("AH_GSE_NO_LEAKAGE", 3);
//        put("AH_GSE_LEAKAGE", 5);
//        put("AH_X_RATIO_NO_LEAKAGE", 4);
//        put("AH_X_RATIO_LEAKAGE", 6);
//        put("AH_THERMAL_POWER", 3);
//        put("STEAM_TURBINE_FLOW", 3);
//        put("HEAT_EX_LMTD", 5);        // * 마지막 변수는 boolean
//        put("HEAT_EX_THERMAL_POWER", 4);
//        put("ISENTROPIC_EFFICIENCY_ST_HP", 4);
//        put("ISENTROPIC_EFFICIENCY_ST_LP", 4);
//        put("ISENTROPIC_EFFICIENCY_ST_PUMP", 4);
//        put("PUMP_TDH", 3);
//        put("ISENTROPIC_EFFICIENCY_GT_COMPRESSOR", 2);
//        put("COMPRESSOR_PRESSURE_RATIO", 4);
//        put("COMPRESSOR_TEMPERATURE_RATIO", 2);
//        put("ISENTROPIC_EFFICIENCY_GT_TURBINE", 2);
//        put("THERMAL_EFFICIENCY_GT", 5);
//        put("GT_MWI", 3);
//
//        put("MINIMUM", 0);
//        put("IGV_ANTI_ICING_SP", 4);
//        put("SELECT", 3);
//        put("OR_EXIST", 2);
//        put("AND_EXIST", 2);
//        put("IS_EXIST", 1);
////        put("LIST", 2);
//        put("PREVIOUS", 2);
//    }};
//    // 사칙연산의 () 인지, 함수 호출의 ()인지 구분
//    private static int ifFunction(List<Object> list, int start, List<String> msg) {
//        if (start > 0) {
//            Object obj = list.get(start - 1);
//            if (obj instanceof String
//                    && !isOperation.test(obj)
//                    && !isSquareBracket.test(String.valueOf(obj))
//                    && !isBracket.test(obj)
//                    && !isComma.test(String.valueOf(obj))) {
//                start = obj instanceof String ? start - 1 : start;
//                if (!functions.containsKey(obj) && "PREVIOUS".equals(obj)) {                                            // 2) ( 앞에 function 인지 확인 한다. 있으면 앞 index 까지 포함 하려고..
//                    msg.add("\""+obj+"\"" + " is_not_function");
//                }
//            }
//        }
//        return start;
//    }
//    // 스택에 값을 넣음.
//    static BiConsumer<List<Object>, Object> pushStack = (stack, o) -> stack.add(o);
//
//    // 스택의 최상위 값을 조회함.
//    static Function<List<Object>, Object> getTopStack = stack -> stack.size() == 0 ? null : stack.get(stack.size() - 1);
//
//    // 스택의 최상위 값을 반환함.
//    static Function<List<Object>, Object> popStack = stack -> stack.remove(stack.size() - 1);
//
//    // 연산자 우선순위 반환
//    private static Function<Object, Integer> precedence = op -> {
//        switch ((String) op) {
//            case "(":
//                return 0;
//            case "+":
//            case "-":
//                return 1;
//            case "*":
//            case "/":
//                return 2;
//            case "^":
//                return 3;
//            default:
//                return 4;
//        }
//    };
//
//    /**
//     * 중위연산식의 토큰리스트를 후위연산식으로 변환
//     *
//     * @param tokenList String 계산식-> 토큰화된 리스트
//     * @return 후위연산식으로 변환된 리스트
//     */
//    public static Function<List<Object>, List<Object>> toPostfix = tokenList -> {
//        List<Object> postfixToken = new ArrayList<>();
//        List<Object> stack = new ArrayList<>();
//
//        for (Object token : tokenList) {
//            if (!isComma.test(String.valueOf(token))) {
//                if (isLeftBracket.test(token)) {                // "("
//                    pushStack.accept(stack, token);
//
//                } else if (isRightBracket.test(token)) {        // ")"
//                    while (!isLeftBracket.test(getTopStack.apply(stack))) {
//                        postfixToken.add(popStack.apply(stack));
//                    }
//                    popStack.apply(stack); // remove '('
//
//                } else if (isOperation.test(token) || functions.containsKey(String.valueOf(token)) || Arrays.asList("PREVIOUS").contains(token)) {   // operation
//                    while (getTopStack.apply(stack) != null && precedence.apply(getTopStack.apply(stack)) >= precedence.apply(token)) {    // stack에 들어있는 것이 우선순위가 높으면 pop
//                        postfixToken.add(popStack.apply(stack));
//                    }
//                    pushStack.accept(stack, token);
//
//                } else {    // Other (Tag Alias, '[', ']' etc...)
//                    postfixToken.add(token);
//                }
//            }
//        }
//        while (getTopStack.apply(stack) != null) {
//            if (!isComma.test(String.valueOf(getTopStack.apply(stack)))) {
//                postfixToken.add(popStack.apply(stack));
//            }
//        }
//        return postfixToken;
//    };
//    public static List<Object> executeCalc(List<Object> list, int start, int end, Map<String, PamTagMapping> pamTagMap, List<String> msg) {
//        List<Object> subList = list.subList(start, end);                            // 3) 필요 부분 잘라서
//        List<Object> postfixToken = toPostfix.apply(subList);                       // 4) 자른 String 후위연산 list 로 변환
//
//        List<Object> stack = new ArrayList<>();
//        List<String> errorMsg = new ArrayList<>();
//        if (postfixToken.size() == 0) {
////            return null;
//        }
//        for (Object token : postfixToken) {
//            if (isOperation.test(token)) {                                       // 연산자 [+, -, *, /, ^(제곱근)]
//                try {
//                    Object last = getAliasData(pamTagMap, popStack.apply(stack), errorMsg);
//                    Object first = getAliasData(pamTagMap, popStack.apply(stack), errorMsg);
//                    pushStack.accept(stack, executeOperation.apply(first, last, token));
//                } catch (Exception e) {
//                    errorMsg.add(getOperationErrMsg.apply("[" + token + "]"));
//                    pushStack.accept(stack, 1.0);
//                }
//                continue;
//            } else if (isInActive.test(token)) {                                 // inactive
//                pushStack.accept(stack, null);
//                continue;
//            } else if (Arrays.asList("PREVIOUS").contains(token)) {
//                getAliasDataList(pamTagMap, stack, errorMsg, (String) token);
//                continue;
//            } else if (functions.containsKey(String.valueOf(token))) {// function
//                setAliasData(stack, pamTagMap, errorMsg);
//                String result = callFunction((String) token, stack);
//                if (!"".equals(result)) {
//                    errorMsg.add(result);
//                }
//                pushStack.accept(stack, 1.0);
//                continue;
//            } else {
//                pushStack.accept(stack, token);
//                continue;
//            }
//        }
//        if (errorMsg.size() > 0) {
//            msg.addAll(errorMsg);
//        }
//        List<Object> result = new ArrayList<>();
//        result.addAll(list.subList(0, start));
//        result.addAll(stack);
//        result.addAll(list.subList(end, list.size()));
//
//        return result;
//    }
//    /**
//     * 계산식 validation check
//     *
//     * @param calcExpression 계산식
//     * @param selfPamValue pam_mapping의 tag_alias list
//     * @param parentPamValue model 일 경우 상위 asset pam_mapping의 tag_alias list
//     * @return 오류 메시지
//     */
//    // 자기 자신 tag_alias도 가능해야 함... list, previous 추가 되었기 때문
//    public static BiFunction<String, Map<String, PamTagMapping>, List<String>> checkCalcExp = (calcExpression, pamTagMap) -> {
//        calcExpression = trimAndUpperCase.apply(calcExpression);
//        List<String> msg = new ArrayList<>();
//        List<Object> result =null;
//        try {
//            msg.addAll(checkBracket.apply(calcExpression));                                 // 0. bracket validation//괄호 [, ], (, ) 체크
//            List<Object> list = makeTextToToken.apply(calcExpression);                      // 1. String 을 tag_alias, 연산자, function, bracket 등의 단위로 list 로 변환
//            while (list.indexOf("(") != -1 && list.indexOf(")") != -1) {                    // 2. () 안의 연산 [()안의 사칙연산, 함수]
//                int end = list.indexOf(")") + 1;                                                    // 1) )를 찾는다.
//                for (int j = end - 1; j >= 0; j--) {
//                    int start = j;
//                    if ("(".equals(list.get(j))) {
//                        start = ifFunction(list, start, msg);
//                        list = executeCalc(list, start, end, pamTagMap, msg);             // 5) 연산 처리 하면서 오류 msg String | 오류가 있으도 1.0 으로 return 계속 진행 해서 오류 전체를 String 으로 저장
//                        break;
//                    }
//                }
//            }
//            result = executeCalc(list, 0, list.size(), pamTagMap, msg);  // ()연산 이후  연산 처리 하면서 오류 msg String | 오류가 있으도 1.0 으로 return 계속 진행 해서 오류 전체를 String 으로 저장
//            if (result.equals(null) || result.size() > 1) { // TODO return 할 때  stack.size() == 1 and isNumber(popStack(stack)) false 이면 문제 있는 것임
//                System.out.println(result + " calculation couldn't be completed");
//            }
//        } catch (Exception e) {
//            System.out.println(String.valueOf(e));
//        }
//        return msg;
//    };
//
//}
