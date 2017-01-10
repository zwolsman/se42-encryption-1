package javaeo.generators;

import java.security.KeyPair;

/**
 *
 * @author Marvin
 */
public interface IGenerator {

    public int[] keySizes();

    public String getName();

    public KeyPair generate(int keySize) throws Exception;
}
