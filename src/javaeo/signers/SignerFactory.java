package javaeo.signers;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Marvin
 */
public class SignerFactory {

    public static Map<String, Signer> signers = new HashMap();

    static {
        for (String algo : new String[]{"SHA1withDSA",
            "SHA1withRSA",
            "SHA256withRSA"}) {
            signers.put(algo, new Signer() {
                @Override
                public String getName() {
                    return algo;
                }
            });
        }
    }
}
