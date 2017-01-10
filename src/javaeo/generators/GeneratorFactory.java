package javaeo.generators;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Marvin
 */
public class GeneratorFactory {

    public static Map<String, Generator> generators = new HashMap();

    static {
        for (Generator generator : new Generator[]{
            new Generator() {
                @Override
                public int[] keySizes() {
                    return new int[]{1024, 2048};
                }

                @Override
                public String getName() {
                    return "RSA";
                }

            },
            new Generator() {
                @Override
                public int[] keySizes() {
                    return new int[]{1024};
                }

                @Override
                public String getName() {
                    return "DiffieHellman";
                }
            },
            new Generator() {
                @Override
                public int[] keySizes() {
                    return new int[]{1024};
                }

                @Override
                public String getName() {
                    return "DSA";
                }

            }}) {
            generators.put(generator.getName(), generator);
        }
    }
}
