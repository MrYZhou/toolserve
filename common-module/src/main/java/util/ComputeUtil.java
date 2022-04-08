package util;

public class ComputeUtil {
    /**
     * 计算平均值
     * @param x
     * @param y
     * @return
     */
    public static int get(int x,int y){
     return (x & y) + ((x ^ y) >> 1);
    }
}
