public class BrailleASCII {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java BrailleASCII <targetCharset> <sourceText>");
            System.exit(1);
        }

        String targetCharset = args[0].toLowerCase();
        String sourceText = args[1];

        try {
            String result;
            switch (targetCharset) {
                case "braille":
                    result = toBraille(sourceText);
                    break;
                case "ascii":
                    result = toASCII(sourceText);
                    break;
                case "unicode":
                    result = toUnicode(sourceText);
                    break;
                default:
                    System.out.println("Invalid target character set. Choose 'braille', 'ascii', or 'unicode'.");
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
            String braille = BrailleASCIITables.toBraille(c);
            brailleBuilder.append(braille);
        }
        return brailleBuilder.toString();
    }

    private static String toASCII(String sourceText) throws Exception {
        StringBuilder asciiBuilder = new StringBuilder();
        for (char c : sourceText.toCharArray()) {
            String binaryRepresentation = BrailleASCIITables.toBraille(c);
            String asciiChar = BrailleASCIITables.toASCII(binaryRepresentation);
            asciiBuilder.append(asciiChar);
        }
        return asciiBuilder.toString();
    }

    private static String toUnicode(String sourceText) throws Exception {
        StringBuilder unicodeBuilder = new StringBuilder();
        for (char c : sourceText.toCharArray()) {
            String binaryRepresentation = BrailleASCIITables.toBraille(c);
            char unicodeChar = BrailleASCIITables.toUnicode(binaryRepresentation);
            unicodeBuilder.append(unicodeChar);
        }
        return unicodeBuilder.toString();
    }
}
