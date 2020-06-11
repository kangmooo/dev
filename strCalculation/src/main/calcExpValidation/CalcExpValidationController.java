package main.calcExpValidation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static main.calcExpValidation.Postfix.makeTextToToken;
import static main.calcExpValidation.Postfix.trimAndUpperCase;
import static main.calcExpValidation.ReservedWords.isFunction;
import static main.calcExpValidation.ValidCheckFuncs.checkBracket;
import static main.calcExpValidation.ValidCheckFuncs.executeCalc;

public class CalcExpValidationController {

    /**
     * 계산식 validation check
     * () 를 우선 순위로 계산을 하고 연산 하도록 한다.
     *
     * @param calcExpression 계산식
     * @param selfPamValue pam_mapping의 tag_alias list
     * @param parentPamValue model 일 경우 상위 asset pam_mapping의 tag_alias list
     * @return 오류 메시지
     */
    public static BiFunction<String, Map<String, Object>, List<String>> checkCalcExp = (calcExpression, aliasDataMap) -> {
        calcExpression = trimAndUpperCase.apply(calcExpression);
        List<String> msg = new ArrayList<>();
        try {
            checkBracket.apply(calcExpression, msg);                                        // 0. bracket validation//괄호 [, ], (, ) 체크
            List<Object> list = makeTextToToken.apply(calcExpression);                      // 1. String 을 tag_alias, 연산자, function, bracket 등의 단위로 list 로 변환
            while (list.contains("(") && list.contains(")")) {                              // 2. () 안의 연산 [()안의 사칙연산, 함수]
                int end = list.indexOf(")") + 1;                                                    // 1) ')'를 찾는다.
                for (int j = end - 1; j >= 0; j--) {
                    int start = j;
                    if ("(".equals(list.get(j))) {                                                  // 2) '(' 를 찾는다.
                        start = (isFunction(list, start, msg)) ? start - 1 : start;                 // 3) 앞의 obj 가 String 이면 함수라고 생각 하고 함수 명까지 포함 하여 executeCalc를 수행 하게 한다.
                        list = executeCalc(list, start, end, aliasDataMap, msg);                       // 4) 연산 처리 하면서 오류 msg String | 오류가 있으도 1.0 으로 return 계속 진행 해서 오류 전체를 String 으로 저장
                        break;
                    }
                }
            }
            List<Object> result = executeCalc(list, 0, list.size(), aliasDataMap, msg);  // ()연산 이후  연산 처리 하면서 오류 msg String | 오류가 있으도 1.0 으로 return 계속 진행 해서 오류 전체를 String 으로 저장
            if (result.equals(null) || result.size() > 1) { // return 할 때  stack.size() == 1 and isNumber(popStack(stack)) false 이면 문제 있는 것임
                System.out.println(result + " calculation couldn't be completed");
            }else {
                System.out.println("result = " + result);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return msg;
    };
}

