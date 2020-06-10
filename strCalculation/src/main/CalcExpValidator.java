package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static main.ValidCheckFuncs.*;

public class CalcExpValidator {

    /**
     * 계산식 validation check
     *
     * @param calcExpression 계산식
     * @param selfPamValue pam_mapping의 tag_alias list
     * @param parentPamValue model 일 경우 상위 asset pam_mapping의 tag_alias list
     * @return 오류 메시지
     */
    // 자기 자신 tag_alias도 가능해야 함... list, previous 추가 되었기 때문
    public static BiFunction<String, Map<String, Object>, List<String>> checkCalcExp = (calcExpression, pamTagMap) -> {
        calcExpression = trimAndUpperCase.apply(calcExpression);
        List<String> msg = new ArrayList<>();
        try {
            msg.addAll(checkBracket.apply(calcExpression));                                 // 0. bracket validation//괄호 [, ], (, ) 체크
            List<Object> list = makeTextToToken.apply(calcExpression);                      // 1. String 을 tag_alias, 연산자, function, bracket 등의 단위로 list 로 변환
            while (list.indexOf("(") != -1 && list.indexOf(")") != -1) {                    // 2. () 안의 연산 [()안의 사칙연산, 함수]
                int end = list.indexOf(")") + 1;                                                    // 1) )를 찾는다.
                for (int j = end - 1; j >= 0; j--) {
                    int start = j;
                    if ("(".equals(list.get(j))) {
                        start = (isFunction(list, start, msg)) ? start - 1 : start;
                        list = executeCalc(list, start, end, pamTagMap, msg);             // 5) 연산 처리 하면서 오류 msg String | 오류가 있으도 1.0 으로 return 계속 진행 해서 오류 전체를 String 으로 저장
                        break;
                    }
                }
            }
            List<Object> result = executeCalc(list, 0, list.size(), pamTagMap, msg);  // ()연산 이후  연산 처리 하면서 오류 msg String | 오류가 있으도 1.0 으로 return 계속 진행 해서 오류 전체를 String 으로 저장
            if (result.equals(null) || result.size() > 1) { // TODO return 할 때  stack.size() == 1 and isNumber(popStack(stack)) false 이면 문제 있는 것임
                System.out.println(result + " calculation couldn't be completed");
            }
        } catch (Exception e) {
            System.out.println(String.valueOf(e));
        }
        return msg;
    };
}

