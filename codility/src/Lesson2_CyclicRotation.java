import org.junit.jupiter.api.Test;

public class Lesson2_CyclicRotation {


    @Test
    public void test() {
        int[] A = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int[] result = solution(A, 26);
        System.out.println(result);
    }

    public int[] solution(int[] A, int K) {
        int length = A.length;
        if(length==0){
            return A;
        }else {
            int index = K % length;
            int[] result = new int[length];

            for (int i = 0; i < length; i++) {
                int setIdx = length - index + i;
                if(setIdx >= length) {
                    setIdx = setIdx - length;
                }
                result[i] = A[setIdx];
            }
            return result;
        }
    }
}
