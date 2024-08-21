package signature;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.util.Base64;

import static jdk.javadoc.internal.doclint.DocLint.SEPARATOR;
import static signature.Signer.DigitalSignature.generateFileSignature;

public class Signer {

    public static boolean verifyMessageSignature(String message, String algorithm, PublicKey publicKey, byte[] digitalSignature) throws
            NoSuchAlgorithmException,
            InvalidKeyException,
            SignatureException {
        Signature signature = Signature.getInstance(algorithm);
        signature.initVerify(publicKey);
        signature.update(message.getBytes());

        return signature.verify(digitalSignature);
    }

    public static byte[] signMessage(String message, String algorithm, PrivateKey privateKey) throws
            NoSuchAlgorithmException,
            InvalidKeyException,
            SignatureException {
        Signature signature = Signature.getInstance(algorithm);
        signature.initSign(privateKey);
        signature.update(message.getBytes());

        return signature.sign();
    }

    public static byte[] signFile(String filename, String algorithm, PrivateKey privateKey) throws
            IOException,
            NoSuchAlgorithmException,
            SignatureException,
            InvalidKeyException {
        File file = new File(filename);
        byte[] fileBytes = Files.readAllBytes(file.toPath());

        Signature signature = Signature.getInstance(algorithm);
        signature.initSign(privateKey);
        signature.update(fileBytes);

        return signature.sign();
    }

    public static boolean verifyFileSignature(String filename, String algorithm, PublicKey publicKey, byte[] digitalSignature)
            throws
            IOException,
            InvalidKeyException,
            NoSuchAlgorithmException,
            SignatureException {
        File file = new File(filename);
        byte[] fileBytes = Files.readAllBytes(file.toPath());

        Signature signature = Signature.getInstance(algorithm);
        signature.initVerify(publicKey);
        signature.update(fileBytes);

        return signature.verify(digitalSignature);
    }

    public class DigitalSignature {

        private static final String SEPARATOR = "||";  // Separador para evitar confusión entre nombre del archivo y firma digital

        // Método para generar la firma digital de un archivo
        static String generateFileSignature(String filePath) throws IOException, NoSuchAlgorithmException {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            try (InputStream fis = Files.newInputStream(Paths.get(filePath))) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    digest.update(buffer, 0, bytesRead);
                }
            }
            byte[] hash = digest.digest();
            return Base64.getEncoder().encodeToString(hash);
        }
    }

    public static void generateSignaturesFile(String directory, String signaturesFile) throws IOException, NoSuchAlgorithmException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(signaturesFile))) {
            Files.list(Paths.get(directory)).forEach(path -> {
                if (Files.isRegularFile(path)) {
                    try {
                        String signature = generateFileSignature(path.toString());
                        writer.write(path.getFileName().toString() + SEPARATOR + signature);
                        writer.newLine();
                    } catch (IOException | NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    // Método para verificar las firmas digitales
    public static boolean verifySignaturesFile(String directory, String signaturesFile) throws IOException, NoSuchAlgorithmException {
        try (BufferedReader reader = new BufferedReader(new FileReader(signaturesFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(SEPARATOR);
                String fileName = parts[0];
                String storedSignature = parts[1];
                String filePath = Paths.get(directory, fileName).toString();
                if (Files.isRegularFile(Paths.get(filePath))) {
                    String currentSignature = generateFileSignature(filePath);
                    if (!storedSignature.equals(currentSignature)) {
                        System.out.println("ERROR: La firma de " + fileName + " no coincide.");
                        return false;
                    }
                }
            }
            System.out.println("Todas las firmas coinciden.");
            return true;
        }
    }

}

