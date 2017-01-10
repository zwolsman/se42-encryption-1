package javaeo.generators;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 *
 * @author Marvin
 */
public abstract class Generator implements IGenerator {

    private static final String[] FILE_NAMES = {"public.key", "private.key"};
    private static SecureRandom secureRandom;

    static {
        try {
            secureRandom = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Invalid secure random thingy");
        }
    }

    @Override
    public KeyPair generate(int keySize) throws Exception {
        if (!isValidKeysize(keySize)) {
            throw new InvalidParameterException("Invalid key size, supported");
        }

        try {
            KeyPairGenerator instance = KeyPairGenerator.getInstance(this.getName());
            instance.initialize(keySize);
            return instance.genKeyPair();
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }

    public boolean save(int keySize, String path) {
        KeyPair kp;
        try {
            kp = generate(keySize);
        } catch (Exception ex) {
            return false;
        }

        if (kp == null) {
            return false;
        }

        Key[] keys = new Key[]{kp.getPublic(), kp.getPrivate()};
        for (int i = 0; i < keys.length; i++) {
            try (FileOutputStream fos = new FileOutputStream(FILE_NAMES[i])) {
                fos.write(keys[i].getEncoded());
            } catch (IOException ioe) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidKeysize(int size) {
        for (int keySize : this.keySizes()) {
            if (keySize == size) {
                return true;
            }
        }
        return false;
    }
}
