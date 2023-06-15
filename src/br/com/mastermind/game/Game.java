package br.com.mastermind.game;

import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Game {
  private static final Logger LOGGER = Logger.getLogger(Logger.class.getName());
  private final int[] password;
  private final int[][] attempts;
  private int currentAttempt;
  private boolean devmode;

  public Game(boolean devmode) {
    this.devmode = devmode;
    this.currentAttempt = 0;
    this.password = generatePassword();
    this.attempts = new int[10][4];
  }

  public void start() {
    LOGGER.setUseParentHandlers(false);

    ConsoleHandler handler = new ConsoleHandler();
    handler.setFormatter(new SimpleFormatter() {
      @Override
      public synchronized String format(LogRecord record) {
        return record.getMessage() + System.lineSeparator();
      }
    });

    LOGGER.addHandler(handler);

    LOGGER.info("Bem vindo ao Mastermind!");
    LOGGER.info("Tente adivinhar a senha de 4 dígitos!");
    LOGGER.info("Você tem 10 tentativas!");
    LOGGER.info("Os dígitos vão de 1 a 6!");
    LOGGER.info("Boa sorte!");
    LOGGER.info(" ");

    if (devmode) {
      LOGGER.info(String.format("A senha é: %d%d%d%d", password[0], password[1], password[2], password[3]));
    }

    Scanner scanner = new Scanner(System.in);

    while (currentAttempt < 10) {
      LOGGER.info(String.format("Digite sua tentativa %d: ", currentAttempt + 1));

      if (currentAttempt > 0) {
        LOGGER.info("Tentativas anteriores: ");
        for (int i = 0; i < currentAttempt; i++) {
          LOGGER.info(String.format("%d%d%d%d", attempts[i][0], attempts[i][1], attempts[i][2], attempts[i][3]));
        }
        LOGGER.info(" ");
      }

      String attempt = scanner.nextLine();

      this.handleAttempt(attempt);
      LOGGER.info(" ");
    }

    LOGGER.info("Você perdeu, ruim demais kkkkkjkkj");

    scanner.close();
  }

  private int[] generatePassword() {
    int[] newPassword = new int[4];

    for (int i = 0; i < 4; i++) {
      newPassword[i] = (int) (Math.random() * 6) + 1;
    }

    return newPassword;
  }

  private void handleAttempt(String attempt) {
    // if (currentAttempt >= 10) {
    //   LOGGER.info("Limite de tentativas atingido!");
    //   LOGGER.info("A senha era: ");
    //   LOGGER.info(String.format("%d%d%d%d", password[0], password[1], password[2], password[3]));
    //   return;
    // }

    if (attempt.length() != 4) {
      LOGGER.warning("A senha deve ter 4 dígitos!");
      return;
    }

    int[] attemptArray = new int[4];

    try {
      for (int i = 0; i < 4; i++) {
        attemptArray[i] = Integer.parseInt(String.valueOf(attempt.charAt(i)));
      }
    } catch (NumberFormatException e) {
      LOGGER.warning("A senha deve conter apenas números!");
      return;
    }

    attempts[currentAttempt] = attemptArray;
    currentAttempt++;

    this.checkAttempt(attemptArray);
  }

  private void checkAttempt(int[] attempt) {
    int digitosCorretos = 0;
    int digitosDeslocados = 0;

    for (int i = 0; i < 4; i++) {
      if (attempt[i] == password[i]) {
        digitosCorretos++;
      } else if (containsDigit(password, attempt[i])) {
        digitosDeslocados++;
      }
    }

    if (digitosCorretos == 4) {
      LOGGER.info("Você acertou a senha!");
      this.end();
    } else {
      LOGGER.info(String.format("Digitos corretos: %d", digitosCorretos));
      LOGGER.info(String.format("Digitos deslocados: %d", digitosDeslocados));
    }
  }

  private boolean containsDigit(int[] array, int digit) {
    for (int i = 0; i < 4; i++) {
      if (array[i] == digit) {
        return true;
      }
    }
    return false;
  }

  private void end() {
    System.exit(0);
  }
}
