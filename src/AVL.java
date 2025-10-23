/*
输入
从标准输入（System.in）读取一个整数数组，例如：
9,6,2,1,4,25,16,13,37,27,17,34,10

构建 AVL 树

普通插入规则：像标准 AVL 树一样插入奇数。
特殊规则（偶数）：
如果插入值是偶数 → 优先往右子树插入；
如果右子树为空，则按标准规则插入；
如果右子树不为空，则只遍历右子树，直到找到合适位置。
插入完后需进行标准的 AVL 平衡调整。

输出

以 post-order（后序遍历） 打印结果。
 */


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

        System.out.println("\n=== Levels ===");
        tree.printLevels();

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

        // ===== Level-order per line =====
        void printLevels() {
            if (root == null) return;
            java.util.Queue<Node> q = new java.util.ArrayDeque<>();
            q.add(root);
            while (!q.isEmpty()) {
                int n = q.size();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < n; i++) {
                    Node x = q.poll();
                    int bf = height(x.left) - height(x.right);
                    sb.append(x.key).append("[h=").append(x.height).append(",bf=").append(bf).append("] ");
                    if (x.left != null) q.add(x.left);
                    if (x.right != null) q.add(x.right);
                }
                System.out.println(sb.toString().trim());
            }
        }

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
            y.height = Math.max(height(y.left), height(y.right)) + 1;
            x.height = Math.max(height(x.left), height(x.right)) + 1;
            return x;
        }

        // left rotate
        Node leftRotate(Node x) {
            Node y = x.right;
            Node T2 = y.left;
            y.left = x;
            x.right = T2;
            x.height = Math.max(height(x.left), height(x.right)) + 1;
            y.height = Math.max(height(y.left), height(y.right)) + 1;
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
                if (getDiffHeight(node.left)<0) {
                    node.left = leftRotate(node.left);
                }
                return rightRotate(node);
            } else if (diff < -1) {
                if (getDiffHeight(node.right)>0) {
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
