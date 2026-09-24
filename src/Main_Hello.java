import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main_Hello {
    public static void main(String[] args) {
        // Run console logic first
        Hello myHello = new Hello();
        myHello.hello_world();

        // Safely launch the GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Magikarp Simulator"); // Updated Title
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 400); // Increased height to fit the UI
            
            // Instantiate the separate graphics class (The Canvas)
            GraphicsExample myGraphicsExample = new GraphicsExample();
            
            // Add the panel to the frame
            frame.add(myGraphicsExample); 
            
            frame.setLocationRelativeTo(null); // Center window on screen
            frame.setVisible(true);
        }); 
    }
}