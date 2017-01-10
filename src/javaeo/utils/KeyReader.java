package javaeo.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marvin
 */
public class KeyReader {

    public static PrivateKey readPrivate(String fileName) {
        try {
            byte[] raw = Files.readAllBytes(new File(fileName).toPath());
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(raw);

            return KeyFactory.getInstance("RSA").generatePrivate(spec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(KeyReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static PublicKey readPublic(String fileName) {
        try {
            byte[] raw = Files.readAllBytes(new File(fileName).toPath());
            X509EncodedKeySpec spec = new X509EncodedKeySpec(raw);

            return KeyFactory.getInstance("RSA").generatePublic(spec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(KeyReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
