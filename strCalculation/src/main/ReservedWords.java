package main;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * Character Classification 문자 분류
 * (* 계산식의 string의 유형 구분 하는 용도로 쓰임)
 *
 * @author SungTae, Kang
 */
@Slf4j
public final class ReservedWords {

    static List<String> operations = Arrays.asList("+", "-", "*", "/", "^");
    static List<String> squareBracket = Arrays.asList("[", "]");
    static List<String> roundBracket = Arrays.asList("(", ")");

    // function 들의 parameter 개수 를 지정 하고 threthold 인 녀석은 0 으로 구분한다.
    public static HashMap<String, Integer> functions = new HashMap<String, Integer>() {{
        put("ABS", 1);
        put("SQRT", 1);
        put("AVERAGE", 0);
        put("MEDIAN", 0);
        put("ENTHALPY", 3);
        put("SPREAD", 0);
        put("MAXIMUM", 0);
        put("SUM", 0);
        put("SATURATION_PRESSURE", 1);
        put("SATURATION_TEMPERATURE", 1);
        put("SPECIFIC_VOLUME_LIQUID", 1);
        put("PUMP_HYDRAULIC_POWER", 6);
        put("AH_LEAKAGE", 2);
        put("AH_GSE_NO_LEAKAGE", 3);
        put("AH_GSE_LEAKAGE", 5);
        put("AH_X_RATIO_NO_LEAKAGE", 4);
        put("AH_X_RATIO_LEAKAGE", 6);
        put("AH_THERMAL_POWER", 3);
        put("STEAM_TURBINE_FLOW", 3);
        put("HEAT_EX_LMTD", 5);        // * 마지막 변수는 boolean
        put("HEAT_EX_THERMAL_POWER", 4);
        put("ISENTROPIC_EFFICIENCY_ST_HP", 4);
        put("ISENTROPIC_EFFICIENCY_ST_LP", 4);
        put("ISENTROPIC_EFFICIENCY_ST_PUMP", 4);
        put("PUMP_TDH", 3);
        put("ISENTROPIC_EFFICIENCY_GT_COMPRESSOR", 2);
        put("COMPRESSOR_PRESSURE_RATIO", 4);
        put("COMPRESSOR_TEMPERATURE_RATIO", 2);
        put("ISENTROPIC_EFFICIENCY_GT_TURBINE", 2);
        put("THERMAL_EFFICIENCY_GT", 5);
        put("GT_MWI", 3);
        put("MINIMUM", 0);
        put("IGV_ANTI_ICING_SP", 4);
        put("SELECT", 3);
        put("OR_EXIST", 2);
        put("AND_EXIST", 2);
        put("IS_EXIST", 1);
        put("PREVIOUS", 2);
    }};

    // 사칙연산 및 제곱근 '+' '-' '*' '/' '^'
    public static Predicate<Object> isOperation = str -> operations.contains(str);
    // boolean 값 체크
    public static Predicate<String> isBoolean = str -> Boolean.valueOf(str);
    // inactive 상태 확인
    public static Predicate<Object> isInActive = str -> "INACTIVE".equals(str);
    // 대괄호 '[', ']'
    public static Predicate<String> isSquareBracket = str -> squareBracket.contains(str);
    // 괄호 '(', ')'
    public static Predicate<Object> isBracket = str -> roundBracket.contains(str);
    // 쉼표     ','
    public static Predicate<String> isComma = str -> ",".equals(str);
    // 숫자 체크
    public static Predicate<Object> isNumber = str -> {
        try {
            Double.parseDouble(String.valueOf(str));
            return true;
        } catch (Exception e) {
            return false;
        }
    };

    // threshold check 를 하는 함수 인지 확인
    public static Predicate<String> isThresholdCheckFunc = str -> Arrays.asList("AVERAGE", "MEDIAN", "SPREAD", "MAXIMUM", "MINIMUM", "SUM").contains(str);

    // 마이너스 체크( true:단항의 음수 | false:이항 연산의 "-" 에 따라 체크)
    // true : 단항의 음수, false 2항의 연산
    public static BiPredicate<String, Integer> isMinus = (str, i) -> {
        if (str.charAt(i) == '-' && i == 0) {                       // 단항의 음수 ( '-' 처음 으로 올때에는 계산식이 아님 )
            return true;
        } else if (str.charAt(i) == '-' && str.length() - 1 == i) {     // '-' 가 마지막 일때 이항 연산 error
            return false;               // 이항 연산
        } else if (str.charAt(i) == '-' && isNumber.test(str.substring(i + 1, i + 2))) {
            return (isOperation.test(String.valueOf(str.charAt(i - 1))) || isComma.test(String.valueOf(str.charAt(i - 1))) || str.charAt(i - 1) == '(' || str.charAt(i - 1) == '[');
        } else {
            return false;
        }
    };

}
