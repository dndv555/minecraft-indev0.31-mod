import net.minecraft.client.Minecraft;
import net.minecraft.client.MinecraftApplet;
import net.minecraft.client.Session;
import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;
public class Start {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
                MinecraftApplet e = new MinecraftApplet();
                JFrame a = new JFrame("Minecraft 0.31");
                a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                Canvas b = new Canvas();
                b.setPreferredSize(new Dimension(854, 480));
                a.getContentPane().add(b);
                a.pack();
                a.setLocationRelativeTo(null);
                a.setVisible(true);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
            }
                Minecraft c = new Minecraft(b, e, 854, 480, false);
                Thread d = new Thread(c, "Minecraft 0.31");
                d.start();
                c.session = new Session("Player", "");       
        });
    }
}