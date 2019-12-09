package rsa;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RSATest {

    KeyPair keyPair;

    @Before
    public void setUp() throws Exception {
        keyPair = KeyPair.generate();
    }
}
