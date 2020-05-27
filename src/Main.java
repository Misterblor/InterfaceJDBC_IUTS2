import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Main implements ActionListener {

    JFrame frame = new JFrame("mor");
    JButton button = new JButton("mor");
    public Main(){
        frame.setSize(800,800);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.add(button);
        button.addActionListener(this);
    }
    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        new Main();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button){
            try {
                Connection connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
                System.out.println("Bravo, vous etes connecté, mais ca ne change en rien le fait que tu soit le roi des fdp");
            } catch (SQLException exp){
                exp.printStackTrace();
            }
        }
    }
}
