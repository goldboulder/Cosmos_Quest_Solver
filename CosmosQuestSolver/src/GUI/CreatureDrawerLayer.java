/*

 */
package GUI;

import Formations.Creature;
import java.awt.AWTEvent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JLayer;
import javax.swing.SwingUtilities;
import javax.swing.plaf.LayerUI;


public class CreatureDrawerLayer extends LayerUI{
        
        private CreatureDragFrame frame;
        private int mouseX = 0;
        private int mouseY = 0;
        
        public CreatureDrawerLayer(CreatureDragFrame frame){
            super();
            this.frame = frame;
        }
        
        @Override
        public void paint(Graphics g, JComponent c) {
            super.paint(g, c);

            Graphics2D g2 = (Graphics2D) g.create();
            Creature mouseCreature = frame.getMouseCreature();
            if (mouseCreature != null){
                g2.translate(mouseX - AssetPanel.CREATURE_PICTURE_SIZE/2, mouseY - AssetPanel.CREATURE_PICTURE_SIZE/2);
                mouseCreature.draw(g2);
                g2.translate(-mouseX + AssetPanel.CREATURE_PICTURE_SIZE/2, -mouseY + AssetPanel.CREATURE_PICTURE_SIZE/2);
            }
            
        }
        
        @Override
        protected void processMouseEvent(MouseEvent e, JLayer l) {
            l.repaint();
        }
        
        @Override
        protected void processKeyEvent(KeyEvent e, JLayer l){
            l.repaint();
        }
        
        @Override
        protected void processMouseMotionEvent(MouseEvent e, JLayer l) {
            Point p = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), l);
            mouseX = p.x;
            mouseY = p.y;
            l.repaint();
        }
        
        //without these two method, the picure glitched and only rendered in the formation panels
        @Override
        public void installUI(JComponent c) {
            super.installUI(c);
            JLayer jlayer = (JLayer)c;
            jlayer.setLayerEventMask(AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
        }

        @Override
        public void uninstallUI(JComponent c) {
            JLayer jlayer = (JLayer)c;
            jlayer.setLayerEventMask(0);
            super.uninstallUI(c);
        }
        
    }
