import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;

public class TetrisInterface extends JPanel {
    
    //Created length and width of the canvas
    private static final int LOGICAL_WIDTH = 700;
    private static final int LOGICAL_HEIGHT = 700;

    //Define interactive areas using logical coordinates
    private final Rectangle mainArea = new Rectangle(50, 50, 300, 550);
    private final Rectangle quitButton = new Rectangle(400, 530, 120, 50);

    private boolean isPaused = false;

    public TetrisInterface() {
        // Handle Quit button clicks
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point p = getLogicalPoint(e.getPoint());
                if (quitButton.contains(p)) {
                    System.exit(0); 
                }
            }
        });

        //Mouse Hover for pause state
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point p = getLogicalPoint(e.getPoint());
                boolean currentlyInMainArea = mainArea.contains(p);
                
                // Only repaint if the state actually changes to save resources
                if (isPaused != currentlyInMainArea) {
                    isPaused = currentlyInMainArea;
                    repaint(); 
                }
            }
        });
    }

    //Converts interactivity to our scale
    private Point getLogicalPoint(Point screenPoint) {
        double scale = getScaleFactor();
        int xOffset = (getWidth() - (int)(LOGICAL_WIDTH * scale)) / 2;
        int yOffset = (getHeight() - (int)(LOGICAL_HEIGHT * scale)) / 2;

        int logicalX = (int)((screenPoint.x - xOffset) / scale);
        int logicalY = (int)((screenPoint.y - yOffset) / scale);
        return new Point(logicalX, logicalY);
    }

    //Calculates scale factor
    private double getScaleFactor() {
        double scaleX = (double) getWidth() / LOGICAL_WIDTH;
        double scaleY = (double) getHeight() / LOGICAL_HEIGHT;
        return Math.min(scaleX, scaleY); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Enable anti-aliasing for smoother lines and text
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Apply Scaling and Centering
        double scale = getScaleFactor();
        int xOffset = (getWidth() - (int)(LOGICAL_WIDTH * scale)) / 2;
        int yOffset = (getHeight() - (int)(LOGICAL_HEIGHT * scale)) / 2;

        AffineTransform oldTransform = g2d.getTransform();
        g2d.translate(xOffset, yOffset);
        g2d.scale(scale, scale);


        // Drawing main area
        g2d.setColor(Color.WHITE);
        g2d.fill(mainArea);
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.draw(mainArea);

        //Next shape pannel
        Rectangle nextShapeBox = new Rectangle(400, 50, 150, 100);
        g2d.draw(nextShapeBox);
        g2d.setFont(new Font("SansSerif", Font.ITALIC | Font.BOLD, 14));
        g2d.drawString("Next shape", 560, 80);

        // Statistics text
        g2d.setFont(new Font("SansSerif", Font.BOLD, 22));
        g2d.drawString("Level:     1", 400, 230);
        g2d.drawString("Lines:     0", 400, 300);
        g2d.drawString("Score:     0", 400, 370);

        //Quit button
        g2d.setColor(Color.WHITE);
        g2d.fill(quitButton);
        g2d.setColor(Color.BLACK);
        g2d.draw(quitButton);
        g2d.drawString("QUIT", 432, 563);

        //Blocks
        draw_tetris(g2d, 150, 90, new Color(76, 175, 80), new int[][]{{0,0}, {1,0}, {0,1}, {1,1}}); // Green O
        draw_tetris(g2d, 430, 70, new Color(244, 67, 54), new int[][]{{1,0}, {0,1}, {1,1}, {2,1}}); // Red T
        
        // blue bottom block
        draw_tetris(g2d, 325, 600, new Color(33, 150, 243), new int[][]{{0,-1}, {0,-2}, {0,-3}, {-1,-1}}); // Blue J
        //yellow bottom block
        draw_tetris(g2d, 220, 600, new Color(255, 235, 59), new int[][]{{-1,-1}, {0,-1}, {1,-1}, {1,-2}}); // Yellow L

        //Pause menu 
        if (isPaused) {
            g2d.setColor(Color.white);
            Rectangle pauseBox = new Rectangle(100, 250, 200, 50);
            g2d.fill(pauseBox);
            g2d.setColor(new Color(66, 103, 178)); // Blue border and text
            g2d.draw(pauseBox);
            g2d.setFont(new Font("SansSerif", Font.BOLD, 32));
            g2d.drawString("PAUSE", 145, 287);
        }
        g2d.setTransform(oldTransform);
    }


    private void draw_tetris(Graphics2D g2d, int startX, int startY, Color c, int[][] gridCoords) {
        int blockSize = 25;
        for (int[] coord : gridCoords) {
            int x = startX + (coord[0] * blockSize);
            int y = startY + (coord[1] * blockSize);
            g2d.setColor(c);
            g2d.fillRect(x, y, blockSize, blockSize);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(x, y, blockSize, blockSize);
        }
    }
//Main method to call everything
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Tetris Interface");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 850); 
            frame.add(new TetrisInterface());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}



