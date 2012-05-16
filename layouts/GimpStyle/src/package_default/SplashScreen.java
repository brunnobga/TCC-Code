/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package package_default;

import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

/**
 *
 * @author caio
 */
public class SplashScreen extends javax.swing.JWindow implements Runnable{
    private AbsoluteLayout abs;
    private AbsoluteConstraints abi;
    private ImageIcon img;
    private JLabel jLabel;
    private int delay;
    
    public SplashScreen(int delay){
        this.delay = delay;
        abs = new AbsoluteLayout();
        abi = new AbsoluteConstraints(0, 0);
        jLabel = new JLabel();
        img = new ImageIcon(this.getClass().getResource("/package_default/imgs/sasqvsplash1final3.png"));
        jLabel.setIcon(img);
        this.getContentPane().setLayout(abs);
        this.getContentPane().add(jLabel, abi);
        this.pack();
    }
    
    @Override
    public void run(){
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException ex) {
        }
        this.setVisible(false);
    }
}
