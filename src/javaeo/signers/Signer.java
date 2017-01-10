package javaeo.signers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javaeo.FXMLDocumentController;
import javaeo.utils.KeyReader;

/**
 *
 * @author Marvin
 */
public abstract class Signer implements ISigner {

    @Override
    public boolean sign(String keyPath, String filePath, String signer) {
        Signature signerInstance;
        try {
            signerInstance = Signature.getInstance(this.getName());
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        PrivateKey privKey = KeyReader.readPrivate(keyPath);

        BufferedInputStream bufferIn;
        try {
            signerInstance.initSign(privKey);
            File file = new File(filePath);
            bufferIn = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bufferIn.read(buffer)) >= 0) {
                signerInstance.update(buffer, 0, len);
            }
            bufferIn.close();
            byte[] signature = signerInstance.sign();

            String fileName = "";
            String extension = "";
            int i = file.getName().lastIndexOf('.');
            if (i > 0) {
                fileName = file.getName().substring(0, i);
                extension = file.getName().substring(i);
            }

            DataOutputStream output = new DataOutputStream(new FileOutputStream(fileName + "(Signed by " + signer + ")" + extension));
            System.out.println("length: " + signature.length);
            output.write((int) signature.length);

            System.out.println("pos: " + output.size());

            output.write(signature);
            System.out.println("pos: " + output.size());
            System.out.println(signature[0]);
            bufferIn = new BufferedInputStream(new FileInputStream(file));
            while ((len = bufferIn.read(buffer)) >= 0) {
                output.write(buffer, 0, len);
            }
            output.close();
            return true;
        } catch (FileNotFoundException | SignatureException | InvalidKeyException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Signer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean restore(String keyPath, String filePath) {
        PublicKey key = KeyReader.readPublic(keyPath);
        try {
            DataInputStream bufferIn = new DataInputStream(new BufferedInputStream(new FileInputStream(filePath)));

            int slen = bufferIn.read();
            byte[] signature = new byte[slen];
            bufferIn.read(signature, 0, signature.length);
            System.out.println(signature[0]);

            Signature signerInstance;
            try {
                signerInstance = Signature.getInstance(this.getName());
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }

            DataOutputStream bufferOut = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("output")));

            signerInstance.initVerify(key);

            byte[] buffer = new byte[1024];
            int len;
            while ((len = bufferIn.read(buffer)) >= 0) {
                bufferOut.write(buffer, 0, len);
                signerInstance.update(buffer, 0, len);
            }
            bufferOut.close();
            bufferIn.close();
            return signerInstance.verify(signature);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Signer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | InvalidKeyException | SignatureException ex) {
            Logger.getLogger(Signer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}
