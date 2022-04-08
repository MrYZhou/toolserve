package BaseUtil;

public class ComputeUtil {

    public static int get(int x,int y){
     return (x & y) + ((x ^ y) >> 1);
    }
}
