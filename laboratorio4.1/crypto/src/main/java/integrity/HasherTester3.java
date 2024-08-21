package integrity;

import java.io.BufferedReader;
import java.io.FileReader;

public class HasherTester3 {
	public static void main(String[] args) {
      
        	
            //Hasher.generateIntegrityCheckerFile("src/binarios", "sha256sum.txt");
            
            //BufferedReader br = new BufferedReader(new FileReader("sha256sum.txt"));
            //String line;
            //while ((line = br.readLine()) != null) {
             // System.out.println(line);
            //
            try {
				Hasher.checkIntegrityFile("CarpetaRecibida", "integrity_check.txt");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
          
    }
	
	

}
