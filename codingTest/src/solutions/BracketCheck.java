package solutions;

public class BracketCheck {
    public static boolean solution(String str) {
        int length = str.length();
        int cnt = 0;
        for (int i = 0; i <length; i++) {
            char ch = str.charAt(i);
            if('('==ch) cnt++;
            if(')'==ch) cnt--;
            if(cnt<0){
                break;
            }
        }
        boolean result = (cnt == 0) ? true : false;
        System.out.println(result);
        return result;
    }
}
