import org.junit.jupiter.api.Test;

public class Lesson3_FrogJmp {

    @Test
    public void test() {
        int result = solution(1, 229, 3);
        System.out.println(result);
    }

    public int solution(int X, int Y, int D) {
        // 나머지가 있으면 + 1을 해줘야 하는데 올림 처리
        return (int) Math.ceil((Y - X) / (double) D);
    }
}
