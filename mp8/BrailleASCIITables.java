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
            System.out.println("Initializing asciiToBrailleTree..."); // Add this line
            asciiToBrailleTree = buildBitTree("ASCIIToBraille.txt", 8); // Add this line
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
            int asciiValue = (int) letter;
            String binaryRepresentation = Integer.toBinaryString(asciiValue);
    
            // Pad with leading zeros if needed to match the expected bit length (8 in this case)
            while (binaryRepresentation.length() < 8) {
                binaryRepresentation = "0" + binaryRepresentation;
            }
    
            // Ensure the binary representation is exactly 8 characters long
            if (binaryRepresentation.length() != 8) {
                throw new IllegalArgumentException("Invalid binary representation length");
            }
    
            System.out.println("Binary representation: " + binaryRepresentation);
            return asciiToBrailleTree.get(binaryRepresentation);
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
                        tree.set(brailleBits, value); // Adjust the order
                    } catch (Exception e) {
                        System.err.println("Error setting value for bits " + brailleBits + ": " + e.getMessage());
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
        String brailleBits = "101100";
        String asciiChar = toASCII(brailleBits);
        System.out.println("Braille to ASCII: " + brailleBits + " -> " + asciiChar);

        char unicodeChar = toUnicode(brailleBits);
        System.out.println("Braille to Unicode: " + brailleBits + " -> " + unicodeChar);

        char inputChar = 'W';
        String brailleString = toBraille(inputChar);
        System.out.println("ASCII to Braille: " + inputChar + " -> " + brailleString);
    }
}
