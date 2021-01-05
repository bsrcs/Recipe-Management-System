package recipemng.util;

import org.apache.commons.codec.digest.DigestUtils;

public class HashUtil {
    public static String getHash(String inputString){
        return DigestUtils.sha256Hex(inputString);
    }
}
