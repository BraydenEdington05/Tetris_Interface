import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class Triangle_machine extends JPanel {
    private final List<Point> points = new ArrayList<>();
    private final String[] labels = {"A", "B", "C", "P"};

    public Triangle_machine() {
        setBackground(Color.WHITE);
        
        // awaits mouse clicks
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // reset method where clicking a 4th point causes the points to reset and start over
                if (points.size() == 4) {
                    points.clear();
                } else {
                    points.add(e.getPoint());
                }
                // Request the panel to redraw itself
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // anti-aliasing for smoother lines and text
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw top instruction text
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Roman", Font.ROMAN_BASELINE, 16));
        if (points.size() < 3) {
            g2.drawString("Click to place point " + labels[points.size()] + ".", 20, 30);
        } else if (points.size() == 3) {
            g2.drawString("Click to place point P.", 20, 30);
        } else {
            g2.drawString("Click anywhere to restart.", 20, 30);
        }

        // Draw the triangle edges if at least 3 points exist
        if (points.size() >= 3) {
            g2.setColor(Color.RED);
            g2.setStroke(new BasicStroke(2));
            Point a = points.get(0);
            Point b = points.get(1);
            Point c = points.get(2);
            g2.drawLine(a.x, a.y, b.x, b.y);
            g2.drawLine(b.x, b.y, c.x, c.y);
            g2.drawLine(c.x, c.y, a.x, a.y);
        }

        // Draw the individual points
        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            if (i < 3) { 
                // Draw A, B, C as black dots
                g2.setColor(Color.BLACK);
                g2.fillOval(p.x - 4, p.y - 4, 8, 8);
                g2.setFont(new Font("Arial", Font.BOLD, 14));
                g2.drawString(labels[i], p.x + 10, p.y + 10); // draws a.b.c on screen
            } else { 
                //draw red cross
                g2.setColor(Color.RED);
                g2.setStroke(new BasicStroke(2));
                int d = 6;
                g2.drawLine(p.x - d, p.y, p.x + d, p.y);
                g2.drawLine(p.x, p.y + d, p.x, p.y - d);
                g2.setFont(new Font("Arial", Font.BOLD, 14));
                g2.drawString("P", p.x + 10, p.y + 10);
            }
        }

        // If P is placed(4), calculate and draw the result
        if (points.size() == 4) {
            String result = checkPosition(points.get(0), points.get(1), points.get(2), points.get(3));
            g2.setColor(new Color(0, 0, 0)); // black for text
            g2.setFont(new Font("Roman", Font.PLAIN, 18));
            g2.drawString("Result: P lies " + result, 20, getHeight() - 30);
        }
    }

    // Calculates the 2D cross product to determine the side of a line.
    private double sign(Point p1, Point p2, Point p3) {
        return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y);
    }
    
    //Calculates the shortest distance from a point to a line segment
    private double distanceToSegment(Point p, Point v, Point w) {
        double lengthSquared = Math.pow(v.x - w.x, 2) + Math.pow(v.y - w.y, 2);
        if (lengthSquared == 0) return Math.hypot(p.x - v.x, p.y - v.y);
        
        double t = ((p.x - v.x) * (w.x - v.x) + (p.y - v.y) * (w.y - v.y)) / lengthSquared;
        t = Math.max(0, Math.min(1, t)); // Constrain to the segment
        
        double projX = v.x + t * (w.x - v.x);
        double projY = v.y + t * (w.y - v.y);
        
        return Math.hypot(p.x - projX, p.y - projY);
    }

    //Determines whether P is inside, outside, or on the edge of the user drawn traingle 
    private String checkPosition(Point a, Point b, Point c, Point p) {
        //Checks if the point is within 3 pixels of any edge 
        double tolerance = 3.0;
        if (distanceToSegment(p, a, b) <= tolerance || 
            distanceToSegment(p, b, c) <= tolerance || 
            distanceToSegment(p, c, a) <= tolerance) {
            return "on an edge of ABC";
        }
        
        //If not on the edge calculates cross products to determine if the point is inside or outside the triangle
        double d1 = sign(p, a, b);
        double d2 = sign(p, b, c);
        double d3 = sign(p, c, a);

        boolean hasNeg = (d1 < 0) || (d2 < 0) || (d3 < 0);
        boolean hasPos = (d1 > 0) || (d2 > 0) || (d3 > 0);

        if (hasNeg && hasPos) {
            return "outside ABC";
        } else {
            return "inside ABC";
        }
    }
//main method to call everything
    public static void main(String[] args) {
        // Ensure GUI creation is run on the correct Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Point in Triangle Check");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 500);
            frame.add(new Triangle_machine());
            frame.setLocationRelativeTo(null); // Centers the window
            frame.setVisible(true);
        });
    }
}