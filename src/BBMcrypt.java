import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;


public class BBMcrypt {
    /*
    Check the given string out and pad it if needed
    This method just adds zeroes to last block if needed
    Then dividing into block will be much easier
     */
    public static String padBlockTexts (String text){
        int n = text.length();
        int remaining = n % 96;
        String zeroes="";
        if (remaining > 0){

            zeroes = Stream.generate(() -> "0").limit(96-remaining).collect(joining());

        }
        return text + zeroes;

    }

    /*
    Read the given file using FileInputStream
    Assign whole file contents to String var
    @return value is content of the file
     */
    private static String readFile(String arg) throws IOException {


        File file = new File(arg);
        FileInputStream inputStream = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        inputStream.read(data);
        inputStream.close();

        return new String(data, StandardCharsets.UTF_8);



    }
    /*
    Write given string into the given file
     */
    private static void writeResultToFile(String filename, String result) throws IOException {
        FileWriter fileWriter = new FileWriter(filename);
        fileWriter.write(result);
        fileWriter.close();

    }
    /*
    Convert given base64 string to binary
     */
    private static String convertBase64KeyToBinary(String key){
        byte[] decodedString = Base64.getDecoder().decode(new String(key).getBytes(StandardCharsets.UTF_8));
        return new String(decodedString);
    }

    /*
    Progress of the program arguments, executes the program
     */
    private static void executeProgram(String[] args) throws IOException {
        String inputFileName = "";
        String keyFileName = "";
        String outputFileName = "";
        String mode = "";
        String action = ""; //enc or dec

        action = args[0];

        for (int i=1;i<args.length-1;i++){
            String elem = args[i];

            if(elem.equals("-K")){
                keyFileName = args[++i];

            }

            else if(elem.equals("-I")){
                inputFileName = args[++i];

            }

            else if(elem.equals("-O")){
                outputFileName = args[++i];

            }

            else if(elem.equals("-M")){
                mode = args[++i];

            }
        }

        String inputText = readFile(inputFileName);
        String masterKey = readFile(keyFileName);
        masterKey = convertBase64KeyToBinary(masterKey);
        String result = "";

        if(action.equals("enc")){

            switch (mode){
                case "ECB":
                    result = ECB.ECBEncryption(inputText,masterKey);
                    break;

                case "CBC":
                    result = CBC.CBCEncryption(inputText,masterKey);
                    break;
                case "OFB":
                    result = OFB.OFBEncryption(inputText,masterKey);
                    break;
            }



        }
        else if(action.equals("dec")){
            switch (mode){
                case "ECB":
                    result = ECB.ECBDecryption(inputText,masterKey);
                    break;

                case "CBC":
                    result = CBC.CBCDecryption(inputText,masterKey);
                    break;
                case "OFB":
                    result = OFB.OFBDecryption(inputText,masterKey);
                    break;
            }
        }

        writeResultToFile(outputFileName,result);


    }


    public static void main(String[] args){


        if(args.length < 9){
            System.out.println("Arguments are not enough!!!");
            System.exit(1);
        }
        try {
            executeProgram(args);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
