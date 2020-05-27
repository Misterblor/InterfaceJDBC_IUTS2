import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.*;

public class Main implements ActionListener {

    JFrame frame = new JFrame("Base de données");
    JPanel panel = new JPanel();

    JButton boutonConnection = new JButton("Connection");
    JButton boutonCreationTable = new JButton("Créer les tables Avions et Compagnie");
    JButton boutonInsert = new JButton("Inserer");
    JButton boutonSelecte = new JButton("Select");
    JButton boutonSuppression = new JButton("Supprimer la compagnie RayanAir");

    JTable table = new JTable();
    DefaultTableModel tableModel = new DefaultTableModel();
    JScrollPane scrollPane;

    Connection connection;

    boolean estConnecte = false;

    public Main(){
        frame.setSize(800,800);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        panel.add(boutonConnection);
        panel.add(boutonCreationTable);
        panel.add(boutonInsert);
        panel.add(boutonSelecte);
        panel.add(boutonSuppression);
        frame.setContentPane(panel);
        boutonConnection.addActionListener(this);
        boutonCreationTable.addActionListener(this);
        boutonInsert.addActionListener(this);
        boutonSelecte.addActionListener(this);
        boutonSuppression.addActionListener(this);

        tableModel.setColumnCount(0);
        tableModel.setRowCount(0);
        table.setModel(tableModel);
        scrollPane = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panel.add(scrollPane);
    }
    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        new Main();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == boutonConnection && !estConnecte){
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:database" + File.separator + "database.db");
                estConnecte = true;
                System.out.println("Bravo, vous etes connecté !");
            } catch (SQLException exp){
                exp.printStackTrace();
            }
        }
        if (e.getSource() == boutonCreationTable && estConnecte){
            if (JOptionPane.showConfirmDialog(frame, "Attention, si les tables existent, elles seront détrutes\n Voulez vous continuer ?", "Attention", JOptionPane.YES_NO_OPTION) == 0){
                try {
                    Statement statement = connection.createStatement();
                    statement.execute("DROP TABLE AVION");
                    statement.execute("DROP TABLE COMPAGNIE");

                    statement.execute("CREATE TABLE AVION (Code_Avion CHAR(6), Designation_Avion CHAR (5), Code_Compagnie CHAR(6))");
                    statement.execute("CREATE TABLE COMPAGNIE (Code_Compagnie CHAR (6), Designation_Compagnie CHAR (50))");

                    ResultSet resultSet = statement.executeQuery("SELECT * FROM AVION");
                    // Affichage pour la question 5
                    System.out.println(resultSet.getMetaData().getColumnCount());
                    System.out.println(resultSet.getMetaData().getColumnName(1) + ", TYPE: " + resultSet.getMetaData().getColumnTypeName(1));
                    System.out.println(resultSet.getMetaData().getColumnName(2) + ", TYPE: " + resultSet.getMetaData().getColumnTypeName(2));
                    System.out.println(resultSet.getMetaData().getColumnName(3) + ", TYPE: " + resultSet.getMetaData().getColumnTypeName(3));

                    resultSet.close();
                    statement.close();
                    System.out.println("Les tables ont été crées avec succès");

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        if (e.getSource() == boutonInsert && estConnecte){
            try {
                Statement statement = connection.createStatement();

                statement.execute("INSERT INTO AVION VALUES ('7TRJ', 'B731', 'FR1024')");
                statement.execute("INSERT INTO AVION VALUES ('G8VC', 'B522', 'FR1024')");
                statement.execute("INSERT INTO AVION VALUES ('QS25', 'B211', 'LIB268')");

                statement.execute("INSERT INTO COMPAGNIE VALUES ('AF1024', 'Air France')");
                statement.execute("INSERT INTO COMPAGNIE VALUES ('US2020', 'US Air Ways')");
                statement.execute("INSERT INTO COMPAGNIE VALUES ('LIB268', 'Ryanair')");

                statement.close();
                System.out.println("Les tuples ont été inséré avec succès");

            } catch (SQLException throwables){
                throwables.printStackTrace();
            }
        }
        if (e.getSource() == boutonSelecte && estConnecte){
            try {
                Statement statement = connection.createStatement();

                ResultSet resultSet = statement.executeQuery("SELECT * FROM AVION");

                tableModel.setColumnCount(resultSet.getMetaData().getColumnCount());
                String[] identifier = new String[resultSet.getMetaData().getColumnCount()];
                for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                    identifier[i] = resultSet.getMetaData().getColumnName(i+1);
                }
                tableModel.setColumnIdentifiers(identifier);

                int compteur = 0;
                while (resultSet.next()){
                    compteur++;
                }
                tableModel.setRowCount(compteur);
                resultSet.close();

                resultSet = statement.executeQuery("SELECT * FROM AVION");
                compteur = 0;
                while (resultSet.next()){
                    tableModel.setValueAt(resultSet.getString(1), compteur, 0);
                    tableModel.setValueAt(resultSet.getString(2), compteur, 1);
                    tableModel.setValueAt(resultSet.getString(3), compteur, 2);
                    compteur++;
                }

                resultSet.close();
                statement.close();
                System.out.println("requete terminé !");

            } catch (SQLException throwables){
                throwables.printStackTrace();
            }
        }
        if (e.getSource() == boutonSuppression && estConnecte){
            try {
                Statement statement = connection.createStatement();

                statement.execute("DELETE FROM AVION WHERE Code_Compagnie = (SELECT Code_Compagnie FROM COMPAGNIE WHERE Designation_Compagnie = 'Ryanair')");
                statement.execute("DELETE FROM COMPAGNIE WHERE Designation_Compagnie = 'Ryanair'");

                statement.close();
                System.out.println("Suppression effectuée avec succès !");

            } catch (SQLException throwables){
                throwables.printStackTrace();
            }
        }
    }
}
