package d_encoding_decoding;

import java.util.Arrays;

public class EncodingPrerequisiteTwo {
    public static void main(String[] args){

        // String serialization
        String x = "hello";
        byte[] y = x.getBytes();
        System.out.println(Arrays.toString(y));
    }
}
