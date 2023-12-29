
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class ServerMasterMind {
    private static final int PORT = 12345;
    private static final int DIGIT_COUNT = 6;
    private static final int MAX_TRIES = 10;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Le serveur est démarré sur le port " + PORT+ "...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Le client est connecté : " + clientSocket.getInetAddress());
                String secretNumber = generateSecretNumber();
                try (InputStream in = clientSocket.getInputStream();
                     OutputStream out = clientSocket.getOutputStream()) {
                    for (int attempt = 1; attempt <= MAX_TRIES; attempt++) {
                        byte[] buffer = new byte[DIGIT_COUNT];
                        in.read(buffer);
                        String clientGuess = new String(buffer);
                        String feedback = checkGuess(secretNumber, clientGuess);
                        out.write(feedback.getBytes());
                        if (feedback.equals("Vous avez gagne !")) {
                            System.out.println("Client " + clientSocket.getInetAddress() + " gagne!");
                            break;
                        }
                    }
                    System.out.println("Fin de la partie avec le client " + clientSocket.getInetAddress() + ".");
                    clientSocket.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String generateSecretNumber() {
        Random random = new Random();
        StringBuilder secretNumber = new StringBuilder();
        for (int i = 0; i < DIGIT_COUNT; i++) {
            secretNumber.append(random.nextInt(10));
        }
        return secretNumber.toString();
    }

    private static String checkGuess(String secretNumber, String guess) {
        StringBuilder feedback = new StringBuilder();
        for (int i = 0; i < DIGIT_COUNT; i++) {
            if (guess.charAt(i) == secretNumber.charAt(i)) {
                feedback.append(secretNumber.charAt(i));
            } else if (secretNumber.contains(String.valueOf(guess.charAt(i)))) {
                feedback.append("X");
            } else {
                feedback.append("-");
            }
        }
        if (feedback.toString().equals(secretNumber)) {
            return "Vous avez gagne !";
        }
        return feedback.toString();
    }
}