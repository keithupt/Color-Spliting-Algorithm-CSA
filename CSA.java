/*
# PROGRAMMER: QUANG CAO and SON HOANG DAU - nhat.quang.cao@student.rmit.edu.au
# DATE CREATED: 22/02/2022
# REVISED DATE: 24/03/2022
# ORIGINAL VERSION was made by RINALDO GAGIANO.
# This is a NEW EFFICIENT VERSION.
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

public class CSA {

    public static void main(String[] args) {

        ColorSplittingAlgorithm CSA = new ColorSplittingAlgorithm();
        BalancedSet[] balancedSets;

        //A color configuration c
        List<NumColor> c = new ArrayList<>();
        Scanner input = new Scanner(System.in);

        int h = 0; //h is the height of a tree
        String option; //A for Automatic Balanced Ancestral Coloring; B for Manual Feasible Color Configuration
        String vc;
        char[] color = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q'
                , 'R','S', 'T', 'U', 'V','W', 'X', 'Y', 'Z','0', '1', '2', '3', '4', '5', '6', '7','8', '9'};

        while (true) {
            System.out.print("Removing all the garbage collector .....");
            System.gc(); //runs the garbage collector

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

            //A. * Automatic Balanced Ancestral Coloring *
            if (option.charAt(0) == 'A' || option.charAt(0) == 'a') {
                h = h(); //Enter the height of a tree "h" from keyboard
                c = CSA.balancedColorConfiguration(h);
                Collections.sort(c);
            }
            //B. * Manual Feasible Color Configuration
            else if (option.charAt(0) == 'B' || option.charAt(0) == 'b') {
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
                        c.add(new NumColor(color[i], Integer.parseInt(ci[i])));
                    }
                    Collections.sort(c);
                } while (!CSA.isFeasible(h, c));

            }
            //Invalid input option
            else System.out.println("Warning! Invalid input option. The option has to be a letter A, B, or Q");

            //The color configuration c = [c1,...,ch]
            ArrayList<Integer> vectorC = new ArrayList<>(c.size());

            c.forEach(x -> vectorC.add(x.getSize()));

            //Start Color-Splitting Algorithm ************************** START *************************
            long startTime = System.nanoTime();

            //Color-Splitting Algorithm
            balancedSets = CSA.ColorSplitting(h, c);

            //End Color-Splitting Algorithm ***************************** END **************************
            long endTime = System.nanoTime();
            long timeElapsed = (endTime - startTime) / 1000000;

            //Print input including the height of a tree and a color configuration c = [c1,...,ch]
            System.out.println("*** Input:");
            System.out.println("    Tree heigh: " + h);
            System.out.println("    c = " + vectorC);

            //Print Output
            System.out.println("*** Output:");
            System.out.println("    Execution time in milliseconds: " + timeElapsed);
            Arrays.stream(balancedSets).forEach(s -> {
                System.out.print("    Color " + s.getColorSet() + ": ");
                Arrays.stream(s.getAddNodes()).forEach(r -> System.out.print(r + " "));
                System.out.println();
            });

            c.clear();
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

    BalancedSet[] balancedSets;
    HashMap<List<NumColor>, List<NumColor>> C = new HashMap<>();
    char[] color = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q'
            , 'R','S', 'T', 'U', 'V','W', 'X', 'Y', 'Z','0', '1', '2', '3', '4', '5', '6', '7','8', '9'};

    //Given a feasible configuration 𝑐 = [𝑐1, . . . , 𝑐ℎ], the algorithm finds a 𝑐-coloring of 𝑇 (ℎ)
    public BalancedSet[] ColorSplitting(int h, List<NumColor> c) {
        int R = 1; //the root of 𝑇 (ℎ) is 1
        balancedSets = new BalancedSet [h];
        for (int q = 0; q < h; q++){
            int size = c.get(q).getSize();
            balancedSets[q] = new BalancedSet(c.get(q).getColor(), size);
            //System.out.println(balancedSets[q].getColorSet() + " size " + balancedSets[q].getColorSetSize());
        }

        ColorSplittingRecursive(R, h, c);
        return balancedSets;
    }

    //𝑅 is the root node of the current subtree 𝑇 (ℎ) of height ℎ
    //Either 𝑅 needs no color (𝑅 = 1) or 𝑅 has already been colored in the previous call
    //𝑐 = [𝑐1, . . . , 𝑐ℎ] is a feasible color configuration, which implies that 2 ≤ 𝑐1 ≤ 𝑐2 ≤ · · · ≤ 𝑐ℎ
    //This procedure colors the two children of 𝑅 and create feasible color configurations for its
    //left and right subtrees
    public boolean ColorSplittingRecursive(int R, int h, List<NumColor> c) {
        int A, B;
        List<NumColor> a = new ArrayList<>(h - 1);
        List<NumColor> b = new ArrayList<>(h - 1);

        C.clear();

        //if (!isFeasible(h, c)) return false;

        if (h > 0) {
            A = 2 * R; //left child of 𝑅
            B = 2 * R + 1; //right child of 𝑅

            //Assign Color 1 to both 𝐴 and 𝐵;
            if (c.get(0).getSize() == 2) {
                for (BalancedSet balancedSet : balancedSets) {
                    if (balancedSet.getColorSet() == c.get(0).getColor()) {
                        balancedSet.addNode(A);
                        balancedSet.addNode(B);
                    }
                }
            }
            //Assign Color 1 to 𝐴 and Color 2 to 𝐵;
            else {
                for (BalancedSet balancedSet : balancedSets) {
                    if (balancedSet.getColorSet() == c.get(0).getColor()) {
                        balancedSet.addNode(A);
                    }
                    if (balancedSet.getColorSet() == c.get(1).getColor()) {
                        balancedSet.addNode(B);
                    }
                }
            }

            if (h > 1) {
                C = FeasibleSplit(h, c);
                C.forEach((n, m) -> {
                    a.addAll(n);
                    b.addAll(m);
                });

                Collections.sort(a);
                ColorSplittingRecursive(A, h - 1, a);

                Collections.sort(b);
                ColorSplittingRecursive(B, h - 1, b);
            }
        }
        return true;
    }

    /* This algorithm splits a ℎ-feasible configuration into two (ℎ − 1)-feasible ones, which will be used for coloring the subtrees;
    only works when ℎ ≥ 2. Note that the splitting rule (see FeasibleSplit(ℎ, 𝑐)) ensures that
    if Color 𝑖 is used for a node then it will no longer be used in the subtree rooted at that node,
    hence guaranteeing the Ancestral Property.*/
    // key = a; value = b which are two (ℎ − 1)-feasible of two subtrees following "Procedure FeasibleSplit(ℎ, 𝑐)"
    public HashMap<List<NumColor>, List<NumColor>> FeasibleSplit(int h, List<NumColor> c) {
        List<NumColor> a = new ArrayList<>(h - 1);
        List<NumColor> b = new ArrayList<>(h - 1);

        C.clear();

        if (h == 2) {
            int i = 1; //Position 2
            int aValue, bValue;

            if (c.get(0).getSize() == 2) {
                aValue = c.get(i).getSize() / 2;
                bValue = c.get(i).getSize() / 2;
                a.add(new NumColor(c.get(1).getColor(), aValue));
                b.add(new NumColor(c.get(1).getColor(), bValue));
            } else {
                aValue = c.get(i).getSize() - 1;
                bValue = c.get(0).getSize() - 1;
                a.add(new NumColor(c.get(1).getColor(), aValue));
                b.add(new NumColor(c.get(0).getColor(), bValue));
            }

            Collections.sort(a);
            Collections.sort(b);

            C.put(a, b);

            return C;

        } else if (h > 2) {
            //Case 1: 𝑐1 = 2
            if (c.get(0).getSize() == 2) {
                int i = 1; //Position 2
                int aValue = (int) Math.floor(c.get(i).getSize() / 2.0);
                int bValue = (int) Math.ceil(c.get(i).getSize() / 2.0);
                a.add(new NumColor(c.get(i).getColor(), aValue));
                b.add(new NumColor(c.get(i).getColor(), bValue));
                int Sa = aValue; //Left sum
                int Sb = bValue; //Right sum
                i++;
                //change for original algorithm
                while (i < h) {
                    if (Sa < Sb) {
                        aValue = (int) Math.ceil(c.get(i).getSize() / 2.0);
                        bValue = (int) Math.floor(c.get(i).getSize() / 2.0);
                    } else {
                        aValue = (int) Math.floor(c.get(i).getSize() / 2.0);
                        bValue = (int) Math.ceil(c.get(i).getSize() / 2.0);
                    }
                    a.add(new NumColor(c.get(i).getColor(), aValue));
                    b.add(new NumColor(c.get(i).getColor(), bValue));
                    Sa += aValue;
                    Sb += bValue;
                    i++;
                }
            }
            //Case 2: 𝑐1 > 2; note that 𝑐1 ≥ 2 due to the feasibility of 𝑐;
            else {
                int i = 1; //Position 2
                int aValue = c.get(i).getSize() - 1;
                int bValue = c.get(0).getSize() - 1;
                a.add(new NumColor(c.get(1).getColor(), aValue));
                b.add(new NumColor(c.get(0).getColor(), bValue));
                int Sa = aValue; //Left sum
                int Sb = bValue; //Right sum
                i++;
                aValue = (int) Math.ceil((c.get(i).getSize() + c.get(0).getSize() - c.get(i - 1).getSize()) / 2.0);
                bValue = c.get(i - 1).getSize() - c.get(0).getSize() + (int) Math.floor((c.get(i).getSize() + c.get(0).getSize() - c.get(i - 1).getSize()) / 2.0);
                a.add(new NumColor(c.get(i).getColor(), aValue));
                b.add(new NumColor(c.get(i).getColor(), bValue));
                Sa += aValue;
                Sb += bValue;
                i++;
                while (i < h) {
                    if (Sa < Sb) {
                        aValue = (int) Math.ceil(c.get(i).getSize() / 2.0);
                        bValue = (int) Math.floor(c.get(i).getSize() / 2.0);
                    } else {
                        aValue = (int) Math.floor(c.get(i).getSize() / 2.0);
                        bValue = (int) Math.ceil(c.get(i).getSize() / 2.0);
                    }
                    a.add(new NumColor(c.get(i).getColor(), aValue));
                    b.add(new NumColor(c.get(i).getColor(), bValue));

                    Sa += aValue;
                    Sb += bValue;
                    i++;
                }
            }
            Collections.sort(a);
            Collections.sort(b);
            C.put(a, b);

            return C;
        } else {
            System.out.println("Warming! h should be greater than or equal 2"); //𝑐 ≥ 2 due to the feasibility of 𝑐;
            return null;
        }
    }

    //Corollary 2.12 (Balanced Color Configuration) return 𝑐 = [𝑐1, 𝑐2, . . . , 𝑐ℎ]
    public List<NumColor> balancedColorConfiguration(int h) {
        List<NumColor> c = new ArrayList<>(h);

        int u = (int) (Math.pow(2, h + 1) - 2) % h;
        int cValue;

        for (int i = 0; i < h; i++) {
            double a = (Math.pow(2, h + 1) - 2) / h;
            if (i < (h - u)) {
                cValue = (int) Math.floor(a);
                c.add(new NumColor(color[i], cValue));
            } else {
                cValue = (int) Math.ceil(a);
                c.add(new NumColor(color[i], cValue));
            }
        }
        return c;
    }

    //Definition 2.3 (Feasible Color Configuration). A color configuration 𝑐fi of dimension ℎ is called
    //"ℎ-feasible" if after being sorted in a non-decreasing order (so that 𝑐1 ≤ 𝑐2 ≤ · · · ≤ 𝑐ℎ), it satisfies
    //the following two conditions: (C1) and (C2)
    public boolean isFeasible(int h, List<NumColor> c) {
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
    public int getTotalColorSize(int m, List<NumColor> c) {
        if (0 < m && m <= c.size()) {
            int sum = 0;
            for (int i = 0; i < m; i++) {
                sum += c.get(i).getSize();
            }
            return sum;
        } else {
            System.out.println("Warming! m should be positive and less than or equal h");
            return 0;
        }
    }
}

/* An example of a perfect binary tree with h = 2
                       1
                    /    \
                  2       3
                /  \    /  \
               4   5   6    7
*/
//Each set will contain all node with the same color
class BalancedSet {
    private final char colorSet;
    //each node will contain R and color
    private final int [] addNodes;
    private int count = 0;

    public BalancedSet(char colorSet, int size) {
        this.colorSet = colorSet;
        addNodes = new int [size];
    }

    public void addNode(int root) {
        //array start from 0, whereas node colored start from 2
        addNodes[count] = root;
        count++;
    }

    public int [] getAddNodes() {
        return addNodes;
    }

    public char getColorSet() {
        return colorSet;
    }

    @Override
    public String toString() {
        StringBuilder roots = new StringBuilder();
        for (int addNode : addNodes) {
            roots.append(addNode).append(" ");
        }
        return "Color " + this.colorSet + ": " + roots;
    }
}

class NumColor implements Comparable<NumColor> {
    private final char color;
    private final int size;

    public NumColor(char color, int size){
        this.color = color;
        this.size = size;
    }

    public char getColor(){ return color;}
    public int getSize(){ return size;}

    @Override
    public int compareTo(NumColor otherNumColor) {
        return Integer.compare(getSize(), otherNumColor.getSize());
    }
}
