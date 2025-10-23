import java.util.Scanner;

public class AVL {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AVLTree tree = new AVLTree();

        if (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                String[] numbers = line.split(",");
                for (String number : numbers) {
                    System.out.println("==================================");
                    tree.printPrettyStepByStep(tree.root, "", true);
                    tree.insert(Integer.parseInt(number.trim()));
                }


            }
        }
        tree.postOrder(tree.root);
        scanner.close();
        System.out.println("=== Pretty (sideways) ===");
        tree.printPretty();
    }

    // Node class
    static class Node {
        int key;
        Node left;
        Node right;
        int height;

        Node(int key) {
            this.key = key;
            this.height = 1;
        }
    }

    // AVL Tree class
    static class AVLTree {
        Node root;  // root of the AVL tree

        // ===== Pretty Print (sideways) =====
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
            int bf = height(node.left) - height(node.right);
            System.out.println(prefix + (isTail ? "└── " : "┌── ") + node.key);
            if (node.left != null) {
                printPrettyStepByStep(node.left, prefix + (isTail ? "    " : "│   "), true);
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

        private int updateHeight(Node n) {
            return Math.max(height(n.left), height(n.right)) + 1;
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

        // insert a node into the AVL tree
        Node insert(Node node, int key) {
            if (node == null) return new Node(key);
            // even number
            if (key % 2 == 0) {
                if (node.right == null) {
                    if (key < node.key) {
                        node.left = insert(node.left, key);
                    } else if (key > node.key) {
                        node.right = insert(node.right, key);
                    }
                } else {
                    node.right = insert(node.right, key);
                }
            }
            // odd number
            else {
                // insert as usual
                if (key < node.key) {
                    node.left = insert(node.left, key);
                } else if (key > node.key) {
                    node.right = insert(node.right, key);
                }
            }
            // update height
            node.height = 1 + Math.max(height(node.left), height(node.right));
            // balance the tree
            int diff = getDiffHeight(node);
            if (diff > 1) {
                if (getDiffHeight(node.left) < 0) {
                    node.left = leftRotate(node.left);
                }
                return rightRotate(node);
            } else if (diff < -1) {
                if (getDiffHeight(node.right) > 0) {
                    node.right = rightRotate(node.right);
                }
                return leftRotate(node);
            }

            return node;
        }

        // insert
        void insert(int key) {
            root = insert(root, key);
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
