import org.junit.jupiter.api.Test;

public class Lesson4_MissingInteger {

    @Test
    public void test() {
        int[] A = {1, 3, 6, 4, 1, 2};
        int result = solution(A);
        System.out.println(result);
    }

    public int solution(int[] A) {
        boolean[] checker = new boolean[A.length + 1];
        int checkCount = 0;
        int num;
        for (int i = 0; i < A.length; i++) {
            num = A[i];
            if (0 < num && num < checker.length) checker[num] = true;   //양수 일 때만 체크
        }

        for (int i = 1; i < checker.length; i++) {
            if(checker[i]) checkCount++;
            else return i;
        }
        return checkCount == (checker.length - 1) ? checker.length : 1;   //모두 만족하면 그 다음수 리턴 아니면 모두 음수이므로 양의 최소값 1 리턴
    }
}
