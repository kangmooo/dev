import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class Lesson3_OddOccurrencesInArray {

    @Test
    public void test() {
        int[] A = {1, 1, 3, 3, 5, 5, 7, 7, 7, 10};
        int result = solution(A);
        System.out.println(result);
    }

    public int solution(int[] A) {
        int length = A.length;
        int result = 0;
        Map<Integer, Integer> map = new HashMap();

        for (int i = 0; i < length; i++) {
            int self = A[i];
            boolean bool = map.containsKey(self);
            if (bool) {
                map.put(self, map.get(self) + 1);
            } else {
                map.put(self, 1);
            }
        }
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            Integer k = entry.getKey();
            Integer v = entry.getValue();
            if (v % 2 != 0) {
                result = k;
            }
        }
        return result;
    }
}
