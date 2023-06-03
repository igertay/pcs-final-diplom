import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Request {

    public static void main(String[] args) {

        try (Socket socket = new Socket(Main.HOST, Main.PORT);
             PrintWriter in = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader out = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println(out.readLine());
            in.println(new Scanner(System.in)
                    .nextLine());
            StringBuilder stringBuilder = new StringBuilder();
            String result;

            while ((result = out.readLine()) != null) {

                stringBuilder.append(result);
                System.out.println(result);
            }

        } catch (IOException e) {
            System.out.println("Клиент не запускается");
            e.printStackTrace();
        }
    }
}


