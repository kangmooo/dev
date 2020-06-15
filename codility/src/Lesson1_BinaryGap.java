import org.junit.jupiter.api.Test;

public class Lesson1_BinaryGap {

    @Test
    public void test() {
        System.out.println(solution(112312300));
    }

    public int solution(int N) {
        String binaryStr = Integer.toBinaryString(N);
        System.out.println(binaryStr);
        int length = binaryStr.length();
        int maxZeroCnt = 0;
        int zeroCnt = 0;

        for (int i = 0; i < length; i++) {
            if ('0' == binaryStr.charAt(i)) {
                zeroCnt++;
            } else {
                maxZeroCnt = Math.max(zeroCnt, maxZeroCnt);
                zeroCnt = 0;
            }
        }
        return maxZeroCnt;
    }
}
