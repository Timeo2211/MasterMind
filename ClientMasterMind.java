import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientMasterMind {
    private static final String SERVER_IP = "localhost";
    private static final int PORT = 12345;
    private static final int DIGIT_COUNT = 6;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_IP, PORT);
             InputStream in = socket.getInputStream();
             OutputStream out = socket.getOutputStream()) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Bienvenue dans le jeu MasterMind!");
            System.out.println("Le but du jeu est de deviner un nombre de " + DIGIT_COUNT + " chiffres.");
            for (int attempt = 1; attempt <= 10; attempt++) {
                System.out.print("Entrer votre proposition:");
                String guess = scanner.nextLine();

                if (guess.length() != DIGIT_COUNT) {
                    System.out.println("Invalide input. Le nombre doit avoir " + DIGIT_COUNT + " chiffres.");
                    attempt--;
                    continue;
                }
                out.write(guess.getBytes());
                byte[] buffer = new byte[DIGIT_COUNT];
                in.read(buffer);
                String feedback = new String(buffer);
                System.out.println("Retour: " + feedback);
                if (feedback.equals("Vous avez gagne!")) {
                    System.out.println("Félicitations! Vous avez gagné!");
                    break;
                }
            }
            System.out.println("Fin de la a partie. Au revoir!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}