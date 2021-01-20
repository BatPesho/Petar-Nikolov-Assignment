package com.company;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    static int[][] state;   // two dimensional array of integers representing the second layer
    static int twoD[][];    // input Layer

    static int brickNo = 0; //the current number of the placed brick

     /**********************************************************************
     * static method deep_copy
     * input parameters:
     * original - two dimensional array of integers
     * returns the copy of input array
     * ********************************************************************/
    private static int[][] deepCopy(int[][] original) {
        final int[][] result = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return result;
    }

     /**********************************************************************
     * static method count_by_state
     * Recursive calls itself to arrange the layout
     * input parameters:
     * state_current - current arrangement
     * returns 1 if the layout is fully populated and 0 otherwise
     * ********************************************************************/
    private static int count_by_state(int[][] state_current, int brickNo) {
        int i_rows = state_current.length;
        int i_columns = state_current[0].length;

        for (int i = 0; i < i_rows; i++) {
            for (int j = 0; j < i_columns; j++) {
                if (state_current[i][j] == 0) {
                    int count_down = 0;
                    int count_right = 0;

                    if (j < i_columns - 1) // check for enough space to place brick on the right
                    {
                        if (state_current[i][j + 1] == 0) {
                            int[][] state_new = deepCopy(state_current); //keep the state of the current iteration so we can return to it
                            int brickNoNew = brickNo;
                            state_new[i][j] = ++brickNoNew;     //place brick horizontally
                            state_new[i][j + 1] = brickNoNew;   //

                            if (twoD[i][j] != twoD[i][j + 1]) { //if current brick completely overlaps another brick from the input layer then this arrangement is wrong and we cancel it
                                count_right = count_by_state(state_new, brickNoNew); //continue with next iteration
                            }
                        }
                    }
                    if(count_right == 0) { //Solution not found yet. If You want to see all of the solutions, remove this condition
                        if (i < i_rows - 1) // check for enough space to place brick downwards
                        {
                            if (state_current[i + 1][j] == 0) {
                                int[][] state_new = deepCopy(state_current);
                                int brickNoNew = brickNo;
                                state_new[i][j] = ++brickNoNew;     //place brick vertically
                                state_new[i + 1][j] = brickNoNew;   //

                                if (twoD[i][j] != twoD[i + 1][j]) { //if current brick completely overlaps another brick from the input layer then this arrangement is wrong and we cancel it
                                    count_down = count_by_state(state_new, brickNoNew); //continue with next iteration
                                }
                            }
                        }
                    }
                    return count_down + count_right; // if not found right or down, just return 0;
                }
            }
        }
        //--------- Formatted printing out of the solution -----------
        String separator = "";
        for (int j = 0; j < i_columns; j++) {
            separator += "----";
        }
        System.out.println(separator);
        for (int i = 0; i < i_rows; i++) {
            System.out.print("|");
            for (int j = 0; j < i_columns; j++) {
                try {
                    if (state_current[i][j] == state_current[i][j + 1])
                        separator = " ";
                    else
                        separator = "|";
                }catch (ArrayIndexOutOfBoundsException ignored){separator = "";}
                System.out.printf("% 3d%s", state_current[i][j], separator);
            }
            System.out.println("|");
            if(i < i_rows-1)
                System.out.print("|");
            for (int j = 0; j < i_columns; j++) {
                try {
                    if (state_current[i][j] == state_current[i+1][j]) {
                        separator = "   ";
                        if(j == i_columns-1)
                            separator += "|";
                        else
                            separator += " ";
                    }else {
                        separator = "---";
                        if(j == i_columns-1)
                            separator += "|";
                        else
                            separator += "-";
                    }
                }catch (ArrayIndexOutOfBoundsException ignored){separator = "";}
                System.out.printf(separator);
            }
            if(i < i_rows-1)
                System.out.println();
         }
        for (int j = 0; j < i_columns; j++) {
            separator += "----";
        }
        System.out.println(separator);
        System.out.println();

        return 1; // All filled
    }

    public static void main(String args[]) {

        System.out.print("Enter 2D array size : ");
        Scanner sc = new Scanner(System.in);
        int rows = sc.nextInt();
        int columns = sc.nextInt();
        int bricks = rows * columns / 2;

        if(rows>100 && columns>100){
            System.out.printf("\nArray dimensions are greater than 100. Try again. \n");
            System.exit(0);
        }

        if(rows%2!=0 || columns%2!=0){
            System.out.printf("\nArray dimensions are not even numbers. Try again. \n");
            System.exit(0);
        }

        System.out.println("Number of bricks : " + bricks);
        System.out.println("Enter array elements : ");

        twoD = new int[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                twoD[i][j] = sc.nextInt();
            }
        }
        System.out.print("\nData you entered : \n");
        for (int[] x : twoD) {
            for (int y : x) {
                System.out.printf("% 3d",y);
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
        //An array that counts the number of pieces of every brick
        int[] brickHalvesCounter = new int[bricks];

        //Checking whether bricks' halves reside in neighbouring fields
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                int match_neighbors = 0;
                try {
                    if (twoD[i][j] == twoD[i][j - 1]) //left
                        match_neighbors++;
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }

                try {
                    if (twoD[i][j] == twoD[i][j + 1]) //right
                        match_neighbors++;
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }

                try {
                    if (twoD[i][j] == twoD[i - 1][j]) //top
                        match_neighbors++;
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }

                try {
                    if (twoD[i][j] == twoD[i + 1][j]) //bottom
                        match_neighbors++;
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }

                if (match_neighbors == 0) {
                    System.out.printf("\nBroken brick No:%d error. Try again.   \n", twoD[i][j]);
                    System.exit(-1);
                } else if (match_neighbors > 1) {
                    System.out.printf("\nBrick No:%d has more than two halves error. Try again.   \n", twoD[i][j]);
                    System.exit(-1);
                }
                try {
                    brickHalvesCounter[twoD[i][j] - 1]++;
                } catch (ArrayIndexOutOfBoundsException ignored) {
                    System.out.printf("\nBrick numerification is not in sequence error. Try again.   \n", twoD[i][j]);
                    System.exit(-1);
                }
            }

        }

        for (int i = 0; i < bricks; i++) {
            if (brickHalvesCounter[i] > 2) {
                System.out.printf("\nBrick No:%d is encountered more than once error. Try again.   \n", i);
                System.exit(-1);
            }
        }

        //------------ Create and initialize working layout -----------------
        state  = new int[rows][columns]; //Create initial layout

        for(int i = 0; i < (state.length - 1); i++){ // and fill it with "0"
            for(int j = 0; j < (state[0].length - 1); j++){
                state[i][j] = 0;
            }
        }

        int counts = count_by_state(state, brickNo); //start
        if(counts == 0) {
            System.out.println("Exit code -1\r\nNo solution found");
        }/*else{
            System.out.printf("Number of solutions found = %d", counts);
        }*/

    }
}

