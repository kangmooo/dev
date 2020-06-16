import org.junit.jupiter.api.Test;

public class Lesson3_TapeEquilibrium {

    @Test
    public void test() {
        int[] A = {1, 2, 3, 4, 5, 6, 11, 8, 9, 10};
        int result = solution(A);
        System.out.println(result);
    }

    public int solution(int[] A) {
        int sum_fromFirst = 0;
        int sum_toLast = 0;

        for (int i = 0; i < A.length; i++) {
            sum_toLast += A[i];
        }

        int minDiff = Integer.MAX_VALUE;

        for (int p = 0; p < A.length -1 ; p++) {
            sum_fromFirst += A[p];
            sum_toLast -= A[p];

            int diff = Math.abs(sum_fromFirst - sum_toLast);

            if (diff < minDiff) {
                minDiff = diff;
            }
        }

        return minDiff;
    }
}
