import javax.swing.*;
import java.awt.*;

public class Test {
    public Test(){
        JFrame frame = new JFrame("Tjenare mannen!..");
        frame.setSize(440,100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        
        
        JLabel message = new JLabel("What is your name?");
        JTextField textbox = new JTextField(10);

        JButton button = new JButton("Click me!");

        button.addActionListener(e -> {
            String input = textbox.getText();
            JOptionPane.showMessageDialog(frame, "Hello: " + input);
        });

        
        frame.add(textbox);
        frame.add(button);
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        new Test();
    }
    
}
