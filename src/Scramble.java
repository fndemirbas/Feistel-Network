import java.util.Arrays;

public class Scramble {
    public static String reduced_block_48bits="";
    /*
    Lookup table of the S-boxes
     */
    private static final String[][] lookupTable={
            {
                    "0010", "1100", "0100", "0001", "0111", "1010", "1011", "0110", "1000", "0101", "0011", "1111", "1101", "0000", "1110", "1001"
            },
            {
                    "1110", "1011", "0010", "1100", "0100", "0111", "1101", "0001", "0101", "0000", "1111", "1010", "0011", "1001", "1000", "0110"
            },
            {
                    "0100", "0010", "0001", "1011", "1010", "1101", "0111", "1000", "1111", "1001", "1100", "0101", "0110", "0011", "0000", "1110"
            },
            {
                    "1011", "1000", "1100", "0111", "0001", "1110", "0010", "1101", "0110", "1111", "0000", "1001", "1010", "0100", "0101", "0011"
            }

    };

    /*
    Get outer and inner bits from input, then return the value of given row-col index from the lookup table
    @param input is given input with 6 bits
    @return value is output of S-box which has 4 bits.
     */
    private static String findValue(String input){
        String outer = input.substring(0,1).concat(input.substring(5));
        String inner = input.substring(1,5);
        int row_index = Integer.parseInt(outer,2);
        int col_index = Integer.parseInt(inner,2);
        return lookupTable[row_index][col_index];


    }
    /*
    Divide given input into 6-bit blocks according to given Scramble function definitions.
    P1 P2 P3 P4 P5 P6 P7 P8 P1+P2 P3+P4 P5+P6 P7+P8 --> this is the method
    @param block_of_48 is the 48-bit input
    @return result is string array that includes elements P1...P7+P8

     */
    private static String[] divideBits(String block_of_48){
        String[] result = new String[12];
        int index_counter=0;int j=0;
        for (int i=0;i<72;i+=6){
            if(i<=47){
                result[index_counter] = block_of_48.substring(i,i+6);
            }
            else{
                result[index_counter] = xorStrings(result[j++], result[j++]);
            }
            index_counter++;
        }
        return result;
    }

    /*
    Concatenate 4-bit block elements that are created from S-boxes
     */
    private static void concatenateReducedBlockElements(String subblock){
        reduced_block_48bits += subblock;
    }

    /*
    Wrapper function of all sub-operations
    Firstly xor the given Pi and Ki
    Divide into 6-bit blocks and then find values of them from the S-box lookup table
    Then concatenate them and do the permutation function

     */
    public String scrambleFunction(String input_block, String subKey){
        reduced_block_48bits="";
        String XORedInput = xorStrings(input_block,subKey);
        String[] blocks_of_6elements = divideBits(XORedInput);
        for (int i=0;i<12;i++){
            String block_str_4 = findValue(blocks_of_6elements[i]);
            concatenateReducedBlockElements(block_str_4);

        }
        return swapCharacters(reduced_block_48bits);
    }
    /*
    Do the XOR function on given two strings that hold binary ints(like String s1 = "01010")
    @param s1,s2 are given strings
    @return result is s1 ^ s2
     */
    public static String xorStrings(String s1, String s2){

        StringBuilder result = new StringBuilder();
        if(s1.length() != s2.length())
            return "";
        for (int i=0;i<s1.length();i++){
            if(s1.charAt(i) == s2.charAt(i)){
                result.append("0");
            }
            else{
                result.append("1");
            }
        }

        return result.toString();
    }

    public static String swapCharacters(String text) {
        char[] swapped = text.toCharArray();
        char temp;
        for (int j = 1; j < text.length(); j += 2) {
            temp = swapped[j-1];
            swapped[j-1] = swapped[j];
            swapped[j] = temp;
        }
        return String.valueOf(swapped);
    }


}
