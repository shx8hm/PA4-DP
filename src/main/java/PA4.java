import java.util.Scanner;

public class PA4 {

    static enum SchoolName {
        VT,
        UVa
    }

    // Solution class holds [max_i, max_j], max_sum, school_name
    static class Solution implements Comparable<Solution>{
        int max_i;
        int max_j;
        int max_sum;
        SchoolName school_name;

        public Solution(int max_i, int max_j, int max_sum, SchoolName school_name) {
            this.max_i = max_i;
            this.max_j = max_j;
            this.max_sum = max_sum;
            this.school_name = school_name;
        }

        @Override
        public int compareTo(Solution other) {
            return Integer.compare(this.max_sum, other.max_sum);
        }
    }

    public static int[] get_people_array_VT_1(String input_str) {
        int [] P = new int[input_str.length()];

        for (int i = 0; i < P.length; i++) {
            // VT ('m') -> 1
            // UVA ('B') -> -1
            P[i] = (input_str.charAt(i) == 'm') ? (1) : (-1);
        }

        return P;
    }

    public static int[] get_people_array_UVA_1(String input_str) {
        int [] P = new int[input_str.length()];

        for (int i = 0; i < P.length; i++) {
            // UVA ('B') -> 1
            // VT ('m') -> -1
            P[i] = (input_str.charAt(i) == 'B') ? (1) : (-1);
        }

        return P;
    }

    public static Solution solve_one_school(int [] P, SchoolName school_name) {
        // 0. length of array
        int n = P.length;


        // 1. initialize memoization array
        int[][] sol = new int[n][n];

        // 1a. Initialize base cases bottom-up
        int i = 0, j = 1;
        int max_i = -1, max_j = -1; // indices
        int max_sum = Integer.MIN_VALUE;

        // Initialize base cases bottom-up
        for (i = 0; i < n; i += 1) {
            sol[i][i] = P[i]; // sol(i, i) means 'the subarray with just person i (P[i])'

            // CORNER CASE- check for max on diagonal (just one person P[i])
            if (sol[i][i] == max_sum) {
                // if this is a tie that starts earlier (or ends earlier if start is tie)
                boolean starts_earlier_i = i < max_i;
                boolean is_start_tie = i == max_i;
                boolean ends_earlier_j = i < max_j;
                if ( (starts_earlier_i) || (is_start_tie && ends_earlier_j) ) {
                    max_i = i;
                    max_j = i;
                }
            }

            // NEW MAX CHECK
            //  UPDATE the max
            else if (sol[i][i] > max_sum) {
                max_sum = sol[i][i];
                max_i = i;
                max_j = i;
            }
        }


        // 2. Run DP
        int rc_offset = 1;
        while (rc_offset < n) {
            // rc_offset-th diagonal from the base case
            i = 0;
            j = i + rc_offset;

            // fill in rc_offset-th diagonal from the base case
            while (j < n /*&& i < n*/) {
                // REMOVED BOUNDS CHECK (not necessary)
                /*
                // bounds check
                int left_total;
                if (i + 1 < n) {
                    left_total = P[i] + sol[i+1][j];
                }
                else {
                    left_total = 0;
                }

                int right_total;
                if (j-1 >= 0) {
                    right_total = sol[i][j-1] + P[j];
                }
                else {
                    right_total = 0;
                }*/

                sol[i][j] = Integer.max(
                        P[i] + sol[i+1][j],
                        sol[i][j-1] + P[j]
                );

                // EQUALITY CHECK
                //  CHOOSE the max with the LOWEST X
                //  if X-tie, with LOWEST Y
                if (sol[i][j] == max_sum) {
                    // if this is a tie that starts earlier (or ends earlier if start is tie)
                    boolean starts_earlier_i = i < max_i;
                    boolean is_start_tie = i == max_i;
                    boolean ends_earlier_j = j < max_j;
                    if ( (starts_earlier_i) || (is_start_tie && ends_earlier_j) ) {
                        max_i = i;
                        max_j = j;
                    }
                }

                // NEW MAX CHECK
                //  UPDATE the max
                else if (sol[i][j] > max_sum) {
                    max_sum = sol[i][j];
                    max_i = i;
                    max_j = j;
                }

                // next (top-right) element in this diagnoal
                i += 1;
                j += 1;
            } // while (diagnoal)

            // i = 0; // moved to top of loop
            rc_offset += 1;
        }

        // Return solution
        return new Solution(max_i, max_j, max_sum, school_name);
    }

    public static void main (String[] args) {
        // 1. Read-in input
        Scanner input = new Scanner(System.in);
        String raw_input_str = input.next();

        // 2. Process (two arrays, one with VT=1,UVA=-1, other with UVA=1,VT=-1)
        int[] UVA_1_array = get_people_array_UVA_1(raw_input_str);
        int[] VT_1_array = get_people_array_VT_1(raw_input_str);

        // 3. Get two solutions
        Solution UVA_1_sol = solve_one_school(UVA_1_array, SchoolName.UVa);
        Solution VT_1_sol = solve_one_school(VT_1_array, SchoolName.VT);
        // 3a. Adjust indices for 1-based output
        UVA_1_sol.max_i += 1; UVA_1_sol.max_j += 1;
        VT_1_sol.max_i += 1; VT_1_sol.max_j += 1;

        // 4. Pick the optimal solution
        //  and display it
        if (UVA_1_sol.compareTo(VT_1_sol) > 0) { // UVA wins
            System.out.println("UVa");
            System.out.print(UVA_1_sol.max_i + " ");
            System.out.println(UVA_1_sol.max_j);
        }
        else if (UVA_1_sol.compareTo(VT_1_sol) < 0) { // VT wins
            System.out.println("VT");
            System.out.print(VT_1_sol.max_i + " ");
            System.out.println(VT_1_sol.max_j);
        }
        else { // tie- break the tie here manually
            // whichever one starts first
            if (UVA_1_sol.max_i < VT_1_sol.max_i) { // Tie, and UVA's subarray starts first
                System.out.println("UVa");
                System.out.print(UVA_1_sol.max_i + " ");
                System.out.println(UVA_1_sol.max_j);
            }
            else if (UVA_1_sol.max_i > VT_1_sol.max_i) { //Tie, and VT's subarray starts first
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
                }
                else if (UVA_1_sol.max_j > VT_1_sol.max_j) { //Tie, and VT's subarray ends first
                    System.out.println("VT");
                    System.out.print(VT_1_sol.max_i + " ");
                    System.out.println(VT_1_sol.max_j);
                }
                // exact, exact, exact tie not possible
            }
        }
    }


}
