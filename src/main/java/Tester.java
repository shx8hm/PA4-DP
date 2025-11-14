import java.util.Scanner;

public class Tester {
    public static void main (String[] args) {
        // 1. Read-in input
        Scanner input = new Scanner(System.in);

        while (input.hasNext()) {
            String raw_input_str = input.next();

            // 2. Process (two arrays, one with VT=1,UVA=-1, other with UVA=1,VT=-1)
            int[] UVA_1_array = PA4.get_people_array_UVA_1(raw_input_str);
            int[] VT_1_array = PA4.get_people_array_VT_1(raw_input_str);

            // 3. Get two solutions
            PA4.Solution UVA_1_sol = PA4.solve_one_school(UVA_1_array, PA4.SchoolName.UVa);
            PA4.Solution VT_1_sol = PA4.solve_one_school(VT_1_array, PA4.SchoolName.VT);
            // 3a. Adjust indices for 1-based output
            UVA_1_sol.max_i += 1;
            UVA_1_sol.max_j += 1;
            VT_1_sol.max_i += 1;
            VT_1_sol.max_j += 1;

            // 4. Pick the optimal solution
            //  and display it
            if (UVA_1_sol.compareTo(VT_1_sol) > 0) { // UVA wins
                System.out.println("UVa");
                System.out.print(UVA_1_sol.max_i + " ");
                System.out.println(UVA_1_sol.max_j);
            } else if (UVA_1_sol.compareTo(VT_1_sol) < 0) { // VT wins
                System.out.println("VT");
                System.out.print(VT_1_sol.max_i + " ");
                System.out.println(VT_1_sol.max_j);
            } else { // tie- break the tie here manually
                // whichever one starts first
                if (UVA_1_sol.max_i < VT_1_sol.max_i) { // Tie, and UVA's subarray starts first
                    System.out.println("UVa");
                    System.out.print(UVA_1_sol.max_i + " ");
                    System.out.println(UVA_1_sol.max_j);
                } else if (UVA_1_sol.max_i > VT_1_sol.max_i) { //Tie, and VT's subarray starts first
                    System.out.println("VT");
                    System.out.print(VT_1_sol.max_i + " ");
                    System.out.println(VT_1_sol.max_j);
                }

                // if i-tie, whichever one ends first
                else { // two equal-sum subarrays start at same time
                    if (UVA_1_sol.max_j < VT_1_sol.max_j) { // Tie, and UVA's subarray ends first
                        System.out.println("UVa");
                        System.out.print(UVA_1_sol.max_i + " ");
                        System.out.println(UVA_1_sol.max_j);
                    } else if (UVA_1_sol.max_j > VT_1_sol.max_j) { //Tie, and VT's subarray ends first
                        System.out.println("VT");
                        System.out.print(VT_1_sol.max_i + " ");
                        System.out.println(VT_1_sol.max_j);
                    }
                    // exact, exact, exact tie not possible
                }
            }
        }
    }
}
