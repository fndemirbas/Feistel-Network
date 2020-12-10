import java.util.ArrayList;
import java.util.Stack;
//Encryption mode: Cipher Block Chaining
public class CBC {

    private static final String initialVector = "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111";
    //Initialization vector

    /*
    CBC encryption method with the Feistel encryption
    Divide given plaintext into text blocks
    Then xor the initialization vector and P1 then encrypt the result
    Get other cipher blocks by xoring previous cipher block and making encryption on it. (like the chain)


     */
    public static String CBCEncryption(String plaintext, String masterKey){
        ArrayList<String> cipherBlocks = new ArrayList<>();
        ArrayList<String> textBlocks = new ArrayList<>();
        int i=0;
        plaintext = BBMcrypt.padBlockTexts(plaintext);

        while (i < plaintext.length()){
            textBlocks.add(plaintext.substring(i,i+96));
            i += 96;
        }
        //If textblocks array is empty then simply return, since there may be some errors
        if(textBlocks.isEmpty()){
            return "";
        }
        //First block
        String p1 = textBlocks.get(0);
        String xoredInput = Scramble.xorStrings(p1,initialVector);
        cipherBlocks.add(FeistelNetwork.feistelEncryption(xoredInput,masterKey));

        for (int j=1;j<textBlocks.size();j++){
            String pj = textBlocks.get(j);
            String xoredP = Scramble.xorStrings(pj,cipherBlocks.get(j-1));
            cipherBlocks.add(FeistelNetwork.feistelEncryption(xoredP,masterKey));
        }

        return String.join("",cipherBlocks);

    }

    /*
    CBC Decryption method using Feistel decryption
    The same as encryption, but to get the plaintext xor the ciphertext and decrypted text
     */
    public static String CBCDecryption(String ciphertext, String masterKey){
        ArrayList<String> cipherBlocks = new ArrayList<>();
        Stack<String> textBlocks = new Stack<String>();
        int i=0;
        ciphertext = BBMcrypt.padBlockTexts(ciphertext);

        while (i < ciphertext.length()){
            cipherBlocks.add(ciphertext.substring(i,i+96));
            i += 96;
        }
        StringBuilder plaintext = new StringBuilder();

        if(cipherBlocks.isEmpty()){
            return "";
        }
        String nonXORed = FeistelNetwork.feistelDecryption(cipherBlocks.get(0),masterKey);
        plaintext.append(Scramble.xorStrings(nonXORed,initialVector));
        for (int j=1;j<cipherBlocks.size();j++){
            String decrypted = FeistelNetwork.feistelDecryption(cipherBlocks.get(j),masterKey);
            plaintext.append(Scramble.xorStrings(decrypted,cipherBlocks.get(j-1)));
        }


        return plaintext.toString();

    }


}
