import org.junit.jupiter.api.Test;

public class Lesson3_FrogJmp {

    @Test
    public void test() {
        int result = solution(1, 229, 3);
        System.out.println(result);
    }

    public int solution(int X, int Y, int D) {
        return (int) Math.ceil((Y - X) / (double) D);
    }
}
