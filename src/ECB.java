
import java.util.ArrayList;

//Encryption mode : Electronic Code Book
public class ECB {

    /*
    Encryption method of ECB
    Broke the given message into block of 96-bits
    Encrypt given message blocks with feistel encryption
    @param plaintext is string that has to be encrypted
    @param masterKey is the given key
    @return is encrypted text
     */
    public static String ECBEncryption(String plaintext, String masterKey){
        ArrayList<String> cipherBlocks = new ArrayList<>();
        ArrayList<String> textBlocks = new ArrayList<>();
        int i=0;
        plaintext = BBMcrypt.padBlockTexts(plaintext);

        while (i < plaintext.length()){
            textBlocks.add(plaintext.substring(i,i+96));
            i += 96;
        }


        for (int j=0;j<textBlocks.size();j++){
            cipherBlocks.add(FeistelNetwork.feistelEncryption(textBlocks.get(j),masterKey));
        }

        return String.join("",cipherBlocks);
    }

    /*
    ECB Decryption method with Feistel Decryption
     */

    public static String ECBDecryption(String ciphertext, String masterKey){
        ArrayList<String> cipherBlocks = new ArrayList<>();
        ArrayList<String> textBlocks = new ArrayList<>();
        int i=0;
        String text = "";
        ciphertext = BBMcrypt.padBlockTexts(ciphertext);

        while (i < ciphertext.length()){
            cipherBlocks.add(ciphertext.substring(i,i+96));
            i += 96;
        }


        for (int j=0;j<cipherBlocks.size();j++){
            textBlocks.add(FeistelNetwork.feistelDecryption(cipherBlocks.get(j),masterKey));
        }

        return String.join("",textBlocks);
    }



}
