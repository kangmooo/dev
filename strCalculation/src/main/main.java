package main;

import main.calcExpValidation.CalcExpValidationController;

import java.util.HashMap;

public class main {
    public static void main(String[] args) {

        HashMap<String, Object> testDataMap = new HashMap<String, Object>() {{
            put("EDDY", 1);
            put("PORORO", 2);
            put("POBI", 4);
            put("KRONG", 3);
        }};

        System.out.println(CalcExpValidationController.checkCalcExp.apply("eddy+pororo", testDataMap));
    }
}



