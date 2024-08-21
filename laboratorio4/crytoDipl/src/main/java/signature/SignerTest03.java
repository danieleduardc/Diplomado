package signature;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static signature.Signer.generateSignaturesFile;
import static signature.Signer.verifySignaturesFile;

public class SignerTest03 {

    public static void main(String[] args) {
        String testDirectory = "binary";
        String signaturesFile = "signatures.txt";

        try {
            generateSignaturesFile(testDirectory, signaturesFile);

            verifySignaturesFile(testDirectory, signaturesFile);
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
