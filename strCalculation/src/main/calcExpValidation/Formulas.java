package main.calcExpValidation;

import java.util.Stack;
import java.util.function.Function;

import static main.calcExpValidation.Messages.getArgsErrMsg;
import static main.calcExpValidation.ReservedWords.isBoolean;
import static main.calcExpValidation.ReservedWords.isNumber;
import static main.calcExpValidation.ValidCheckFuncs.checkFuncAgrs;
import static main.calcExpValidation.ValidCheckFuncs.thresholdCheck;

public class Formulas {
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

    /**
     * calculation function 을 호출하여 연산을 수행
     *
     * @param func  31개의 function 중의 수행해야 될 function
     * @param stack 수행될 data가 포함된 stack
     * @return calculation function 결과값
     */
    public static String callFunction(String func, Stack<Object> stack) {
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
}
