package javaeo.signers;

/**
 *
 * @author Marvin
 */
public interface ISigner {

    public String getName();

    public boolean sign(String keyPath, String filePath, String signer);

    public boolean restore(String keyPath, String filePath);
}
