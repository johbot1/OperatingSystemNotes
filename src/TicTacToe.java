package src;

import java.io.FileReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TicTacToe {
    private static final int DEFAULT_SQUARES = 9;

    public static void main(String[] args) {
        List<Move> moves = null;
        Symbol first = Symbol.X;
        int numSquares = DEFAULT_SQUARES;
        for (int i = 0; i < args.length; i++) {
            switch (i) {
                case 0:
                    moves = loadMoves(args[i]);
                    break;

                case 1:
                    first = parseSymbol(args[i]);
                    break;

                case 2:
                    numSquares = parseInt(args[i]);
                    break;
            }
        }
        if (!validateData(moves, first, numSquares)) {
            System.err.println("Failed");
            System.exit(-1);
        }
        //Debugging to make sure I did it correctly lol
        if (moves != null) {
            System.out.println("\nLoaded Moves:");
            for (Move move : moves) {
                System.out.println("Symbol: " + move.symbol + ", Location: " + move.location);
            }
        }
        // Teach the AI program using the data
    }

    //HOMEWORK: Load in all moves.txt, then generate a move for each line
    private static List<Move> loadMoves(String filename) {
        //Check if the file name is not null, and isn't empty
        if (filename == null || filename.trim().isEmpty()) {
            System.err.println("Error: Filename cannot be empty.");
            return null;
        }

        List<Move> movesList = new java.util.ArrayList<>(); // Initialize the list to store moves
        int lineNumber = 0; // Keep track of line number for error reporting

        //If the file is readable,
        try (java.io.FileReader fileReader = new java.io.FileReader(filename);
             java.io.BufferedReader bufferedReader = new java.io.BufferedReader(new FileReader(filename))) {
            String line;

            // skip all empty lines and comments,
            while ((line = bufferedReader.readLine()) != null) {
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                // then split line by whitespace (one or more spaces).
                String[] parts = line.split("\\s+");
                if (parts.length != 2) {
                    System.err.println("Warning: Invalid move format in file '" +
                            filename + "' at line " + lineNumber + ": '" + line +
                            "'. Expected format: 'SYMBOL LOCATION'. Skipping line.");
                    continue;
                }
                // Instantiate the two parts (Symbol and location),
                String symbolStr = parts[0];
                String locationStr = parts[1];
                Symbol symbol = parseSymbol(symbolStr);
                int location = parseInt(locationStr);

                // then scan to make sure the symbol is valid.
                if (symbol == Symbol.INVALID) {
                    System.err.println("Warning: Invalid symbol: '" + symbolStr + "'. Skipping line.");
                    continue;
                }

                // Another check to ensure there is no invalid locations,
                if (location <= 0 || location > DEFAULT_SQUARES) {
                    System.err.println("Warning: Invalid location: '" + locationStr + "'. Location must be positive. Skipping line.");
                    continue;
                }
                // before finally adding in the moves to the list.
                Move move = new Move();
                move.symbol = symbol;
                move.location = location;
                movesList.add(move);

            }
        } catch (java.io.IOException e) {
            System.err.println("[LOADMOVES] ERROR");
            System.err.println("Error reading file: " + filename);
            e.printStackTrace(); // Print the error details for debugging
            return null;
        }
        System.out.println("Loaded " + movesList.size() + " moves from " + filename);
        return movesList;
    }

    private static int parseInt(String num) {
        try {
            return Integer.parseInt(num);
        } catch (NumberFormatException ignored) {
        }
        return 0;
    }

    private static Symbol parseSymbol(String symbol) {
        // If you give me garbage, I'll give it back
        if (symbol == null) {
            return Symbol.INVALID;
        }

        switch (symbol.toUpperCase().trim()) {
            case "X":
                return Symbol.X;

            case "O":
                return Symbol.O;

            default:
                return Symbol.INVALID;
        }
    }

    private static boolean validateData(List<Move> moves, Symbol first, int numSquares) {
        // Validate the number of squares
        boolean success = true;
        double squareOfNumSquares = Math.sqrt(numSquares);
        double remainder = squareOfNumSquares - (int) squareOfNumSquares;
        if (numSquares < DEFAULT_SQUARES || remainder > 0) {
            System.err.println("Error: Invalid number of squares (" + numSquares + ")");
            success = false;
        }

        // Validate the first move
        if (first == Symbol.INVALID) {
            System.err.println("Error: Invalid start Symbol");
            success = false;
        }

        // Validate the number of Moves. Bail early because all the rest
        // of the moves validation relies on having a non-null moves list.
        if (moves == null) {
            System.err.println("Error: No moves loaded");
            return false;
        }

        // Continue validation on number of moves
        if (moves.size() > numSquares) {
            System.err.println("Error: Number of moves does not match number of squares");
            success = false;
        }

        // Validate the actual Moves
        final Set<Integer> usedLocations = new HashSet<>();
        Symbol nextMove = first;
        for (Move m : moves) {
            if (m.symbol == Symbol.INVALID) {
                System.err.println("Error: At least one of the moves has an invalid Symbol");
                success = false;
            }
            if (usedLocations.contains(m.location)) {
                System.err.println("Error: The same location (" + m.location + ") was used multiple times");
                success = false;
            } else {
                usedLocations.add(m.location);
            }
            if (m.symbol != nextMove) {
                System.err.println("Error: Moves did not alternate");
                success = false;
            }
            nextMove = m.symbol.nextMove();
        }

        return success;
    }

    private enum Symbol {
        INVALID,
        X,
        O;

        Symbol nextMove() {
            return switch (this) {
                case X -> O;
                case O -> X;
                default -> INVALID;
            };
        }
    }

    private static class Move {
        Symbol symbol;
        int location;
    }
}
