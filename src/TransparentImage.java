

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.event.*;

public class TransparentImage extends JPanel
{
  private BufferedImage backImage, frontImage;
  private float alpha = 0.5f;

  public TransparentImage(String backPath, String frontPath)
  {
    try
    {
      backImage = ImageIO.read(new File(backPath) );
      frontImage = ImageIO.read(new File(frontPath) );
    }
    catch(Exception e)
    {
      System.out.println(e);
    }
  }



  @Override
  public Dimension getPreferredSize()
  {
    return new Dimension(backImage.getWidth(), backImage.getHeight());
  }

  public void setAlpha(float alpha)
  {
    this.alpha = alpha;
    repaint();
  }

  @Override
  protected void paintComponent(Graphics g)
  {
    super.paintComponent(g);

    //  Paint background image

    Graphics2D g2 = (Graphics2D) g;
    int x = (getWidth() - backImage.getWidth())/2;
    int y = (getHeight()- backImage.getHeight())/2;
    g2.drawRenderedImage(backImage, AffineTransform.getTranslateInstance(x, y));

    //  Paint foreground image with appropriate alpha value

    Composite old = g2.getComposite();
    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
    x = (getWidth() - frontImage.getWidth())/2;
    y = (getHeight()- frontImage.getHeight())/2;
    g2.drawRenderedImage(frontImage, AffineTransform.getTranslateInstance(x, y));
    g2.setComposite(old);
  }

  public static TransparentImage createAndShowUI()
  {
    final TransparentImage green = new TransparentImage("D:\\Projects\\Java\\Leap2\\src\\hackertainedred.jpg", "D:\\Projects\\Java\\Leap2\\src\\hackertained.jpg");


    JFrame frame = new JFrame("Hackertained");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(green);
    frame.setSize(green.getPreferredSize());
    frame.setLocationByPlatform( true );
    frame.pack();
    frame.setVisible( true );
    return green;
  }
}