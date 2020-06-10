package main;

import java.util.HashMap;

public class main {
    public static void main(String[] args) {

        HashMap<String, Object> testDataMap = new HashMap<String, Object>() {{
            put("eddy", 1);
            put("pororo", 2);
            put("krong", 3);
            put("pobi", 4);
        }};

        CalcExpValidator.checkCalcExp.apply("eddy+pororo", testDataMap);
    }
}



