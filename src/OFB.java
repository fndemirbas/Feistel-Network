import java.util.ArrayList;

//Encryption mode: Output Feedback Mode
public class OFB {

    private static final String initialVector = "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111";
    //Initialization vector

    private static ArrayList<String> subKeys;

    /*
    OFB Encryption mode using the feistel encryption
    Generate Xi (like subkeys) by encrypting previous Xi-1
    After generating the keys, XOR them with Pi at each step
     */
    public static String OFBEncryption(String plaintext, String masterKey){
        ArrayList<String> cipherBlocks = new ArrayList<>();
        ArrayList<String> textBlocks = new ArrayList<>();

        int i=0;

        StringBuilder ciphertext = new StringBuilder();
        plaintext = BBMcrypt.padBlockTexts(plaintext);


        while (i < plaintext.length()){
            textBlocks.add(plaintext.substring(i,i+96));
            i += 96;
        }
        generateSubKeysOfOFB(masterKey,textBlocks.size());

        for (int j = 0;j<textBlocks.size();j++){
            ciphertext.append(Scramble.xorStrings(subKeys.get(j), textBlocks.get(j)));
        }

        return ciphertext.toString();

    }
    /*
    Generates subkeys(Xi) of OFB and adds them into the key arraylist
     */
    private static void generateSubKeysOfOFB(String key, int lim){
        subKeys = new ArrayList<>();
        String X1 = FeistelNetwork.feistelEncryption(initialVector,key);
        subKeys.add(X1);

        for (int i=1;i<lim;i++){
            subKeys.add(FeistelNetwork.feistelEncryption(subKeys.get(i-1),key));
        }


    }

    /*
    OFB Decryption using Feistel encryption again. This is different from other encryption modes
    Other enc modes uses feistel decryption to decrypt messages but OFB uses encryption
    To get the Pi XOR the Xi and Ci(cipherblock)
     */
    public static String OFBDecryption(String ciphertext, String masterKey){
        ArrayList<String> cipherBlocks = new ArrayList<>();

        int i=0;
        ciphertext = BBMcrypt.padBlockTexts(ciphertext);


        while (i < ciphertext.length()){
            cipherBlocks.add(ciphertext.substring(i,i+96));
            i += 96;
        }

        StringBuilder plaintext = new StringBuilder();

        generateSubKeysOfOFB(masterKey,cipherBlocks.size());

        for (int j=0;j<cipherBlocks.size();j++){
            plaintext.append(Scramble.xorStrings(cipherBlocks.get(j), subKeys.get(j)));
        }
        return plaintext.toString();
    }



    
}
