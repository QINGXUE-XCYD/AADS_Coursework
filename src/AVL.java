import java.util.Arrays;
import java.util.Scanner;

public class AVL {

    public static void main(String[] args) {
        // Read input from stdin
        Scanner scanner = new Scanner(System.in);
        // Create an AVL tree
        AVLTree tree = new AVLTree();

        // Read input line by line and insert numbers into the AVL tree
        if (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                String[] numbers = line.split(",");
                for (String number : numbers) {
                    // View the tree step by step
                    System.out.println("==================================");
                    tree.printPrettyVertical(tree.root);
                    tree.insert(Integer.parseInt(number.trim()));
                }


            }
        }
        // Print the tree in post-order
        tree.postOrder(tree.root);
        scanner.close();
        // Print the tree
        System.out.println("=== Pretty (sideways) ===");
        tree.printPretty();
    }

    // Node class
    static class Node {
        int key;
        Node left;
        Node right;
        int height;

        // Constructor
        Node(int key) {
            this.key = key;
            this.height = 1;
        }
    }

    // AVL Tree class
    static class AVLTree {
        Node root;  // root of the AVL tree


        void printPretty() {
            if (root == null) return;
            printPretty(root, "", true);
        }

        private void printPretty(Node node, String prefix, boolean isTail) {
            if (node == null) return;
            if (node.right != null) {
                printPretty(node.right, prefix + (isTail ? "│   " : "    "), false);
            }
            int bf = height(node.left) - height(node.right);
            System.out.println(prefix + (isTail ? "└── " : "┌── ") + node.key + " (h=" + node.height + ", bf=" + bf + ")");
            if (node.left != null) {
                printPretty(node.left, prefix + (isTail ? "    " : "│   "), true);
            }
        }

        private void printPrettyStepByStep(Node node, String prefix, boolean isTail) {
            if (node == null) return;
            if (node.right != null) {
                printPrettyStepByStep(node.right, prefix + (isTail ? "│   " : "    "), false);
            }
            System.out.println(prefix + (isTail ? "└── " : "┌── ") + node.key);
            if (node.left != null) {
                printPrettyStepByStep(node.left, prefix + (isTail ? "    " : "│   "), true);
            }
        }
        private void printPrettyVertical(Node root) {
            if (root == null) return;

            int height = height(root);
            int width = (int) Math.pow(2, height + 1); // 预留足够宽度
            char[][] canvas = new char[height * 2][width];
            for (char[] row : canvas) Arrays.fill(row, ' ');

            fillCanvas(root, canvas, 0, width / 2, width / 4, height * 2);

            // 输出
            for (char[] row : canvas) {
                System.out.println(new String(row));
            }
        }

        private void fillCanvas(Node node, char[][] canvas, int row, int col, int gap, int totalRows) {
            if (node == null || row >= totalRows) return;
            String key = String.valueOf(node.key);
            int start = col - key.length() / 2;
            for (int i = 0; i < key.length(); i++) {
                canvas[row][start + i] = key.charAt(i);
            }

            // 左子树
            if (node.left != null) {
                canvas[row + 1][col - gap / 2] = '/';
                fillCanvas(node.left, canvas, row + 2, col - gap, gap / 2, totalRows);
            }
            // 右子树
            if (node.right != null) {
                canvas[row + 1][col + gap / 2] = '\\';
                fillCanvas(node.right, canvas, row + 2, col + gap, gap / 2, totalRows);
            }
        }


        // get the height of a node
        int height(Node n) {
            if (n == null)
                return 0;
            return n.height;
        }

        // get the difference in height between left and right subtrees
        int getDiffHeight(Node n) {
            if (n == null)
                return 0;
            return height(n.left) - height(n.right);
        }

        // right rotate
        Node rightRotate(Node y) {
            Node x = y.left;
            Node T2 = x.right;
            x.right = y;
            y.left = T2;
            y.height = updateHeight(y);
            x.height = updateHeight(x);
            return x;
        }

        // left rotate
        Node leftRotate(Node x) {
            Node y = x.right;
            Node T2 = y.left;
            y.left = x;
            x.right = T2;
            x.height = updateHeight(x);
            y.height = updateHeight(y);
            return y;
        }

        // update the height of a node
        private int updateHeight(Node n) {
            return Math.max(height(n.left), height(n.right)) + 1;
        }

        // balance the tree
        private Node balance(Node n) {
            if (n == null) {
                return null;
            }
            // update height
            n.height = updateHeight(n);
            // get the difference in height between left and right subtrees
            int diff = getDiffHeight(n);
            // LL case
            if (diff > 1 && getDiffHeight(n.left) >= 0) {
                return rightRotate(n);
            }
            // LR case
            if (diff > 1 && getDiffHeight(n.left) < 0) {
                n.left = leftRotate(n.left);
                return rightRotate(n);
            }
            // RR case
            if (diff < -1 && getDiffHeight(n.right) <= 0) {
                return leftRotate(n);
            }
            // RL case
            if (diff < -1 && getDiffHeight(n.right) > 0) {
                n.right = rightRotate(n.right);
                return leftRotate(n);
            }
            return n;
        }

        // usual standard insert
        private Node usualInsert(Node node, int key) {
            if (node == null) {
                return new Node(key);
            }
            if (key < node.key) {
                node.left = usualInsert(node.left, key);
            } else if (key > node.key) {
                node.right = usualInsert(node.right, key);
            } else {
                return node;    // same key
            }
            return balance(node);
        }

        // special insert for even values
        private Node specialInsert(Node node, int key) {
            if (node == null) {
                return new Node(key);
            }
            // even number goes to the right subtree
            if (node.right == null) {
                // if right subtree is null, insert as usual
                node.right = new Node(key);
            } else {
                // if right subtree is not null, insert to the right subtree
                node.right = specialInsert(node.right, key);
            }
            return balance(node);
        }

        // insert a node into the AVL tree
        void insert(int key) {
            if (key % 2 == 0) {
                // even number
                root = specialInsert(root, key);
            } else {
                // odd number
                root = usualInsert(root, key);
            }
        }

    // post-order traversal
    void postOrder(Node node) {
        if (node != null) {
            postOrder(node.left);
            postOrder(node.right);
            System.out.println(node.key);
        }
    }
}
}
