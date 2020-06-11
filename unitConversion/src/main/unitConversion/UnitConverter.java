package main.unitConversion;

import static main.unitConversion.Units.*;

/**
 * Units 의 단위 비윺및 계산식을 이용 하여 단위 환산 해주는 converter
 */
public class UnitConverter {

    /**
     * fromUnit -> toUnit 으로 단위 환산 해주는 함수
     *
     * @param cd       단위의 타입
     * @param fromUnit input 단위
     * @param value    input value
     * @param toUnit   output 단위
     * @return
     */
    public static Double conversion(Double value, String cd, String fromUnit, String toUnit) {
        Double result;
        try {
            result = fromUnit.equals(toUnit)
                    ? value
                    : toTargetUnit(toStandard(value, cd, fromUnit), cd, toUnit);
        } catch (Exception e) {
            result = value;
        }
        System.out.println(value + " "+fromUnit+ " -> " + result+ " "+toUnit);
        return result;
    }

    /**
     * toStandard(프로그램 내부의 기준 으로 변경)
     * 개발 당시 에는 default_yn 맞추어 stardard 를 설정 했지만
     * default_yn은 변경 가능 하므로 starndard 가 아닐 수도 있으 므로 toStandard 는 프로그램 내부의 기준 으로 생각 해야 함
     *
     * @param value    input value
     * @param cd       property_cd
     * @param fromUnit input unit
     * @return
     */
    public static Double toStandard(Double value, String cd, String fromUnit) {
        return "T".equals(cd)
                ? TEMPERATURE_TO_STANDARD.get(fromUnit).apply(value)
                : value / UNITS.get(cd).get(fromUnit);
    }

    /**
     * 원하는 단위로 변경
     *
     * @param standardValue 내부 기준 단위로 변경된 value
     * @param cd            property_cd
     * @param toUnit        target Unit
     * @return
     */
    public static Double toTargetUnit(Double standardValue, String cd, String toUnit) {
        return "T".equals(cd)
                ? TEMPERATURE_TO_OTHER.get(toUnit).apply(standardValue)
                : standardValue * UNITS.get(cd).get(toUnit);
    }
}
