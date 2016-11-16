import java.awt.*;

/**
 * Created by joel on 11/16/16.
 */
public class Main {
    /* main accepts a single argument: a directory to parse
     * If there is no directory supplied, a gui will be presented.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Controller controller;
                if(args.length > 0) controller = new Controller(args[0]);
                else controller = new Controller();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
