package main.unitConversion;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * 단위들 의 비율 및 온도 변환 계산식 모음
 *
 */
public class Units {

    /**
     * input Unit -> K (켈빈) 으로 변환 하는 함수 모음
     */
    public static final Map<String, Function<Double, Double>> TEMPERATURE_TO_STANDARD = Collections.unmodifiableMap(new HashMap<String, Function<Double, Double>>() {{
        put("°C", v -> v + 273.15);
        put("°F", v -> (v + 459.67) * 5 / 9);
        put("°R", v -> v * 5 / 9);
        put("K", v -> v);
    }});

    /**
     * K (켈빈) -> target Unit 으로 변환 하는 함수 모음
     */
    public static final Map<String, Function<Double, Double>> TEMPERATURE_TO_OTHER = Collections.unmodifiableMap(new HashMap<String, Function<Double, Double>>() {{
        put("°C", v -> v - 273.15);
        put("°F", v -> (v * 9 / 5) - 459.67);
        put("°R", v -> v * 9 / 5);
        put("K", v -> v);
    }});

    /**
     * pressure 단위 비율
     * standard = MPa
     */
    public static final Map<String, Double> PRESSURE = Collections.unmodifiableMap(new HashMap<String, Double>() {{
        put("MPa", (double) 1);
        put("Mpa", (double) 1);                         // p가 소문자는 DB에 들어 있어서 추가 함
        put("kPa", (double) 1000);
        put("bar", (double) 10);
        put("psi", (double) Math.pow(254, 2) / 444.82216152605);//145.038
        put("mmHg", (double) 7500.616827041698);
        put("inHg", (double) 7600 / (254 * 0.101325));

        put("mmH", (double) 1/ 0.00000980665);          // unit_conversion_master 에 없음
        put("inH", (double) 1/ 254 * 0.000000980665);   // unit_conversion_master 에 없음
        put("kgf", (double) 1/ 0.0980665);              // unit_conversion_master 에 없음

        put("mbar", (double) 10000);                    // macro 에 없음
        put("mmH2O", (double) 101971.621);              // macro 에 없음 mmH2O   = mmH 인 것 으로 추정됨
        put("inH2O", (double) 4014.63015);              // macro 에 없음 inH2O   = inH 인 것 으로 추정됨
        put("kgf/cm2", (double) 10.1971621);            // macro 에 없음 kgf/cm2 = kgf 인 것 으로 추정됨
        put("Pa", (double) 1000000);
    }});

    /**
     * specific_volume 단위 비율
     * standard = m3/kg
     */
    public static final Map<String, Double> SPECIFIC_VOLUME = Collections.unmodifiableMap(new HashMap<String, Double>() {{
        put("m3/kg", (double) 1);
        put("ft3/lb", (double) 0.45359237 / Math.pow(0.3048, 3));
        put("cm3/g", (double) 1000); // 아래와 같다 (구글링 해서 찾은 값)
        put("mL/g", (double) 1000);
    }});

    /**
     * specific_density 단위 비율
     * standard = kg/m3
     */
    public static final Map<String, Double> SPECIFIC_DENSITY = Collections.unmodifiableMap(new HashMap<String, Double>() {{
        put("kg/m3", (double) 1);
        put("lb/ft3", (double) Math.pow(0.3048, 3) / 0.45359237 );
        put("g/cm3", (double) 0.001); // 아래와 같다 (구글링 해서 찾은 값)
        put("g/mL", (double) 0.001);
    }});

    /**
     * specific_internal_energy | specific_enthalpy 단위 비율
     * standard = kJ/kg
     */
    public static final Map<String, Double> SPECIFIC_INTERNAL_ENERGY = Collections.unmodifiableMap(new HashMap<String, Double>() {{
        put("MJ/kg", (double) 0.001);
        put("BTU/lb", (double) 45359237 / 105506000);
        put("kcal/kg", (double) 1 / 4.1868);
        put("kJ/kg", (double) 1);
    }});

    /**
     * specific_enthalpy | specific isobaric heat capacity | specific isochoric heat capacity 단위 비율
     * standard = kJ/kgK
     */
    public static final Map<String, Double> CAPACITY = Collections.unmodifiableMap(new HashMap<String, Double>() {{
        put("kJ/kgK", (double) 1);
        put("BTU/lb°F", (double) 1/4.1868);
        put("kcal/kg°C", (double) 1/4.1868);
        put("kcal/kgK", (double) 0.238845896627); // 요거는 없는거 같음
    }});

    /**
     * speed_of_sound 단위 비율
     * standard = m/s
     */
    public static final Map<String, Double> SPEED_OF_SOUND = Collections.unmodifiableMap(new HashMap<String, Double>() {{
        put("m/s", (double) 1);
        put("ft/s", (double) 1 / 0.3048);
    }});

    /**
     * macro 에 없음
     * electricity 단위 비율
     * standard = MW
     */
    public static final Map<String, Double> ELECTRICITY = Collections.unmodifiableMap(new HashMap<String, Double>() {{
        put("MW", (double) 1);
        put("kW", (double) 1000);
        put("W", (double) 1000000);
    }});

    /**
     * macro 에 없음
     * electric_current 단위 비율
     * standard = A
     */
    public static final Map<String, Double> ELECTRIC_CURRENT = Collections.unmodifiableMap(new HashMap<String, Double>() {{
        put("A", (double) 1);
        put("mA", (double) 1000);
    }});

    /**
     * macro 에 없음
     * mass_flow_rate 단위 비율
     * standard = kg/s
     */
    public static final Map<String, Double> MASS_FLOW_RATE = Collections.unmodifiableMap(new HashMap<String, Double>() {{
        put("kg/s", (double) 1);
        put("t/h", (double) 3.6);
    }});


    /**
     * macro 에 없음
     * volumetric_flow_rate 단위 비율
     * standard = m3/hr
     */
    public static final Map<String, Double> VOLUMETRIC_FLOW_RATE = Collections.unmodifiableMap(new HashMap<String, Double>() {{
        put("m3/hr", (double) 1);
        put("m3/s", (double) 0.000277778);
//        put("Nm3/sec", (double) 0); ??
    }});

    /**
     * property_cd | property 모음
     */
    public static final Map<String, Map<String, Double>> UNITS = Collections.unmodifiableMap(new HashMap<String, Map<String, Double>>() {{
        put("P", PRESSURE);
        put("V", SPECIFIC_VOLUME);
        put("D", SPECIFIC_DENSITY);
        put("U", SPECIFIC_INTERNAL_ENERGY);
        put("S", CAPACITY);
        put("H", SPECIFIC_INTERNAL_ENERGY);
        put("CP", CAPACITY);
        put("CV", CAPACITY);
        put("W", SPEED_OF_SOUND);
        put("E", ELECTRICITY);
        put("EC", ELECTRIC_CURRENT);
        put("M", MASS_FLOW_RATE);
        put("VFR", VOLUMETRIC_FLOW_RATE);
//        put("T", temperature);
    }});
}
