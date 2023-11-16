import java.io.*;

class BitTreeNode {
    String val;
    BitTreeNode left;
    BitTreeNode right;

    BitTreeNode() {
        this.val = "";
        this.left = null;
        this.right = null;
    }
}

class BitTreeLeaf extends BitTreeNode {
    BitTreeLeaf(String val) {
        this.val = val;
        this.left = null;
        this.right = null;
    }
}

public class BitTree {
    private BitTreeNode root;
    private int bitLength;

    public BitTree(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("Bit length must be >= 1");
        }
        this.bitLength = n;
        this.root = new BitTreeNode(); // Initialize an empty root node.
    }

    public void set(String bits, String value) throws Exception {
        if (bits.length() != bitLength || !isValidBitString(bits)) {
            throw new Exception("Invalid bits length or format");
        }
    
        BitTreeNode curr = root;
        for (int i = 0; i < bits.length() - 1; i++) { // Adjust loop length
            if (bits.charAt(i) == '0') {
                if (curr.left == null) {
                    curr.left = new BitTreeNode();
                }
                curr = curr.left;
            } else if (bits.charAt(i) == '1') {
                if (curr.right == null) {
                    curr.right = new BitTreeNode();
                }
                curr = curr.right;
            }
        }
        if (bits.charAt(bits.length() - 1) == '0') {
            curr.left = new BitTreeLeaf(value);  // Create leaf for the last bit
        } else {
            curr.right = new BitTreeLeaf(value); // Create leaf for the last bit
        }
    }    

    public String get(String bits) throws Exception {
        if (bits == null || bits.length() != bitLength || !isValidBitString(bits)) {
            throw new Exception("Invalid path length or format");
        }

        BitTreeNode curr = root;
        for (int i = 0; i < bits.length(); i++) {
            if (bits.charAt(i) == '0') {
                if (curr.left == null) {
                    throw new Exception("Invalid path");
                }
                curr = curr.left;
            } else if (bits.charAt(i) == '1') {
                if (curr.right == null) {
                    throw new Exception("Invalid path");
                }
                curr = curr.right;
            }
        }
        return (curr instanceof BitTreeLeaf) ? ((BitTreeLeaf) curr).val : null;
    }

    public void dump(PrintWriter pen) {
        dumpHelper(root, "", pen);
    }

    private void dumpHelper(BitTreeNode node, String path, PrintWriter pen) {
        if (node != null) {
            if (node instanceof BitTreeLeaf) {
                pen.println(path + "," + ((BitTreeLeaf) node).val);
            }
            dumpHelper(node.left, path + "0", pen);
            dumpHelper(node.right, path + "1", pen);
        }
    }

    public void load(InputStream source) {
        if (source == null) {
            System.err.println("InputStream is null. Cannot load data.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(source))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                String[] parts = line.split(",", 2); // Limit the split to 2 parts
                if (parts.length == 2) {
                    String bits = parts[0];
                    String value = parts[1];
                    try {
                        set(bits, value);
                    } catch (Exception e) {
                        System.err.println("Error setting value for bits " + bits +
                                " at line " + lineNumber + ": " + e.getMessage());
                    }
                } else {
                    System.err.println("Invalid line format at line " + lineNumber + ": " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading from InputStream: " + e.getMessage());
        }
    }

    private boolean isValidBitString(String bits) {
        return bits.matches("[01]+");
    }

    public static void main(String[] args) {
        try {
            BitTree tree = new BitTree(6);
            tree.set("101100", "M");
            tree.set("101110", "N");
            tree.set("100000", "A");
            tree.set("110000", "B");

            // Dump the tree before testing get method
            PrintWriter penBefore = new PrintWriter(System.out);
            tree.dump(penBefore);
            penBefore.flush();

            // Test the get method
            PrintWriter testResultsPen = new PrintWriter(System.out);

            String value1 = tree.get("101100");
            String value2 = tree.get("101110");
            String value3 = tree.get("100000");

            testResultsPen.println("Value for '101100': " + value1);
            testResultsPen.println("Value for '101110': " + value2);
            testResultsPen.println("Value for '100000': " + value3);

            testResultsPen.flush(); // Flush the PrintWriter to ensure output is visible

            // Dump the tree after testing get method
            PrintWriter penAfter = new PrintWriter(System.out);
            tree.dump(penAfter);
            penAfter.flush();

            // Continue with the rest of the main method...
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
