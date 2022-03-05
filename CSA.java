/*
# PROGRAMMER: QUANG CAO - nhat.quang.cao@student.rmit.edu.au
# DATE CREATED: 22/02/2022
# REVISED DATE: 08/03/2022
# TITLE: Ancestral Colorings of Perfect Binary Trees With Applications in Private Retrieval of Merkle Proofs
# AUTHORS: Quang Cao, Rinaldo Gagiano, Duy Huynh, Xun Yi, Son Hoang Dau, Phuc Lu Le, Quang-Hung Luu, Emanuele Viterbo, Yu-Chih Huang, Jingge Zhu, Mohammad M. Jalalzai, and Chen Feng
# PURPOSE: In this paper, we develop a divide-and-conquer algorithm called Color-Splitting Algorithm (CSA)
#          that takes ℎ as input and generates a balanced ancestral coloring for 𝑇 (ℎ) in time linear
#          in the number of tree nodes (excluding the root) 2^(ℎ+1) − 2. In fact, this algorithm can generate
#          not only a balanced ancestral coloring but also any ancestral coloring (color classes having heterogeneous sizes) feasible for 𝑇 (ℎ).
#          It is worth noting that this flexibility of our algorithm establishes the existence of optimal combinatorial patterned-batch codes
#          corresponding to the case of servers with heterogeneous storage capacities as well.
#          At the high level, the algorithm colors two sibling nodes at a time and proceeds recursively down to the two subtrees
#          and repeats the process while maintaining the Ancestral Property:
#          If a color is used for a node then it will not be used for the descendants of that node.
#          The algorithm can produce a balanced ancestral coloring for the tree 𝑇 (20) around 1 second.
*/

import java.util.*;
import java.util.stream.*;

public class CSA {

    public static void main(String[] args) {
        ColorSplittingAlgorithm CSA = new ColorSplittingAlgorithm();
        PerfectBinaryTree T;

        int h = 0; //h is the height of a tree
        String option; //A for Automatic Balanced Ancestral Coloring; B for Manual Feasible Color Configuration
        String vc;

        while (true) {
            System.out.print("Removing all the garbage collector .....");
            System.gc(); //runs the garbage collector
            Scanner input = new Scanner(System.in);

            printMenu();

            //Enter input option until option is valid from MENU (A, B, or Q)
            boolean check;
            do {
                System.out.print("Enter your option (A or B or Q): ");
                option = input.nextLine();
                check = validOptionMenu(option);
            } while (!check);

            //Quit
            if (option.charAt(0) == 'Q' || option.charAt(0) == 'q') break;

            //A color configuration c with key = color, value = node ID
            HashMap<String, Integer> c = new HashMap<>();

            //A. * Automatic Balanced Ancestral Coloring *
            if (option.charAt(0) == 'A' || option.charAt(0) == 'a') {
                h = h(); //Enter the height of a tree "h" from keyboard
                c = CSA.balancedColorConfiguration(h);
                c = CSA.intSorted(c);

                //B. * Manual Feasible Color Configuration
            } else if (option.charAt(0) == 'B' || option.charAt(0) == 'b') {
                do {
                    input = new Scanner(System.in);
                    System.out.println("\n" + "|| Following the Definition 2.3 (Feasible Color Configuration)");
                    System.out.println("|| + C1: Colors 1, 2, . . . , ℓ can be used to color all nodes in Layers 1, 2, . . . , ℓ of the perfect binary tree 𝑇 (ℎ)");
                    System.out.println("|| + C2: The total size of 𝑐 is equal to the number of nodes in 𝑇 (ℎ)" + "\n");
                    System.out.print("Enter your Feasible Color Configuration c = [c1,...,ch] (e.g, 4 4 6): ");
                    // Enter option from keyboard
                    vc = input.nextLine();
                    String[] ci = vc.split(" ");
                    h = ci.length;
                    for (int i = 0; i < h; i++) {
                        c.put("C" + (i + 1), Integer.parseInt(ci[i]));
                    }
                    c = CSA.intSorted(c);
                } while (!CSA.isFeasible(h, c));

                //Invalid input option
            } else System.out.println("Warning! Invalid input option. The option has to be a letter A, B, or Q");

            //The color configuration c = [c1,...,ch]
            ArrayList<Integer> vectorC = new ArrayList<>(c.values());

            //Start Color-Splitting Algorithm
            long startTime = System.nanoTime();

            //Color-Splitting Algorithm
            T = CSA.ColorSplitting(h, c);

            //End Color-Splitting Algorithm
            long endTime = System.nanoTime();
            long timeElapsed = (endTime - startTime) / 1000000;

            //Print input including the heigh of a tree and a color configuration c = [c1,...,ch]
            System.out.println("*** Input:");
            System.out.println("    Tree heigh: " + T.getHeight());
            System.out.println("    c = " + vectorC);

            //Print output
            System.out.println("*** Output:");
            System.out.println("    Execution time in milliseconds: " + timeElapsed);
            printOutput(T);
        }
    }

    public static void printMenu() {
        String[] menu = {"\n" + "********* MENU *********",
                "A. * Automatic Balanced Ancestral Coloring *",
                "B. * Manual Feasible Color Configuration *",
                "Q. * Quit *"};
        for (String s : menu) {
            System.out.println(s);
        }
    }

    //Print all the sets of balanced colors
    public static void printOutput(PerfectBinaryTree T) {
        //Group roots R based on their colors.
        HashMap<Long, String> groupRoots = T.getcoloringNodes();
        Map<String, List<Long>> groupColoringNodes = groupRoots.keySet().stream().collect(Collectors.groupingBy(groupRoots :: get));

        //Print all the sets of balanced colors
        groupColoringNodes.forEach((color, r) -> {
            System.out.print("    * Color " + color + " = ");
            r.forEach(x -> System.out.print(x + " "));
            System.out.println();
        });
    }

    //Enter the height of a tree "h" from keyboard. h has to greater than or equal 2.
    public static Integer h() {
        Scanner input = new Scanner(System.in);
        int height; //the height of the tree

        do {
            System.out.print("Enter the height of a tree \"h\" = ");
            // The height "h" input have to an integer number
            while (!input.hasNextInt()) {
                System.out.print("NOTE! You have to put integer of the height \"h\" = ");
                input.next();
            }
            height = input.nextInt();
            if (height < 2) {
                System.out.println(" NOTE! Enter an Integer >= 2");
            }
        } while (height < 2); //The height "h" input has to greater than or equal 2

        return height;
    }

    //Check valid Option in Menu
    public static boolean validOptionMenu(String opt) {
        if (opt.length() != 1) {
            System.out.println("You have to put a character option in MENU (A, B, or Q)");
            return false;
        } else if (opt.charAt(0) != 'A' && opt.charAt(0) != 'a'
                && opt.charAt(0) != 'B' && opt.charAt(0) != 'b'
                && opt.charAt(0) != 'Q' && opt.charAt(0) != 'q') {
            System.out.println("You have to put a character option in MENU (A, B, or Q)");
            return false;
        } else {
            return true;
        }
    }
}


//Color-Splitting Algorithm
class ColorSplittingAlgorithm {

    PerfectBinaryTree T;
    ArrayList<String> sc = new ArrayList<>();
    LinkedHashMap<String, Integer> sorted = new LinkedHashMap<>();
    HashMap<HashMap<String, Integer>, HashMap<String, Integer>> C = new HashMap<>();

    //Given a feasible configuration 𝑐 = [𝑐1, . . . , 𝑐ℎ], the algorithm finds a 𝑐-coloring of 𝑇 (ℎ)
    public PerfectBinaryTree ColorSplitting(int h, HashMap<String, Integer> c) {
        long R = 1; //the root of 𝑇 (ℎ) is 1
        T = new PerfectBinaryTree(h);
        ColorSplittingRecursive(R, h, c);
        return T;
    }

    //𝑅 is the root node of the current subtree 𝑇 (ℎ) of height ℎ
    //Either 𝑅 needs no color (𝑅 = 1) or 𝑅 has already been colored in the previous call
    //𝑐 = [𝑐1, . . . , 𝑐ℎ] is a feasible color configuration, which implies that 2 ≤ 𝑐1 ≤ 𝑐2 ≤ · · · ≤ 𝑐ℎ
    //This procedure colors the two children of 𝑅 and create feasible color configurations for its
    //left and right subtrees
    public boolean ColorSplittingRecursive(long R, int h, HashMap<String, Integer> c) {
        long A, B;
        HashMap<String, Integer> a = new HashMap<>(h - 1);
        HashMap<String, Integer> b = new HashMap<>(h - 1);

        sc.clear();
        C.clear();
        sc.addAll(c.keySet());

        //if (!isFeasible(h, c)) return false;

        if (h > 0) {
            A = 2 * R; //left child of 𝑅
            B = 2 * R + 1; //right child of 𝑅
            //Assign Color 1 to both 𝐴 and 𝐵;
            if (c.get(sc.get(0)) == 2) {
                T.setColorNode(A, sc.get(0));
                T.setColorNode(B, sc.get(0));
            }
            //Assign Color 1 to 𝐴 and Color 2 to 𝐵;
            else {
                T.setColorNode(A, sc.get(0));
                T.setColorNode(B, sc.get(1));
            }

            if (h > 1) {
                C = FeasibleSplit(h, c);
                C.forEach((n, m) -> {
                    a.putAll(n);
                    b.putAll(m);
                });

                ColorSplittingRecursive(A, h - 1, intSorted(a));
                a.clear();
                sorted.clear();

                ColorSplittingRecursive(B, h - 1, intSorted(b));
                b.clear();
                sorted.clear();
            }
        }
        return true;
    }

    /* This algorithm splits a ℎ-feasible configuration into two (ℎ − 1)-feasible ones, which will be used for coloring the subtrees;
    only works when ℎ ≥ 2. Note that the splitting rule (see FeasibleSplit(ℎ, 𝑐)) ensures that
    if Color 𝑖 is used for a node then it will no longer be used in the subtree rooted at that node,
    hence guaranteeing the Ancestral Property.*/
    // key = a; value = b which are two (ℎ − 1)-feasible of two subtrees following "Procedure FeasibleSplit(ℎ, 𝑐)"
    public HashMap<HashMap<String, Integer>, HashMap<String, Integer>> FeasibleSplit(int h, HashMap<String, Integer> c) {
        HashMap<String, Integer> a = new HashMap<>(h - 1);
        HashMap<String, Integer> b = new HashMap<>(h - 1);

        sc.clear();
        C.clear();
        sc.addAll(c.keySet());

        if (h == 2) {
            int i = 1; //Position 2
            int aValue, bValue;

            if (c.get(sc.get(0)) == 2) {
                aValue = c.get(sc.get(i)) / 2;
                bValue = c.get(sc.get(i)) / 2;
                a.put(sc.get(1), aValue);
                b.put(sc.get(1), bValue);
            } else {
                aValue = c.get(sc.get(i)) - 1;
                bValue = c.get(sc.get(0)) - 1;
                a.put(sc.get(1), aValue);
                b.put(sc.get(0), bValue);
            }

            C.put(intSorted(a), intSorted(b));

            return C;

        } else if (h > 2) {
            //Case 1: 𝑐1 = 2
            if (c.get(sc.get(0)) == 2) {
                int i = 1; //Position 2
                int aValue = (int) Math.floor(c.get(sc.get(i)) / 2.0);
                int bValue = (int) Math.ceil(c.get(sc.get(i)) / 2.0);
                a.put(sc.get(i), aValue);
                b.put(sc.get(i), bValue);
                int Sa = aValue; //Left sum
                int Sb = bValue; //Right sum
                i++;
                //change for original algorithm
                while (i < h) {
                    if (Sa < Sb) {
                        aValue = (int) Math.ceil(c.get(sc.get(i)) / 2.0);
                        bValue = (int) Math.floor(c.get(sc.get(i)) / 2.0);
                    } else {
                        aValue = (int) Math.floor(c.get(sc.get(i)) / 2.0);
                        bValue = (int) Math.ceil(c.get(sc.get(i)) / 2.0);
                    }
                    a.put(sc.get(i), aValue);
                    b.put(sc.get(i), bValue);
                    Sa += aValue;
                    Sb += bValue;
                    i++;
                }
            }
            //Case 2: 𝑐1 > 2; note that 𝑐1 ≥ 2 due to the feasibility of 𝑐;
            else {
                int i = 1; //Position 2
                int aValue = c.get(sc.get(i)) - 1;
                int bValue = c.get(sc.get(0)) - 1;
                a.put(sc.get(1), aValue);
                b.put(sc.get(0), bValue);
                int Sa = aValue; //Left sum
                int Sb = bValue; //Right sum
                i++;
                aValue = (int) Math.ceil((c.get(sc.get(i)) + c.get(sc.get(0)) - c.get(sc.get(i - 1))) / 2.0);
                bValue = c.get(sc.get(i - 1)) - c.get(sc.get(0)) + (int) Math.floor((c.get(sc.get(i)) + c.get(sc.get(0)) - c.get(sc.get(i - 1))) / 2.0);
                a.put(sc.get(i), aValue);
                b.put(sc.get(i), bValue);
                Sa += aValue;
                Sb += bValue;
                i++;
                while (i < h) {
                    if (Sa < Sb) {
                        aValue = (int) Math.ceil(c.get(sc.get(i)) / 2.0);
                        bValue = (int) Math.floor(c.get(sc.get(i)) / 2.0);
                    } else {
                        aValue = (int) Math.floor(c.get(sc.get(i)) / 2.0);
                        bValue = (int) Math.ceil(c.get(sc.get(i)) / 2.0);
                    }
                    a.put(sc.get(i), aValue);
                    b.put(sc.get(i), bValue);

                    Sa += aValue;
                    Sb += bValue;
                    i++;
                }
            }
            C.put(intSorted(a), intSorted(b));
            a.clear();
            b.clear();

            return C;
        } else {
            System.out.println("Warming! h should be greater than or equal 2"); //𝑐 ≥ 2 due to the feasibility of 𝑐;
            return null;
        }
    }

    //Corollary 2.12 (Balanced Color Configuration) return 𝑐 = [𝑐1, 𝑐2, . . . , 𝑐ℎ]
    public HashMap<String, Integer> balancedColorConfiguration(int h) {
        HashMap<String, Integer> c = new HashMap<>(h);
        int u = (int) (Math.pow(2, h + 1) - 2) % h;
        int cValue;

        for (int i = 0; i < h; i++) {
            double a = (Math.pow(2, h + 1) - 2) / h;
            if (i < (h - u)) {
                cValue = (int) Math.floor(a);
                c.put("C" + (i + 1), cValue);
            } else {
                cValue = (int) Math.ceil(a);
                c.put("C" + (i + 1), cValue);
            }
        }
        return c;
    }

    //Definition 2.3 (Feasible Color Configuration). A color configuration 𝑐fi of dimension ℎ is called
    //"ℎ-feasible" if after being sorted in a non-decreasing order (so that 𝑐1 ≤ 𝑐2 ≤ · · · ≤ 𝑐ℎ), it satisfies
    //the following two conditions: (C1) and (C2)
    public boolean isFeasible(int h, HashMap<String, Integer> c) {
        for (int m = h; m > 0; m--) {
            int sum;
            sum = getTotalColorSize(m, c);
            //check (C2)
            if (m == h && sum != (int) (Math.pow(2, m + 1) - 2)) {
                System.out.println("\n" + " ***** WARNING! *****");
                System.out.println("The color configuration is NOT feasible");
                System.out.println("Conflict with C2: The total size of 𝑐 is equal to the number of nodes in 𝑇 (ℎ)");
                return false;
            }
            //check (C1)
            if (sum < (int) Math.pow(2, m + 1) - 2) {
                System.out.println("\n" + " ***** WARNING! *****");
                System.out.println("The color configuration is NOT feasible");
                System.out.println("Conflict with C1: Colors 1, 2, . . . , ℓ can be used to color all nodes in Layers 1, 2, . . . , ℓ of the perfect binary tree 𝑇 (ℎ)");
                return false;
            }
        }
        return true;
    }

    //Sum all color values
    public int getTotalColorSize(int m, HashMap<String, Integer> c) {
        if (0 < m && m <= c.size()) {
            int sum = 0;
            ArrayList<Integer> sc = new ArrayList<>(c.values());
            for (int i = 0; i < m; i++) {
                sum += sc.get(i);
            }
            return sum;
        } else {
            System.out.println("Warming! m should be positive and less than or equal h");
            return 0;
        }
    }

    //Sorts 𝑐𝑖 in a non-decreasing order
    public HashMap<String, Integer> intSorted(HashMap<String, Integer> m) {
        sorted = m.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry :: getKey, Map.Entry :: getValue, (e1, e2) -> e1, LinkedHashMap :: new));
        return sorted;
    }
}


/* An example of a perfect binary tree with h = 2
                       1
                    /    \
                  2       3
                /  \    /  \
               4   5   6    7
*/
class PerfectBinaryTree {
    private final int height;
    //each node will contain key = R; value = color
    private final HashMap<Long, String> coloringNodes;

    public PerfectBinaryTree(int height) {
        this.height = height;
        coloringNodes = new HashMap<>((int) (Math.pow(2, (height + 1)) - 2));
    }

    public void setColorNode(long root, String value) {
        coloringNodes.put(root, value);
    }

    public HashMap<Long, String> getcoloringNodes() {
        return coloringNodes;
    }

    public int getHeight() {
        return height;
    }
}
