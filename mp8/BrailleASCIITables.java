import java.io.*;

public class BrailleASCIITables {
    private static BitTree asciiToBrailleTree;
    private static BitTree brailleToAsciiTree;
    private static BitTree brailleToUnicodeTree;

    static {
        try {
            System.out.println("Initializing brailleToAsciiTree...");
            brailleToAsciiTree = buildBitTree("brailleToASCII.txt", 6);
            System.out.println("Initializing brailleToUnicodeTree...");
            brailleToUnicodeTree = buildBitTree("brailleToUnicode.txt", 6);
            System.out.println("Initialization successful!");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error initializing trees: " + e.getMessage());
        }
    }    

    public static String toASCII(String bits) throws Exception {
        try {
            int expectedBitLength = 6; // Adjust this value based on your actual bit length

            if (bits.length() != expectedBitLength || !isValidBitString(bits)) {
                throw new IllegalArgumentException("Invalid braille bits");
            }

            System.out.println("Braille bits: " + bits);
            return brailleToAsciiTree.get(bits);
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            return "";
        }
    }
    
    

    public static char toUnicode(String bits) throws Exception {
        try {
            String unicodeValue = brailleToUnicodeTree.get(bits);
            if (unicodeValue != null && !unicodeValue.isEmpty()) {
                return (char) Integer.parseInt(unicodeValue, 16);
            } else {
                throw new IllegalArgumentException("Invalid braille bits");
            }
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            return '\0';
        }
    }

    public static String toBraille(char letter) throws Exception {
        try {
            return asciiToBrailleTree.get(String.valueOf(letter));
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static BitTree buildBitTree(String fileName, int bitLength) {
        BitTree tree = new BitTree(bitLength);
    
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String brailleBits = parts[0];
                    String value = parts[1];
                    try {
                        tree.set(value, brailleBits);
                    } catch (Exception e) {
                        System.err.println("Error setting value for bits " + value + ": " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error reading file: " + e.getMessage());
        }
    
        return tree;
    }    

    private static boolean isValidBitString(String bits) {
        return bits.matches("[01]+");
    }

    public static void main(String[] args) throws Exception {
        // Example usage
        String brailleBits = "100100";
        String asciiChar = toASCII(brailleBits);
        System.out.println("Braille to ASCII: " + brailleBits + " -> " + asciiChar);

        char unicodeChar = toUnicode(brailleBits);
        System.out.println("Braille to Unicode: " + brailleBits + " -> " + unicodeChar);

        char inputChar = 'A';
        String brailleString = toBraille(inputChar);
        System.out.println("ASCII to Braille: " + inputChar + " -> " + brailleString);
    }
}
