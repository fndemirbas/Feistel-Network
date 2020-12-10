import java.util.ArrayList;
import java.util.Arrays;

public class KeyGenerator {
    public static String[] permutedSubkeys = new String[10];
    private static ArrayList<int[]> shiftedKeys = new ArrayList<>();

    /*
    Make left circular shift by 1 bit
    @param key is the given subkey in each round, array of integers(bit by bit)
    @return shifted integer array
     */
    private static int[] leftShift(int[] key){
        int msb = key[0];
        //TODO: control the bit size
        int[] shifted = new int[key.length];
        shifted = key;
        for (int i=0;i<key.length-1;i++){
            shifted[i] = shifted[i+1];
        }
        shifted[key.length-1] = msb;
        return shifted;
    }
    /*
    convert given string to integer array
    @param key is the key which will be converted
    it is especially used before the left shift
     */
    private static int[] convertStringToIntArray(String key) {
        int size = key.length();
        int [] arr = new int [size];
        for(int i=0; i<size; i++) {
            arr[i] = Integer.parseInt(String.valueOf(key.charAt(i)));
        }
        return arr;

    }

    /*
    choose bits from the given shifted subkey
    @param shiftedBits is int array that holds shifted subkey
    @param round is the round number
    @return permutedkey which includes chosen bits according to the given rule below
    if round is divisible by 2 (even numbered) choose even digits,
    choose odd ones otherwise
    subkey bit indices are from left to right

     */
    private static String permutedKey(int[] shiftedBits , int round ) {

        String permutedKey = "";

        if (round%2==0) {

            for (int i = 0; i < shiftedBits.length; i+=2) {
                permutedKey+= String.valueOf(shiftedBits[i]);
            }

        }

        else {
            for (int i = 1; i < shiftedBits.length; i+=2) {
                permutedKey+= String.valueOf(shiftedBits[i]);
            }
        }

        return permutedKey;

    }
    /*
    Create shifted keys of each round recursively
    Get shifted keys from the leftshift method and add into the arraylist that holds each rounds' shifted keys
    Pass the shifted key to the same method again to use in the next round
    @param key is the key from previous round
    @param round is the round number
     */
    private static void createShiftedKeysOfEachRound(int[] key, int round){
        if (round==10)
            return;
        shiftedKeys.add(key);
        //TODO: control size of the array which means number of bits of the key
        int[] shiftedKey = new int[key.length];
        System.arraycopy(leftShift(key),0,shiftedKey,0,key.length);
        createShiftedKeysOfEachRound(shiftedKey,round+1);

    }
    /*
    Wrapper function that calls all other functions
    and fills the permutedChoice array which holds permuted keys of each round
     */
    public static void subkeyGenerator(String masterKey){
        createShiftedKeysOfEachRound(convertStringToIntArray(masterKey),0);
        for (int i=0;i<10;i++){
            String permutedChoice = permutedKey(shiftedKeys.get(i),i);
            permutedSubkeys[i] = permutedChoice;
        }

    }

}
