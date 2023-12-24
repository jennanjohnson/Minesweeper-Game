/**
 * Minesweeper game that is played on the console. Player navigates a grid, trying to avoid mines.
 *  CSE 174
 *  Section A
 *  @author Danny Kirk & Jenna Johnson
 */

// importing Random and Scanner classes
import java.util.Random;
import java.util.Scanner;

public class FinalProject
{
    public static void initializeGrid(String[][] grid)
    {
        int gridSize = 10;
        // Fill the grid with the brown square
        for (int i = 0; i < gridSize; i++)
        {
            for (int j = 0; j < gridSize; j++)
            {
                grid[i][j] = "\uD83D\uDFEB"; // brown square
            }
        }
    }
    public static void placeMines(String[][] grid)
    {
        // declare and initialize counter variables for mines placed and total mines of each type
        int detectableMines = 10;
        int undetectableMines = 10;
        int detectableMinesPlaced = 0;
        int undetectableMinesPlaced = 0;

        Random random = new Random(); // declare random variable
        int row;
        int col;
        int gridSize = 10; // set grid size

        // Place mines
        while(detectableMinesPlaced < detectableMines || undetectableMinesPlaced < undetectableMines)
        {
            row = random.nextInt(gridSize);
            col = random.nextInt(gridSize);

            //checking if mines are in corner
            if(!((row == 0 && col == 0) || (row == 0 && col == 1) || (row == 1 && col == 0)))
            {
                // placing 10 detectable and 10 undetectable mines
                if(!grid[row][col].equals("\uD83D\uDEA8") && !grid[row][col].equals("\uD83D\uDCA3"))
                {
                    if (detectableMinesPlaced < detectableMines)
                    {
                        grid[row][col] = "\uD83D\uDCA3"; // Red bomb representation for detectable mines
                        detectableMinesPlaced++;
                    }
                    else if (undetectableMinesPlaced < undetectableMines)
                    {
                        grid[row][col] = "\uD83D\uDEA8"; // Black bomb representation for undetectable mines
                        undetectableMinesPlaced++;
                    }
                }
            }
        }
    }

    public static void printGrid(String[][] grid, boolean[][] path)
    {
        // Display the final state of the grid, iterate through and print
        int gridSize = 10;
        for (int i = 0; i < gridSize; i++)
        {
            for (int j = 0; j < gridSize; j++)
            {
                if(path[i][j])
                {
                System.out.print("\uD83D\uDFEA" + " "); // update player path to purple square
                }
                else
                {
                    System.out.print(grid[i][j] + " ");
                }

            }
            System.out.println();
        }
    }

    public static void detectNearbyMines(String[][] grid, int playerRow, int playerCol)
    {
        // arrays for neighboring cells to be checked for detectable mines:
        int[] rowOffsets = {-1, 0, 1, 0};
        int[] colOffsets = {0, -1, 0, 1};

        int gridSize = 10;
        // counters for mines
        int detectableBombs = 0;
        int undetectableBombs = 0;
        // declare variable for neighboring row and col
        int neighborRow;
        int neighborCol;

        for (int i = 0; i < rowOffsets.length; i++)
        {
            neighborRow = playerRow + rowOffsets[i];
            neighborCol = playerCol + colOffsets[i];
            // Check if the neighboring position is within the grid bounds
            if (neighborRow >= 0 && neighborRow < gridSize && neighborCol >= 0 && neighborCol < gridSize)
            {
                // if detectable bomb at position:
                if (grid[neighborRow][neighborCol].equals("\uD83D\uDCA3"))
                {
                    // replace detectable bombs with green check
                    grid[neighborRow][neighborCol] = grid[neighborRow][neighborCol].replace("\uD83D\uDCA3", "\u2705");
                    detectableBombs++;
                }
                // undetectable bombs
                else if (grid[neighborRow][neighborCol].equals("\uD83D\uDEA8"))
                {
                    undetectableBombs++;
                }
            }
        }
        System.out.println(detectableBombs + " regular mine(s) were detected and swept nearby, but there's still " + undetectableBombs + " undetectable mine(s) nearby!");
    }

    public static void main(String[] args)
    {
        // Set the grid size
        int gridSize = 10;
        int row = 0;
        int col = 0;
        int moveNumber = 0; // counts the number of moves the player makes

        // Create the grid
        String[][] grid = new String[gridSize][gridSize];

        // invoking method to initialize grid
        initializeGrid(grid);
        // invoking method to place mines in grid
        placeMines(grid);

        // initialize scanner variable
        Scanner scanner = new Scanner(System.in);


        boolean gameOver = false;

        int newRow = row;
        int newCol = col;

        boolean[][] path = new boolean[gridSize][gridSize];

        System.out.println("Move 15 times without hitting a bomb and you win!");
        while (!gameOver)
        {
            // prompt user to move
            System.out.print("W/A/S/D to move, Q to quit: ");
            String playerMove = scanner.nextLine();

            if (moveNumber <= 15)
            {

                if (playerMove.equalsIgnoreCase("q"))
                {
                    break;
                }
                else if (playerMove.equalsIgnoreCase("w"))
                {
                    newRow--;
                }
                else if (playerMove.equalsIgnoreCase("a"))
                {
                    newCol--;
                }
                else if (playerMove.equalsIgnoreCase("s"))
                {
                    newRow++;
                }
                else if (playerMove.equalsIgnoreCase("d"))
                {
                    newCol++;
                }

                // Check if the new position is within the grid bounds
                if (newRow >= 0 && newRow < gridSize && newCol >= 0 && newCol < gridSize)
                {
                    if (grid[newRow][newCol].equals("\uD83D\uDEA8"))
                    {
                        System.out.println("Game over! You hit a mine.");
                        grid[newRow][newCol] = grid[newRow][newCol].replace("\uD83D\uDEA8", "\uD83D\uDD25");
                        gameOver = true;

                    }
                    // Update the position only if it's within the grid bounds
                    else
                    {
                        path[row][col] = true;
                        grid[newRow][newCol] = "\uD83D\uDFEA"; // update the player's position to a purple square
                        grid[row][col] = "\uD83D\uDFEB"; // Reset the current position
                        row = newRow;
                        col = newCol;
                        moveNumber++;
                    }

                }
                else  // when the player makes an invalid move
                {
                    System.out.println("Invalid move, try again.");
                }
            }
            // detect nearby mines if the player has less than 15 moves and game is not won yet
            if (!gameOver && moveNumber < 15)
            {
                detectNearbyMines(grid, row, col);
            }
            // if player makes 15 moves successfully, game is won
            if (moveNumber == 15)
            {
                System.out.println("Congratulations! You won!");
                break;
            }
        }
        // invoking method for printing the grid
        printGrid(grid, path);
    }
}
