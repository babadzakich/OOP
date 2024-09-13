package ru.nsu.chuvashov;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import static java.lang.Math.abs;

/**
 * Blackjack implementation.
 *
 * @author artyom.
 */
public class Main {
    /**
     * starting point for program.
     *
     * @param args empty.
     */
    public static void main(String[] args) {
        Game game = new Game();
        game.BlackJack();
    }
}

/**
 * Class for main logic behind the game.
 */
class Game {
    private static int round;
    private static int player_score;
    private static int dealer_score;
    private static boolean closed_card;
    static ArrayList<Card> player = new ArrayList<>();
    static ArrayList<Card> dealer = new ArrayList<>();

    /**
     * Starter of game.
     */
    public void blackJack() {
        System.out.println("Добро пожаловать в Блэкджек!");
        round = 1;
        player_score = dealer_score = 0;
        Deck.create_big_deck();
        Game.round();
    }

    /**
     * Round logic with start, player move, dealer move and resolve.
     */
    private static void round() {
        int resultOfGame;
        while (abs(player_score - dealer_score) < 3) {
            resultOfGame = round_start();
            if (resultOfGame > 0) {
                System.out.println("Победили Русские!!!!!!\n");
                player_score++;
                end_round();
                continue;
            }
            resultOfGame = player_move();
            if (resultOfGame > 0) {
                System.out.println("Игрок набрал Блэкджек!\n");
                player_score++;
                end_round();
                continue;
            } else if (resultOfGame < 0) {
                System.out.println("Перебор, Дилер победил\n");
                dealer_score++;
                end_round();
                continue;
            }
            resultOfGame = dealer_move();
            if (resultOfGame < 0) {
                System.out.println("Перебор, победил игрок\n");
                player_score++;
                end_round();
            } else if (resultOfGame > 0) {
                System.out.println("Диллер собрал Блэкджек, он победил!\n");
                dealer_score++;
                end_round();
            } else {
                if (Deck.get_points(player) < Deck.get_points(dealer)) {
                    System.out.println("Диллер набрал больше, он победил!\n");
                    dealer_score++;
                    end_round();
                } else if (Deck.get_points(player) > Deck.get_points(dealer)) {
                    System.out.println("Игрок набрал больше, он победил!\n");
                    player_score++;
                    end_round();
                } else {
                    System.out.println("Игрок и Диллер набрали одинаково, получилась ничья!\n");
                    end_round();
                }
            }
        }
        if (player_score > dealer_score) {
            System.out.println("Игрок победил, казино в проигрыше!");
        } else {
            System.out.println("Диллер выиграл, Казино в выигрыше!");
        }
    }

    /**
     * ending of round.
     * clears all player decks, shows score and increments round.
     */
    private static void end_round() {
        player.clear();
        dealer.clear();
        round ++;
        System.out.println("Cчет " + player_score + " : " + dealer_score);
    }

    /**
     * Starts round, shuffles deck and gives cards to player.
     *
     * @return points that acquired player(we could get 21 instantly).
     */
    private static int round_start() {
        Deck.shuffle_deck();
        System.out.println("Раунд " + round + "\nДиллер раздал карты");
        Deck.create_small_deck(player);
        Deck.create_small_deck(dealer);
        closed_card = true;
        get_round_info(true);
        return calculate_points(player);
    }

    /**
     * Logic for player.
     * By pressing 1 player draws a card,
     * if he receives more than 21, he loses(returns -1),
     * if he receives 21, he instantly wins(returns 1),
     * if he receives less than 21, he can draw more.
     * To stop drawing, press 0(returns 0).
     * Other inputs forces user to repeat input.
     *
     * @return resolve for player move
     */
    private static int player_move() {
        int input;
        System.out.println("Ваш Ход");
        System.out.println("-------");
        do {
            System.out.println("Введите “1”, чтобы взять карту, и “0”, чтобы остановиться...");
            input = working_input();
            if (input == 0) { break; }
            Deck.draw_cards(player);
            System.out.println("Вы открыли карту " + player.get(player.size() - 1));
            int res = calculate_points(player);
            get_round_info(closed_card);
            if (res != 0) {
                return res;
            }
        } while (true);
        return input;
    }

    /**
     * Dealer opens his closed card, and draws until more 17.
     * If more than 21, dealer loses(return -1),
     * if dealer draws 21, he wins instantly(return 1),
     * else return 0.
     *
     * @return dealer resolve.
     */
    private static int dealer_move() {
        closed_card = false;
        System.out.println("Ход диллера");
        System.out.println("-------");
        System.out.println("Диллер открывает закрытую карту " + dealer.get(dealer.size() - 1));
        get_round_info(closed_card);
        while (dealer.get(0).summary < 17) {
            Deck.draw_cards(dealer);
            System.out.println("Диллер открыл карту " + dealer.get(dealer.size() - 1));
            get_round_info(closed_card);
        }
        return calculate_points(dealer);
    }

    /**
     * receives input, and checks,
     * whether it`s correct.
     *
     * @return input data.
     */
    private static int working_input() {
        Scanner in = new Scanner(System.in);
        int input = in.nextInt();
        while (input != 0 && input != 1) {
            System.out.println("Неправильный ввод, повторите еще раз!");
            input = in.nextInt();
        }
        return input;
    }

    /**
     * counts points of a player,
     * resolves situation of a game,
     * and handles aces.
     *
     * @param player deck of a player.
     * @return 1 - BlackJack, -1 - overflow, 0 - else.
     */
    @SuppressWarnings("ReassignedVariable")
    private static int calculate_points(ArrayList<Card> player) {
        int sum = Deck.get_points(player);
        while (sum > 21) {
            if (!player.get(0).aces.isEmpty()) {
                player.get(player.get(0).aces.get(0)).value = 1;
                player.get(0).aces.remove(0);
                player.get(0).summary -= 10;
                sum = Deck.get_points((player));
            } else {
                return -1;
            }
        }

        if (sum == 21) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * prints points of players,
     * depending on the situation in game.
     *
     * @param closed_card - flag whether to show closed dealer kard, or hide.
     */
    private static void get_round_info(boolean closed_card) {
        System.out.println("Карты игрока: " + player.toString() + " => " + Deck.get_points(player));
        if (closed_card) {
            System.out.println("Карты Дилера: " + "[" + dealer.get(0).toString() + ", <закрытая карта>]\n");
        } else {
            System.out.println("Карты Дилера: " + dealer.toString() + " => " + Deck.get_points(dealer) + "\n");
        }
    }
}

/**
 * class that represents deck.
 */
class Deck {
    static private ArrayList<Card> deck;
    static private int deck_card_index;

    /**
     * creating big deck from which we draw cards.
     */
    public static void create_big_deck() {
        deck = new ArrayList<>();
        for(Mast mast: Mast.values()) {
            for (Kards kards : Kards.values()) {
                deck.add(new Card(mast, kards));
            }
        }
        deck_card_index = 0;
    }

    /**
     * Shuffles deck.
     */
    public static void shuffle_deck() {
        Collections.shuffle(deck);
        deck_card_index = 0;
    }

    /**
     * We draw card,
     * save summary of cards in the first elem of deck,
     * and store in first element where aces located.
     *
     * @param player deck where we draw cards.
     */
    public static void draw_cards(ArrayList<Card> player) {
        player.add(deck.get(deck_card_index++));
        if (player.get(player.size() - 1).value == 11) {
            player.get(0).aces.add(player.size() - 1);
        }
        player.get(0).summary += player.get(player.size() - 1).value;
    }

    /**
     * We create players deck.
     * We also store the summary and aces pos in 1 elem.
     *
     * @param player deck which we create.
     */
    public static void create_small_deck(ArrayList<Card> player) {
        player.add(deck.get(deck_card_index++));
        if (player.get(player.size() - 1).value == 11) {
            player.get(0).aces.add(player.size()-1);
        }
        player.add(deck.get(deck_card_index++));
        if (player.get(player.size() - 1).value == 11) {
            player.get(0).aces.add(player.size()-1);
        }
        player.get(0).summary += player.get(player.size() - 1).value;
    }

    /**
     * @param player deck which points we get.
     * @return points of a player.
     */
    public static int get_points(ArrayList<Card> player) {
        return player.get(0).summary;
    }
}

/**
 * Class for Card representation,
 * has Name, value and Mast`,
 * also we have fields for summary and aces list,
 * where we save them in other methods.
 */
class Card {
    String Mast;
    String Name;
    int value;
    int summary;
    ArrayList<Integer> aces;

    /**
     * Constructor for class.
     *
     * @param mast mast` of card.
     * @param kards - card where we have name and value.
     */
    Card(Mast mast, Kards kards) {
        this.Mast = mast.name;
        this.Name = kards.name;
        this.value = kards.value;
        this.summary = this.value;
        this.aces = new ArrayList<>();
    }

    /**
     * @return string card representation.
     */
    public String toString() {
        return this.Mast + " " + this.Name + " (" + this.value + ")";
    }
}

/**
 * An enum for Masti of cards.
 */
enum Mast {
    SPADES("Пики"),
    HEARTS("Черви"),
    BUBNA("Буби"),
    KRESTY("Крести");

    final String name;

    /**
     * Constructor
     *
     * @param name - mast` of card.
     */
    Mast(String name) {
        this.name = name;
    }

    /**
     * @return string representation of mast`.
     */
    public String toString() {
        return name;
    }
}

/**
 * An enum for Cards representation.
 */
enum Kards {
    ACE("Туз", 11),
    KING("Король", 10),
    QUEEN("Королева", 10),
    VALET("Валет", 10),
    TEN("Десять", 10),
    NINE("Девять", 9),
    EIGHT("Восемь", 8),
    SEVEN("Семь", 7),
    SIX("Шесть", 6),
    FIVE("Пять", 5),
    FOUR("Четыре", 4),
    THREE("Три", 3),
    TWO("Два", 2);

    final String name;
    final int value;

    /**
     * Constructor for Cards.
     *
     * @param name - name of card.
     * @param value - score of a card.
     */
    Kards(String name,int value) {
        this.value = value;
        this.name = name;
    }

    /**
     * @return string representation of cards.
     */
    public String toString() {
        return name;
    }
}