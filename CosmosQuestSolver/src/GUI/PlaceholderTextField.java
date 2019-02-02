/*

 */
package GUI;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javafx.scene.paint.Color;
import javax.swing.JTextField;


public class PlaceholderTextField extends JTextField{
    
    private Color defaultColor = Color.GRAY;
    private String placeholder;
    
    public PlaceholderTextField(String placeholder){
        super();
        this.placeholder = placeholder;
    }
    
    @Override
    protected void paintComponent(Graphics pG) {
        super.paintComponent(pG);

        if (placeholder == null || placeholder.length() == 0 || getText().length() > 0) {
            return;
        }

        Graphics2D g = (Graphics2D) pG;
        g.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(getDisabledTextColor());
        g.drawString(placeholder, getInsets().left, pG.getFontMetrics()
            .getMaxAscent() + getInsets().top);
    }
    
    public String getPlaceholder (String s){
        return this.placeholder;
    }

    public void setPlaceholder(String s) {
        placeholder = s;
    }
    
}
