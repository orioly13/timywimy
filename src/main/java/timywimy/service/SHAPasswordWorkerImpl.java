package timywimy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

@Component
public class SHAPasswordWorkerImpl implements SHAPasswordWorker {
    //https://howtodoinjava.com/security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
    //https://stackoverflow.com/questions/19348501/pbkdf2withhmacsha512-vs-pbkdf2withhmacsha1
    private final int iterations;
    private final int hashLength;
    private final byte[] pepper;
    private final SecretKeyFactory skf;
    private final SecureRandom sr;

    @Autowired
    public SHAPasswordWorkerImpl(@Value("${api.hash.iterations}") int iterations,
                                 @Value("${api.hash.hashLength}") int hashLength,
                                 @Value("${api.hash.pepper}") byte[] pepper) {
        Assert.isTrue(iterations > 0, "iterations should be positive");
        Assert.isTrue(hashLength > 0, "hashLength should be positive");
        this.hashLength = hashLength;
        this.iterations = iterations;
        this.pepper = pepper;
        try {
            this.skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            this.sr = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException();
        }
    }

    public String generatePasswordHash(String password) {
        try {
            byte[] salt = getSalt();
            byte[] saltPepperHash = generateSaltPepperPasswordHash(password, salt, iterations, hashLength);
            return iterations + ":" + toHex(salt) + ":" + toHex(saltPepperHash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean validatePassword(String password, String saltPepperHash) {
        try {
            String[] parts = saltPepperHash.split(":");
            int iterations = Integer.parseInt(parts[0]);
            byte[] salt = fromHex(parts[1]);
            byte[] spHash = fromHex(parts[2]);

            byte[] newSPHash = generateSaltPepperPasswordHash(password, salt, iterations, spHash.length);

            int diff = spHash.length ^ newSPHash.length;
            if (diff == 0) {
                for (int i = 0; i < spHash.length; i++) {
                    diff |= spHash[i] ^ newSPHash[i];
                }
            }
            return diff == 0;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] generateHash(char[] chars, byte[] salt, int iterations, int length) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, length * 8);
        return skf.generateSecret(spec).getEncoded();
    }

    private byte[] generateSaltPepperPasswordHash(String password, byte[] salt, int iterations, int hashLength)
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        char[] chars = password.toCharArray();
        //salt hash
        byte[] saltHash = generateHash(chars, salt, iterations, hashLength);
        //peppering saltHash
        return generateHash(toHex(saltHash).toCharArray(), pepper, iterations, hashLength);
    }
    //random salt
    private byte[] getSalt() throws NoSuchAlgorithmException {
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    private String toHex(byte[] array) throws NoSuchAlgorithmException {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    private byte[] fromHex(String hex) throws NoSuchAlgorithmException {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }


}
