package ru.nsu.chuvashov.bj;

import static java.lang.Math.abs;

import java.util.Scanner;

/**
 * Class for main logic behind the game.
 */
public class Game {

    private final int endGame = 228;
    private int round;
    int playerScore;
    int dealerScore;
    private boolean closedCard;
    private PlayerDeck player;
    private PlayerDeck dealer;
    Deck bigDeck;

    /**
     * Constructor for GAME class.
     */
    public Game() {
        System.out.println("Добро пожаловать в Блэкджек!");
        bigDeck = Deck.getInstance();
    }

    /**
     * Starter of game.
     */
    public void blackJack(Boolean flag) {
        round = 1;
        dealerScore = playerScore = 0;
        round(flag);
    }

    /**
     * Round logic with start, player move, dealer move and resolve.
     *
     * @param flag - shows if player is human or pc.
     */
    private void round(boolean flag) {
        int resultOfGame = 0;
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
                if (resultOfGame == endGame) {
                    break;
                }
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
                if (player.getPoints() < dealer.getPoints()) {
                    System.out.println("Диллер набрал больше, он победил!\n");
                    dealerScore++;
                    endRound();
                } else if (player.getPoints() > player.getPoints()) {
                    System.out.println("Игрок набрал больше, он победил!\n");
                    playerScore++;
                    endRound();
                } else {
                    System.out.println("Игрок и Диллер набрали одинаково, получилась ничья!\n");
                    endRound();
                }
            }
        }
        if (resultOfGame == endGame) {
            System.out.println("Игра была прервана пользователем с помощью магической силы");
        } else {
            if (playerScore > dealerScore) {
                System.out.println("Игрок победил, казино в проигрыше!");
            } else {
                System.out.println("Диллер выиграл, Казино в выигрыше!");
            }
        }
    }

    /**
     * ending of round.
     * clears all player decks, shows score and increments round.
     */
    private void endRound() {
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
    private int roundStart() {
        System.out.println("Раунд " + round + "\nДиллер раздал карты");
        player = new PlayerDeck();
        dealer = new PlayerDeck();
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
    private int realPlayerMove() {
        int input;
        System.out.println("Ваш Ход");
        System.out.println("-------");
        do {
            System.out.println("Введите “1”, чтобы взять карту, и “0”, чтобы остановиться...");
            input = workingInput();
            if (input == endGame) {
                return input;
            }
            if (input == 0) {
                break;
            }
            player.drawCards();
            System.out.println("Вы открыли карту " + player.getLast());
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
    private int dealerMove() {
        closedCard = false;
        System.out.println("Ход диллера");
        System.out.println("-------");
        System.out.println("Диллер открывает закрытую карту " + dealer.getLast());
        getRoundInfo(closedCard);
        while (dealer.getPoints() < 17) {
            dealer.drawCards();
            System.out.println("Диллер открыл карту " + dealer.getLast());
            getRoundInfo(closedCard);
        }
        return calculatePoints(dealer);
    }

    /**
     * Implementation of player decision,
     * for testing purposes.
     *
     * @return player resolve.
     */
    private int computerPlayerMove() {
        System.out.println("Ваш ход");
        System.out.println("-------");
        getRoundInfo(closedCard);
        while (player.getPoints() < 18) {
            player.drawCards();
            System.out.println("Вы открыли карту " + player.getLast());
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
    private int workingInput() {
        Scanner in = new Scanner(System.in);
        int input = in.nextInt();
        if (input == 228) {
            return input;
        }
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
    private int calculatePoints(PlayerDeck player) {
        int sum = player.getPoints();
        while (sum > 21) {
            if (player.aceChecker()) {
                sum = player.getPoints();
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
    private void getRoundInfo(boolean closedCard) {
        System.out.println("Карты игрока: " + player.toString() + " => " + player.getPoints());
        if (closedCard) {
            System.out.println("Карты Дилера: [" + dealer.getFirst().toString() + ", <закрыто>]\n");
        } else {
            System.out.println("Карты Дилера: " + dealer.toString()
                    + " => " + dealer.getPoints() + "\n");
        }
    }
}