package unsw.gloriaromanus;

import unsw.gloriaromanus.game.Game;
import unsw.gloriaromanus.units.SoldierType;
import unsw.gloriaromanus.world.Province;
import unsw.gloriaromanus.world.TaxLevel;

import java.util.ArrayList;
import java.util.Scanner;

public class MainTextual {
    enum State { MAIN_MENU, GAME_RUNNING}
    enum MenuOptions {CONTINUE, NEW_GAME, SAVE_GAME, LOAD_GAME, EXIT, INVALID}
    enum GameRunningOptions {SHOW_INFO, SELECT_REGION, PASS, MENU, EXIT, INVALID}
    enum ProvinceSelectedOptions {SET_TAXES, RECRUIT, MOVE_TROOPS, CANCEL, INVALID}

    private static State state;
    private static Game game;
    public static void  main (String [] args) {
        Scanner in = new Scanner(System.in);
        state = State.MAIN_MENU;
        while (true){
        while (state == State.MAIN_MENU) {
            printMainMenu();
            MenuOptions opt = getMainMenuInput(in);
            switch (opt) {
                case CONTINUE:
                    if (game != null)
                        state = State.GAME_RUNNING;
                    break;
                case NEW_GAME:
                    System.out.println("Select Battle Resolver:");
                    System.out.println("(Future release)");
                    System.out.println("Select AI:");
                    System.out.println("(Future release)");
                    System.out.println("Select Faction:");
                    System.out.println("(Future release)");
                    Game.newGame("Rome");
                    game = Game.getInstance();
                    state = State.GAME_RUNNING;
                    break;
                case SAVE_GAME:
                    System.out.println("Enter filename:");
                    String save = in.nextLine();
                    System.out.println(save);
                    if (game.saveGame(save))
                        System.out.println("Game saved!");
                    else
                        System.out.println("Something went wrong...");
                    break;
                case LOAD_GAME:
                    System.out.println("Enter filename:");
                    if (!Game.loadGame(in.nextLine()))
                        System.out.println("Invalid filename!");
                    else {
                        game = Game.getInstance();
                        state = State.GAME_RUNNING;
                    }
                    break;
                case EXIT:
                    return;
                default:
                    break;
            }
        }
        System.out.println("Game started!");
        while (state == State.GAME_RUNNING) {
            printGameRunningOptions();
            GameRunningOptions opt = getGameRunningInput(in);
            switch (opt) {
                case SHOW_INFO:
                    System.out.println(game.info());
                    break;
                case SELECT_REGION:
                    ArrayList<Province> prs = game.getFaction("Rome").getProvinces();
                    for (int i = 0; i < prs.size(); i++) {
                        System.out.println("["+ (i+1) + "] " +prs.get(i).getName() + ": " + prs.get(i).getArmy());
                    }
                    int province_num = readInt(in) - 1;
                    Province province = prs.get(province_num);
                    if (province == null){
                        System.out.println("Invalid province");
                        break;
                    }
                    System.out.println(province);
                    printProvinceSelectedOptions();
                    ProvinceSelectedOptions p_opt = getProvinceSelectedInput(in);
                    switch (p_opt){
                        case SET_TAXES:
                            System.out.println("Current tax level: "+province.getTaxLevel().toString());
                            printAListOfOptions(4, new String[]{"Low Taxes",
                                    "Normal Taxes", "High Taxes", "Very High Taxes"});
                            int choice = readInt(in);
                            if (choice == 1)
                                province.setTaxLevel(TaxLevel.LOW_TAX);
                            if (choice == 2)
                                province.setTaxLevel(TaxLevel.NORMAL_TAX);
                            if (choice == 3)
                                province.setTaxLevel(TaxLevel.HIGH_TAX);
                            if (choice == 4)
                                province.setTaxLevel(TaxLevel.VERY_HIGH_TAX);
                            break;
                        case RECRUIT:
                            int slots = province.numberOfTrainingSlotsAvailable();
                            System.out.println("There are "+ slots +" slots available");
                            if (slots != 0){
                                System.out.println("Which unit to train?");
                                printAListOfOptions(6, new String[] {"melee infantry: 100", "melee chivalry: 180",
                                    "melee artillery: 150", "ranged infantry: 100", "ranged chivalry: 150", "ranged artillery: 160"});
                                boolean recruited;
                                int choice1 = readInt(in);
                                switch (choice1) {
                                    case 1:
                                        recruited = game.recruit(province, SoldierType.MELEE_INFANTRY);
                                        break;
                                    case 2:
                                        recruited = game.recruit(province, SoldierType.MELEE_CHIVALRY);
                                        break;
                                    case 3:
                                        recruited = game.recruit(province, SoldierType.MELEE_ARTILLERY);
                                        break;
                                    case 4:
                                        recruited = game.recruit(province, SoldierType.RANGED_INFANTRY);
                                        break;
                                    case 5:
                                        recruited = game.recruit(province, SoldierType.RANGED_CHIVALRY);
                                        break;
                                    case 6:
                                        recruited = game.recruit(province, SoldierType.RANGED_ARTILLERY);
                                        break;
                                    default:
                                        recruited = false;
                                        break;
                                }
/*
                                switch (choice1) {
                                    case 1:
                                        recruited = game.recruit(game.getPlayer(), province, SoldierType.MELEE_INFANTRY);
                                        break;
                                    case 2:
                                        recruited = game.recruit(game.getPlayer(), province, SoldierType.MELEE_CHIVALRY);
                                        break;
                                    case 3:
                                        recruited = game.recruit(game.getPlayer(), province, SoldierType.MELEE_ARTILLERY);
                                        break;
                                    case 4:
                                        recruited = game.recruit(game.getPlayer(), province, SoldierType.RANGED_INFANTRY);
                                        break;
                                    case 5:
                                        recruited = game.recruit(game.getPlayer(), province, SoldierType.RANGED_CHIVALRY);
                                        break;
                                    case 6:
                                        recruited = game.recruit(game.getPlayer(), province, SoldierType.RANGED_ARTILLERY);
                                        break;
                                    default:
                                        recruited = false;
                                        break;
                                }
*/
                                if (recruited)
                                    System.out.println("Training!");
                                else
                                    System.out.println("Not enough gold!");
                            }
                            break;
                        case MOVE_TROOPS:
                            ArrayList <Province> neighbours = new ArrayList<>();
                            for (String s: province.getNeighbours()){
                                System.out.println(neighbours.size());
                                neighbours.add(game.getProvince(s));}
                            System.out.println("Adjacent provinces: ");
                            for (int i = 0; i < neighbours.size(); i++) {
                                System.out.println("[" + (i+1) + "] " +
                                        neighbours.get(i).getName() + neighbours.get(i).getOwner());
                            }
                            int choice3 = readInt(in) - 1;
                            if (choice3 <= neighbours.size())
                                game.moveOrInvade(province, neighbours.get(choice3));

                            break;
                        case CANCEL:
                            break;
                        default:
                            break;
                    }

                    break;
                case PASS:
                    if (game.pass())
                        System.out.println("Congratulations! YOU WON!");
                    break;
                case MENU:
                    state = State.MAIN_MENU;
                    break;
                case EXIT:
                    return;
            }
        }}
    }

    private static ProvinceSelectedOptions getProvinceSelectedInput(Scanner in) {
        switch (readInt(in)) {
            case 1:
                return ProvinceSelectedOptions.SET_TAXES;
            case 2:
                return ProvinceSelectedOptions.RECRUIT;
            case 3:
                return ProvinceSelectedOptions.MOVE_TROOPS;
            case 4:
                return ProvinceSelectedOptions.CANCEL;
            default:
                return ProvinceSelectedOptions.INVALID;
        }
    }
    private static void printProvinceSelectedOptions() {
        System.out.println("What to do?");
        System.out.println("[1] Set taxes");
        System.out.println("[2] Recruit units");
        System.out.println("[3] Move troops");
        System.out.println("[4] Cancel");
    }

    private static GameRunningOptions getGameRunningInput(Scanner in) {
        switch (readInt(in)) {
            case 1:
                return GameRunningOptions.SHOW_INFO;
            case 2:
                return GameRunningOptions.SELECT_REGION;
            case 3:
                return GameRunningOptions.PASS;
            case 4:
                return GameRunningOptions.MENU;
            case 5:
                return GameRunningOptions.EXIT;
            default:
                return GameRunningOptions.INVALID;
        }
    }
    private static MenuOptions getMainMenuInput(Scanner in) {
        switch (readInt(in)) {
            case 1:
                return MenuOptions.CONTINUE;
            case 2:
                return MenuOptions.NEW_GAME;
            case 3:
                return MenuOptions.SAVE_GAME;
            case 4:
                return MenuOptions.LOAD_GAME;
            case 5:
                return MenuOptions.EXIT;
            default:
                return MenuOptions.INVALID;
        }
    }

    private static void printMainMenu() {
        System.out.println("Gloria Romanus!\nPlease select an option:");
        System.out.println("[1] Continue");
        System.out.println("[2] New Game");
        System.out.println("[3] Save Game");
        System.out.println("[4] Load Game");
        System.out.println("[5] Exit");
    }
    private static void printGameRunningOptions() {
        System.out.println("Turn " + game.getTurn()+", gold: "+game.getPlayerGold()+", wealth: "+game.getPlayerWealth());
        System.out.println("What do you want to do?");
        System.out.println("[1] Show game infos");
        System.out.println("[2] Select a region");
        System.out.println("[3] Pass");
        System.out.println("[4] Menu");
        System.out.println("[5] Exit");
    }
    private static void printAListOfOptions (int num , String[] opts) {
        for (int i = 0; i < num; i++) {
            System.out.println("[" + (i+1) + "] "+opts[i]);
        }
    }
    private static int readInt (Scanner in) {
        return Integer.parseInt(in.nextLine());
    }
}
