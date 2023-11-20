/**
 * 
 * A class containing the main method using 
 * CLAs in which instructions are called
 * 
 * @author Jonathan Wang
 * November 2023
 * 
 */

public class BrailleASCII {
    public static void main(String[] args) {
      if (args.length != 2) { // If incorrect number of args
        System.out.println("Usage: java BrailleASCII <targetCharset> <sourceText>");
        System.exit(1);
      }
  
      String targetCharset = args[0].toLowerCase();
      String sourceText = args[1];
  
      try {
        String result;
        switch (targetCharset) { // Switch case for input instructions
          case "braille":
            result = toBraille(sourceText);
            break;
          case "ascii":
            result = toBrailleASCII(sourceText);
            break;
          case "unicode":
            result = toUnicode(sourceText);
            break;
          default:
            System.out.println("Incorrect instruction. Please input 'braille', 'ascii', or 'unicode'.");
            return;
        }
  
        System.out.println(result);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  
    private static String toBraille(String sourceText) throws Exception {
      StringBuilder brailleBuilder = new StringBuilder();
      for (char c : sourceText.toCharArray()) {
        String braille = BrailleASCIITables.toBraille(c); // Call toBraille()
        brailleBuilder.append(braille);
      }
      return brailleBuilder.toString();
    } // toBraille()
  
    private static String toBrailleASCII(String sourceText) {
      StringBuilder asciiBuilder = new StringBuilder();
      for (int i = 0; i < sourceText.length(); i += 6) { // Loop through every 6 chars
        String brailleChunk = sourceText.substring(i, Math.min(i + 6, sourceText.length()));
        try {
          String asciiChar = BrailleASCIITables.toASCII(brailleChunk).toLowerCase(); // Call toASCII()
          asciiBuilder.append(asciiChar);
        } catch (Exception e) {
          System.err.println("Error converting Braille chunk: " + brailleChunk + " - " + e.getMessage());
        }
      }
      return asciiBuilder.toString();
    } // toBrailleASCII()
  
    private static String toUnicode(String sourceText) throws Exception {
      StringBuilder unicodeBuilder = new StringBuilder();
      for (char c : sourceText.toCharArray()) {
        String binaryRepresentation = BrailleASCIITables.toBraille(c); // Call toBraille()
        String unicodeChar = String.valueOf(BrailleASCIITables.toUnicode(binaryRepresentation));
        unicodeBuilder.append(unicodeChar); // Append each unicode bit extracted using String.valueOf
      }
      return unicodeBuilder.toString();
    } // toUnicode()
  }  