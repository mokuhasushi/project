package unsw.gloriaromanus;

import org.checkerframework.checker.units.qual.A;
import unsw.gloriaromanus.game.Faction;
import unsw.gloriaromanus.game.Game;
import unsw.gloriaromanus.units.SoldierType;
import unsw.gloriaromanus.world.Province;
import unsw.gloriaromanus.world.TaxLevel;

import java.util.ArrayList;
import java.util.Scanner;

public class MainTextual {
    enum State { MAIN_MENU, GAME_RUNNING}
    enum MenuOptions {CONTINUE, NEW_GAME, SAVE_GAME, LOAD_GAME, EXIT}
    enum GameRunningOptions {SHOW_REGIONS, SELECT_REGION, PASS, MENU, EXIT}
    enum ProvinceSelectedOptions {SET_TAXES, RECRUIT, MOVE_TROOPS, CANCEL}

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
                    System.out.println("TODO:");//TODO
                    Game.newGame("Rome");
                    game = Game.getInstance();
                    state = State.GAME_RUNNING;
                    break;
                case SAVE_GAME:
                    System.out.println("TODO");//TODO
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
            printGameRunningOptions();
            GameRunningOptions opt = getGameRunningInput(in);
            switch (opt) {
                case SHOW_REGIONS:
                    for (Province p: game.getFaction("Rome").getProvinces())
                        System.out.println(p.getName() + ": " + p.getArmy());
                    break;
                case SELECT_REGION:
                    ArrayList<Province> prs = game.getFaction("Rome").getProvinces();
                    for (int i = 0; i < prs.size(); i++) {
                        System.out.println("["+ (i+1) + "] " +prs.get(i).getName() + ": " + prs.get(i).getArmy());
                    }
                    int province_num = in.nextInt() - 1;
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
                            int choice = in.nextInt();
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
                                printAListOfOptions(6, new String[] {"melee infantry", "melee chivalry",
                                    "melee artillery", "ranged infantry", "ranged chivalry", "ranged artillery"});
                                int choice1 = in.nextInt();
                                    switch (choice1) {
                                        case 1:
                                            province.recruit(SoldierType.MELEE_INFANTRY);
                                            break;
                                        case 2:
                                            province.recruit(SoldierType.MELEE_CHIVALRY);
                                            break;
                                        case 3:
                                            province.recruit(SoldierType.MELEE_ARTILLERY);
                                            break;
                                        case 4:
                                            province.recruit(SoldierType.RANGED_INFANTRY);
                                            break;
                                        case 5:
                                            province.recruit(SoldierType.RANGED_CHIVALRY);
                                            break;
                                        case 6:
                                            province.recruit(SoldierType.RANGED_ARTILLERY);
                                            break;
                                        default:
                                            break;
                                    }
                            }
                            break;
                        case MOVE_TROOPS:
                            ArrayList <Province> neighbours = new ArrayList<>();
                            for (String s: province.getNeighbours())
                                neighbours.add(game.getProvince(s));
                            System.out.println("Adjacent provinces: ");
                            for (int i = 0; i < neighbours.size(); i++) {
                                System.out.println("[" + (i+1) + "] " +
                                        neighbours.get(i).getName() + neighbours.get(i).getOwner());
                            }
                            int choice3 = in.nextInt() - 1;
                            if (choice3 <= neighbours.size())
                                game.moveOrInvade(province, neighbours.get(choice3));

                            break;
                        case CANCEL:
                            break;
                    }

                    break;
                case PASS:
                    game.pass();
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
        switch (in.nextInt()) {
            case 1:
                return ProvinceSelectedOptions.SET_TAXES;
            case 2:
                return ProvinceSelectedOptions.RECRUIT;
            case 3:
                return ProvinceSelectedOptions.MOVE_TROOPS;
            case 4:
                return ProvinceSelectedOptions.CANCEL;
            default:
                return null;
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
        switch (in.nextInt()) {
            case 1:
                return GameRunningOptions.SHOW_REGIONS;
            case 2:
                return GameRunningOptions.SELECT_REGION;
            case 3:
                return GameRunningOptions.PASS;
            case 4:
                return GameRunningOptions.MENU;
            case 5:
                return GameRunningOptions.EXIT;
            default:
                return null;
        }
    }
    private static MenuOptions getMainMenuInput(Scanner in) {
        switch (in.nextInt()) {
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
                return null;
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
        System.out.println("[1] Show your regions");
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
}