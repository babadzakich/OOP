package ru.nsu.chuvashov.bj;

import static java.lang.Math.abs;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class for main logic behind the game.
 */
public class Game {

    private static int round;
    private static int playerScore;
    private static int dealerScore;
    private static boolean closedCard;
    static ArrayList<Card> player = new ArrayList<>();
    static ArrayList<Card> dealer = new ArrayList<>();

    /**
     * Constructor for GAME class.
     */
    public Game() {
        System.out.println("Добро пожаловать в Блэкджек!");
        round = 1;
        playerScore = dealerScore = 0;
        Deck.createBigDeck();
    }

    /**
     * Starter of game.
     */
    public void blackJack(Boolean flag) {
        Game.round(flag);
    }

    /**
     * Round logic with start, player move, dealer move and resolve.
     */
    private static void round(boolean flag) {
        int resultOfGame;
        while (abs(playerScore - dealerScore) < 3) {
            resultOfGame = roundStart();
            if (resultOfGame > 0) {
                System.out.println("Победили Русские!!!!!!\n");
                playerScore++;
                endRound();
                continue;
            }
            if (flag) {
                resultOfGame = realPlayerMove();
            } else {
                resultOfGame = computerPlayerMove();
            }


            if (resultOfGame > 0) {
                System.out.println("Игрок набрал Блэкджек!\n");
                playerScore++;
                endRound();
                continue;
            } else if (resultOfGame < 0) {
                System.out.println("Перебор, Дилер победил\n");
                dealerScore++;
                endRound();
                continue;
            }
            resultOfGame = dealerMove();
            if (resultOfGame < 0) {
                System.out.println("Перебор, победил игрок\n");
                playerScore++;
                endRound();
            } else if (resultOfGame > 0) {
                System.out.println("Диллер собрал Блэкджек, он победил!\n");
                dealerScore++;
                endRound();
            } else {
                if (Deck.getPoints(player) < Deck.getPoints(dealer)) {
                    System.out.println("Диллер набрал больше, он победил!\n");
                    dealerScore++;
                    endRound();
                } else if (Deck.getPoints(player) > Deck.getPoints(dealer)) {
                    System.out.println("Игрок набрал больше, он победил!\n");
                    playerScore++;
                    endRound();
                } else {
                    System.out.println("Игрок и Диллер набрали одинаково, получилась ничья!\n");
                    endRound();
                }
            }
        }
        if (playerScore > dealerScore) {
            System.out.println("Игрок победил, казино в проигрыше!");
        } else {
            System.out.println("Диллер выиграл, Казино в выигрыше!");
        }
    }

    /**
     * ending of round.
     * clears all player decks, shows score and increments round.
     */
    private static void endRound() {
        player.clear();
        dealer.clear();
        round++;
        System.out.println("Cчет " + playerScore + " : " + dealerScore);
    }

    /**
     * Starts round, shuffles deck and gives cards to player.
     *
     * @return points that acquired player(we could get 21 instantly).
     */
    private static int roundStart() {
        Deck.shuffleDeck();
        System.out.println("Раунд " + round + "\nДиллер раздал карты");
        Deck.createSmallDeck(player);
        Deck.createSmallDeck(dealer);
        closedCard = true;
        getRoundInfo(true);
        return calculatePoints(player);
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
    private static int realPlayerMove() {
        int input;
        System.out.println("Ваш Ход");
        System.out.println("-------");
        do {
            System.out.println("Введите “1”, чтобы взять карту, и “0”, чтобы остановиться...");
            input = working_input();
            if (input == 0) {
                break;
            }
            Deck.draw_cards(player);
            System.out.println("Вы открыли карту " + player.get(player.size() - 1));
            int res = calculatePoints(player);
            getRoundInfo(closedCard);
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
    private static int dealerMove() {
        closedCard = false;
        System.out.println("Ход диллера");
        System.out.println("-------");
        System.out.println("Диллер открывает закрытую карту " + dealer.get(dealer.size() - 1));
        getRoundInfo(closedCard);
        while (dealer.get(0).summary < 17) {
            Deck.draw_cards(dealer);
            System.out.println("Диллер открыл карту " + dealer.get(dealer.size() - 1));
            getRoundInfo(closedCard);
        }
        return calculatePoints(dealer);
    }

    private static int computerPlayerMove() {
        System.out.println("Ваш ход");
        System.out.println("-------");
        getRoundInfo(closedCard);
        while (player.get(0).summary < 18) {
            Deck.draw_cards(player);
            System.out.println("Вы открыли карту " + player.get(player.size() - 1));
            getRoundInfo(closedCard);
        }
        return calculatePoints(player);
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
    private static int calculatePoints(ArrayList<Card> player) {
        int sum = Deck.getPoints(player);
        while (sum > 21) {
            if (!player.get(0).aces.isEmpty()) {
                player.get(player.get(0).aces.get(0)).value = 1;
                player.get(0).aces.remove(0);
                player.get(0).summary -= 10;
                sum = Deck.getPoints((player));
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
     * @param closedCard - flag whether to show closed dealer kard, or hide.
     */
    private static void getRoundInfo(boolean closedCard) {
        System.out.println("Карты игрока: " + player.toString() + " => " + Deck.getPoints(player));
        if (closedCard) {
            System.out.println("Карты Дилера: [" + dealer.get(0).toString() + ", <закрыто>]\n");
        } else {
            System.out.println("Карты Дилера: " + dealer.toString()
                    + " => " + Deck.getPoints(dealer) + "\n");
        }
    }
}