package solutions;

public class ReverseString {
    public static void solution(String str) {
        int length = str.length();
        for (int i = length - 1; i >= 0; i--) {
            System.out.println(str.charAt(i));
        }
    }
}