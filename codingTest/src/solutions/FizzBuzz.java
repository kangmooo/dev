package solutions;

public class FizzBuzz {
    public static void solution() {
        for (int i = 1; i < 101; i++) {
            String result = "";
            if (i % 3 == 0) {
                result += "Fizz";
            }
            if (i % 5 == 0) {
                result += "Buzz";
            }
            if (i % 3 != 0 && i % 5 != 0) {
                result += i;
            }
            System.out.println(result);
        }
    }
}
