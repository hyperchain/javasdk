package cn.hyperchain.sdk.crypto.jce;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;

public final class ECKeyPairGenerator {

    public static final String ALGORITHM = "EC";
    public static final String CURVE_NAME = "secp256k1";
    public static final String CURVE_NAMER1 = "secp256r1";

    private static final String algorithmAssertionMsg =
            "Assumed JRE supports EC key pair generation";

    private static final String keySpecAssertionMsg =
            "Assumed correct key spec statically";

    private static final ECGenParameterSpec SECP256K1_CURVE
            = new ECGenParameterSpec(CURVE_NAME);

    private static final ECGenParameterSpec SECP256R1_CURVE
            = new ECGenParameterSpec(CURVE_NAMER1);

    private ECKeyPairGenerator() {
    }

    private static class Holder {
        private static final KeyPairGenerator INSTANCE;

        static {
            try {
                INSTANCE = KeyPairGenerator.getInstance(ALGORITHM);
                INSTANCE.initialize(SECP256K1_CURVE);
            } catch (NoSuchAlgorithmException ex) {
                throw new AssertionError(algorithmAssertionMsg);
            } catch (InvalidAlgorithmParameterException ex) {
                throw new AssertionError(keySpecAssertionMsg);
            }
        }
    }

    public static KeyPair generateKeyPair() {
        return Holder.INSTANCE.generateKeyPair();
    }

    public static KeyPairGenerator getInstance(final String provider, final SecureRandom random) throws NoSuchProviderException {
        try {
            final KeyPairGenerator gen = KeyPairGenerator.getInstance(ALGORITHM, provider);
            gen.initialize(SECP256K1_CURVE, random);
            return gen;
        } catch (NoSuchAlgorithmException ex) {
            throw new AssertionError(algorithmAssertionMsg);
        } catch (InvalidAlgorithmParameterException ex) {
            throw new AssertionError(keySpecAssertionMsg);
        }
    }

    public static KeyPairGenerator getInstance(final Provider provider, final SecureRandom random) {
        try {
            final KeyPairGenerator gen = KeyPairGenerator.getInstance(ALGORITHM, provider);
            gen.initialize(SECP256K1_CURVE, random);
            return gen;
        } catch (NoSuchAlgorithmException ex) {
            throw new AssertionError(algorithmAssertionMsg);
        } catch (InvalidAlgorithmParameterException ex) {
            throw new AssertionError(keySpecAssertionMsg);
        }
    }

    public static KeyPairGenerator getInstanceR1(final Provider provider, final SecureRandom random) {
        try {
            final KeyPairGenerator gen = KeyPairGenerator.getInstance(ALGORITHM, provider);
            gen.initialize(SECP256R1_CURVE, random);
            return gen;
        } catch (NoSuchAlgorithmException ex) {
            throw new AssertionError(algorithmAssertionMsg);
        } catch (InvalidAlgorithmParameterException ex) {
            throw new AssertionError(keySpecAssertionMsg);
        }
    }
}
