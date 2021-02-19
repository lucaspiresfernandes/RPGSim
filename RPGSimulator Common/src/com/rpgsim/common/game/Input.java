package com.rpgsim.common.game;

import com.rpgsim.common.Vector2;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class Input implements KeyListener, MouseListener, MouseMotionListener, WindowFocusListener
{
    private static final ArrayList<Integer> pressedKeys = new ArrayList<>();
    private static final ArrayList<Integer> heldKeys = new ArrayList<>();
    
    private static final ArrayList<Integer> pressedButtons = new ArrayList<>();
    private static final ArrayList<Integer> heldButtons = new ArrayList<>();
    
    private static int mouseX, mouseY;
    
    private static boolean mouseMoved = false;
    private static boolean mouseDragging = false;
    
    private static AffineTransform screenTransform;

    public Input(AffineTransform screenTransform)
    {
        Input.screenTransform = screenTransform;
    }
    
    public static boolean getKey(KeyCode code)
    {
        return heldKeys.contains(code.getCode());
    }
    
    public static boolean getKeyDown(KeyCode code)
    {
        return pressedKeys.contains(code.getCode()) && !heldKeys.contains(code.getCode());
    }
    
    public static boolean getKeyUp(KeyCode code)
    {
        return !pressedKeys.contains(code.getCode()) && heldKeys.contains(code.getCode());
    }
    
    public static boolean getMouseButton(int code)
    {
        return heldButtons.contains(code);
    }
    
    public static boolean getMouseButtonDown(int code)
    {
        return pressedButtons.contains(code) && !heldButtons.contains(code);
    }
    
    public static boolean getMouseButtonUp(int code)
    {
        return !pressedButtons.contains(code) && heldButtons.contains(code);
    }
    
    public static boolean mouseMoved()
    {
        return mouseMoved;
    }
    
    public static boolean mouseDragged()
    {
        return mouseDragging && mouseMoved;
    }
    
    public static Vector2 mousePosition()
    {
        return new Vector2(mouseX - screenTransform.getTranslateX(), mouseY - screenTransform.getTranslateY());
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e)
    {
        if (!pressedKeys.contains(e.getKeyCode()))
            pressedKeys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        pressedKeys.remove(new Integer(e.getKeyCode()));
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e)
    {
        if (!pressedButtons.contains(e.getButton()))
            pressedButtons.add(e.getButton());
        mouseDragging = true;
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        pressedButtons.remove(new Integer(e.getButton()));
        mouseDragging = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e)
    {
        mouseX = e.getX();
        mouseY = e.getY();
        mouseMoved = true;
    }

    @Override
    public void mouseMoved(MouseEvent e) 
    {
        mouseX = e.getX();
        mouseY = e.getY();
        mouseMoved = true;
    }
    
    @Override
    public void windowGainedFocus(WindowEvent e) {}

    @Override
    public void windowLostFocus(WindowEvent e)
    {
        heldButtons.clear();
        heldKeys.clear();
        mouseDragging = false;
        mouseMoved = false;
        pressedButtons.clear();
        pressedKeys.clear();
    }
    
    public void update()
    {
        heldKeys.clear();
        for (Integer pk : pressedKeys)
            heldKeys.add(pk);
        heldButtons.clear();
        for (Integer pd : pressedButtons)
            heldButtons.add(pd);
        mouseMoved = false;
    }
    
}
