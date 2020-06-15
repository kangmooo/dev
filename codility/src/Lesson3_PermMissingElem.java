import org.junit.jupiter.api.Test;

public class Lesson3_PermMissingElem {

    @Test
    public void test() {
        int[] A = {1, 2, 3, 4, 5, 6, 11, 8, 9, 10};
        int result = solution(A);
        System.out.println(result);
    }

    public int solution(int[] A) {
        int result = 0;
        // 1부터 이기때문에 +1 빠진 숫자가 있기 때문에 +1 해서 2를 length 애 추가 해 준다.
        boolean[] booleans = new boolean[A.length + 2];

        // 주어진 배열이 있는 숫자는  boolean list 의 index로 해서 값을 true 로 저장한다.
        for (int i = 0; i < A.length; i++) {
            booleans[A[i]] = true;
        }
        // 리스트에서 true 가 아닌 값의 index 를 반환한다.
        for (int i = 1; i < booleans.length; i++) {
            if (!booleans[i]) {
                result = i;
                break;
            }
        }
        return result;
    }
}
