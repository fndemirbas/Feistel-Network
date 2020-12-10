

public class FeistelNetwork {


    /*
    If given string(plaintext)'s length is less than the block size
    then the plaintext will be padded with all zeros to obtain 96 bit block
     */
    public static String padPlaintext(String plaintext){
        for (int i=plaintext.length();i<96;i++){
            plaintext += "0";
        }
        return plaintext;
    }

    /*
    Feistel encryption method using scramble function
    Do encryption according to rules
    Ld = Rd-1 , Rd = Ld-1 ^ f(d-1)(Rd-1,Kd-1)

     */
    public static String feistelEncryption(String plaintext, String masterKey){
        String ciphertext="";
        if(plaintext.length() < 96){
            plaintext = padPlaintext(plaintext);
        }

        KeyGenerator.subkeyGenerator(masterKey);
        String left = plaintext.substring(0,48);
        String right = plaintext.substring(48);
        for (int i=0;i<10;i++){
            Scramble scramble = new Scramble();
            String temp_left = left;
            left = right;
            right= Scramble.xorStrings(temp_left,scramble.scrambleFunction(right,KeyGenerator.permutedSubkeys[i]));
        }

        ciphertext = left+right;
        return  ciphertext;



    }

    /*
    Feistel decryption method
    Rd-1 = Ld, Ld-1 = Rd ^ f(d-1)(Ld,Kd-1)
     */
    public static String feistelDecryption(String ciphertext, String masterKey){
        String plaintext="";
        if(ciphertext.length() < 96){
            ciphertext = padPlaintext(ciphertext);
        }
        Scramble scramble = new Scramble();
        KeyGenerator.subkeyGenerator(masterKey);
        String left = ciphertext.substring(0,48);
        String right = ciphertext.substring(48);

        for (int i=10;i>0;i--){

            String temp_left = left;
            String temp_right = right;
            left = Scramble.xorStrings(right,scramble.scrambleFunction(temp_left,KeyGenerator.permutedSubkeys[i-1]));
            right = temp_left;


        }

        return left+right;


    }

}
