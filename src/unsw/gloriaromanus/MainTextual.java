package unsw.gloriaromanus;

import unsw.gloriaromanus.game.Faction;
import unsw.gloriaromanus.game.Game;

import java.util.Scanner;

public class MainTextual {
    enum State { MAIN_MENU, GAME_RUNNING}
    enum MenuOptions {NEW_GAME, LOAD_GAME, EXIT}
    private static State state;
    private static Game game;
    public static void  main (String [] args) {
        Scanner in = new Scanner(System.in);
        state = State.MAIN_MENU;

        while (state == State.MAIN_MENU) {
            printMainMenu();
            MenuOptions opt = getMainMenuInput(in);
            switch (opt) {
                case NEW_GAME:
                    System.out.println("Select Battle Resolver:");
                    System.out.println("(Future release)");
                    System.out.println("Select AI:");
                    System.out.println("(Future release)");
                    System.out.println("Select Faction:");
                    System.out.println("TODO:");//TODO
                    Game.newGame("Rome");
                    game = Game.getInstance();
                    state = State.GAME_RUNNING;
                    break;
                case LOAD_GAME:
                    System.out.println("Enter filename:");
                    if (game.loadGame(in.nextLine()))
                        System.out.println("Invalid filename!");
                    else {
                        state = State.GAME_RUNNING;
                    }
                    break;
                case EXIT:
                    return;
                default:
                    ;
            }
        }
        System.out.println("Game started!");
        while (state == State.GAME_RUNNING) {
            System.out.println(game.getFaction("Rome").getProvinces());
            return;
        }
    }

    private static MenuOptions getMainMenuInput(Scanner in) {
        switch (in.nextInt()) {
            case 1:
                return MenuOptions.NEW_GAME;
            case 2:
                return MenuOptions.LOAD_GAME;
            case 3:
                return MenuOptions.EXIT;
            default:
                return null;
        }
    }

    private static void printMainMenu() {
        System.out.println("Gloria Romanus!\nPlease select an option:");
        System.out.println("[1] New Game");
        System.out.println("[2] Load Game");
        System.out.println("[3] Exit");
    }
}
