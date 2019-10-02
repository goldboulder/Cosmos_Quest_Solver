/*

 */
package cosmosquestsolver;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Base64;

//miscelanius functions
public class OtherThings {
    
    private static DecimalFormat intCommaFormat = new DecimalFormat("###,###,###,###");
    private static DecimalFormat twoDecimalFormat = new DecimalFormat("##.00");
    private static DecimalFormat sciNotFormat = new DecimalFormat("0.#E0");
    
    public static double fact(int num){
        double ans = 1;
        for (int i = 2; i <= num; i++){
            ans *= i;
        }
        return ans;
    }
    
    public static long nCr(int n, int r){
        long num = 1;
        for (int i = n; i >= n - r + 1; i --){
            num *= i;
        }
        return num/(long)fact(r);
    }

    public static String intToCommaString(long damage) {
        return intCommaFormat.format(damage);
    }
    
    public static String twoDecimalFormat(double num) {
        return twoDecimalFormat.format(num);
    }
    
    public static String intOrNiceDecimalFormat(double num){
        if (num % 1 == 0){
            return Integer.toString((int) num);
        }
        else if ((num*10)%1 == 0){
            return Double.toString(num);
        }
        else{
            return twoDecimalFormat.format(num);
        }
    }
    
    public static String nicePercentString(double percent){
        String ans;
        double percent100 = percent * 100;
        if (percent100 % 1 == 0){
            ans = Integer.toString((int)percent100) + "%";
        }
        else{
            double rounded = Math.round(percent100 * 100) / 100.0;
            ans = Double.toString(rounded) + "%";
        }
        return ans;
    }
    
    public static String encodeBase64(String str) throws UnsupportedEncodingException{//error handling?
        byte[] bytes = str.getBytes("UTF-8");
        return Base64.getEncoder().encodeToString(bytes);
    }
    
    public static String decodeBase64(String code){
        byte[] decoded = Base64.getDecoder().decode(code);
        return new String(decoded);
    }

    public static String SciNotStr(double num) {
        return sciNotFormat.format(num);
    }
    
}
