import org.junit.jupiter.api.Test;
import solutions.BracketCheck;

public class TestMain {
    @Test
    void test(){
//        FizzBuzz.solution();
//        ReverseString.solution("ABCDEFG");
//        RemoveDupl.solution(Arrays.asList(1,1,1,1,1,1,2,2,2,2,3,3,3,3,6,6,6,7,7,7,11,9,8,7,3,3,3,3,3,1,1,1,1));
        BracketCheck.solution("()(");
        BracketCheck.solution("))(");
        BracketCheck.solution(")(");
        BracketCheck.solution("(");
        BracketCheck.solution("()");
    }
}
