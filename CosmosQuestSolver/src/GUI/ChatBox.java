/*

 */
package GUI;

import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;


public class ChatBox extends JPanel{
    private JPanel innerPanel;
    //private LinkedList<JLabel> textLines;
    private JScrollPane scrollPane;
    
    
    public ChatBox(){//font?
        innerPanel = new JPanel();
        innerPanel.setLayout(new BoxLayout(innerPanel,BoxLayout.Y_AXIS));
        scrollPane = new JScrollPane(innerPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(4);
        add(scrollPane);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        setOpaque(false);
    }
    
    
    public void addText(String text){
        JLabel newLabel = new JLabel(text);
        //textLines.add(new JLabel(text));
        innerPanel.add(newLabel);
        revalidate();
        adjustSize();
    }
    
    public void clear(){
        innerPanel.removeAll();
        revalidate();
        adjustSize();
    }
    
    
    private void adjustSize(){//listener for manumally changing size?
        /*
        scrollPane.setPreferredSize(getPreferredSize());
        scrollPane.setMaximumSize(getMaximumSize());
        scrollPane.setMinimumSize(getMinimumSize());
        System.out.println(this.getPreferredSize());
        
        innerPanel.setPreferredSize(getPreferredSize());
        innerPanel.setMaximumSize(getMaximumSize());
        innerPanel.setMinimumSize(getMinimumSize());
*/
    }
    
    @Override
    public void setPreferredSize(Dimension d){
        super.setPreferredSize(d);
        //scrollPane.setPreferredSize(d);
        scrollPane.setPreferredSize(new Dimension(d.width,d.height - 50));// why this number?**
    }
    
    @Override
    public void setMaximumSize(Dimension d){
        super.setMaximumSize(d);
        //scrollPane.setMaximumSize(d);
        scrollPane.setMaximumSize(new Dimension(d.width,d.height - 50));
    }
    
    @Override
    public void setMinimumSize(Dimension d){
        super.setMinimumSize(d);
        //scrollPane.setMinimumSize(d);
        scrollPane.setMinimumSize(new Dimension(d.width,d.height - 50));
    }
    
}
