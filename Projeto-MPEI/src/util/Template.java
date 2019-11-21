package util;

import javax.swing.*;

public class Template extends JFrame {
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JButton button5;
    private JButton button6;
    private JButton button7;
    private JButton button8;
    private JButton button9;
    private JComboBox comboBox1;
    private JComboBox comboBox2;

    public Template(String title) {

        add(comboBox1);
        add(comboBox2);
        add(button1);
        add(button2);
        setTitle(title);
        setVisible(true);
        setSize(500,500);
    }

    public static void main(String args[]) {
        JFrame frame = new Template("Projeto final de MPEI");
        /*JFrame a = new JFrame("Teste Swing");
        a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        a.setVisible(true);*/
    }



}


