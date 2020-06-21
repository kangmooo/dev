package solutions;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RemoveDupl {
    public static void solution(List<Integer> list) {
        int size = list.size();
        Set set = new HashSet();

        for (int i = 0; i < size; i++) {
            set.add(list.get(i));
        }
        List result = Arrays.asList(set.toArray());
        System.out.println(result);
    }
}