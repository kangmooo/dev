package batch;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Unisteam 함수를 호출 ( C:\\fileName.dll 을 load 하여 호출 )
 *  파일의  package 또한 유지해 주어야 함 (dll 파일에 이미 정의 되어 있기 때문)
 *
 * @author SungTae, Kang
 */
class LoadDll {

    static {
        Properties prop = new Properties();
        String path = "";
        InputStream inputStream = LoadDll.class.getClassLoader().getResourceAsStream("application.yml");
        try {
            prop.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        path = prop.getProperty("dll_path");
        System.load(path);
//        System.load("D:\\fileName.dll");
    }
    static native double dllFucntion1(double param1, double param2, int param3);   // dll에 명시한 함수 정의
    static native double dllFucntion2(double param1, double param2, int param3);   // dll에 명시한 함수 정의
    static native double dllFucntion3(double param1, double param2, int param3);   // dll에 명시한 함수 정의
}