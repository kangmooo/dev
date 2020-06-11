package main.calcExpValidation;

import java.util.function.Function;

import static main.calcExpValidation.ReservedWords.functions;

public class Messages {

    public static Function<String, String> makeMsg = str -> str + ". \r\n";
    public static Function<String, String> getArgsNumberErrMsg = func -> makeMsg.apply(func + " arguments must be " + functions.get(func));
    public static Function<String, String> getArgsErrMsg = func -> makeMsg.apply(func + " arguments type mismatch");
    public static Function<String, String> getOperationErrMsg = func -> makeMsg.apply("Arithmetic operation problem " + func);

}
