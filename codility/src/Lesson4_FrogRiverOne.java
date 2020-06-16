import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

public class Lesson4_FrogRiverOne {

    @Test
    public void test() {
        int[] A = {1, 3, 1, 4, 2, 3, 5, 4};
        int result = solution(5, A);
        System.out.println(result);
    }

    public int solution(int X, int[] A) {
        int result = -1;
        int length = A.length;
        Set set = new HashSet<Integer>();
        for (int i = 0; i < length; i++) {
            if (A[i] <= X) {
                set.add(A[i]);
                if (set.size() == X) {
                    result = i;
                    break;
                }
            }
        }
//        Map map = new HashMap<>();
//        for (int i = 0; i < length; i++) {
//            if (A[i] <= X) {
//                map.put(A[i],true);
//                if (map.size() == X) {
//                    result = i;
//                    break;
//                }
//            }
//        }
        return result;
    }
}
