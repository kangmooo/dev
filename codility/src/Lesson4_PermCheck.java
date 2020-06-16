import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class Lesson4_PermCheck {

    @Test
    public void test() {
        int[] A = {1, 3, 6, 4, 1, 2};
        int result = solution(A);
        System.out.println(result);
    }
    // test 더 진행 해야함
    public int solution(int[] A) {
        int length = A.length;
        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            list.set(A[i], A[i]);
        }
        int cnt = 0;
        for (int i = 0; i < list.size(); i++) {
            cnt+=1;
            if(cnt != list.get(i)){
                return 0;
            }

        }
        return 1;
    }
}
