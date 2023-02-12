package assign11;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * This class represents a GUI component for displaying an image. This class has
 * been updated to contain an additional method to paint a transparent rectangle
 * over the given coordinates with the given size
 * 
 * @author Prof. Parker & Aidan Barrett
 * @version December 6th, 2022
 */
public class ImagePanel extends JPanel {

	private BufferedImage bufferedImg;

	/**
	 * Creates a new ImagePanel to display the given image.
	 * 
	 * @param img - the given image
	 */
	public ImagePanel(Image img) {
		int rowCount = img.getNumberOfRows();
		int colCount = img.getNumberOfColumns();

		this.bufferedImg = new BufferedImage(colCount, rowCount, BufferedImage.TYPE_INT_RGB);

		for (int i = 0; i < rowCount; i++)
			for (int j = 0; j < colCount; j++)
				this.bufferedImg.setRGB(j, i, img.getPixel(i, j).getPackedRGB());

		this.setPreferredSize(new Dimension(colCount, rowCount));
	}

	/**
	 * 
	 * This method is called by the system to create a rectangle representing the
	 * crop area over the given graphics object, with the specified location and
	 * size for the rectangle.
	 * 
	 * Partially overrides the paintComponent method of JPanel and overloads the
	 * other paintComponent method in the imagePanel class
	 * 
	 * @param g      - the graphics context that the rectangle is drawn onto
	 * @param rect1X - the X value of the first mouse event
	 * @param rect1Y - the Y value of the first mouse event
	 * @param rect2X - the X value of the second mouse event
	 * @param rect2Y - the Y value of the second mouse event
	 */
	public void paintComponent(Graphics g, int rect1X, int rect1Y, int rect2X, int rect2Y) {

		super.paintComponent(g);

		g.drawImage(this.bufferedImg, 0, 0, this);

		g.setColor(new Color(105, 105, 105, 125));

		g.fillRect(rect1X, rect1Y, rect2X - rect1X, rect2Y - rect1Y);
	}

	/**
	 * This method is called by the system when a component needs to be painted.
	 * Which can be at one of three times: --when the component first appears --when
	 * the size of the component changes (including resizing by the user) --when
	 * repaint() is called
	 * 
	 * Partially overrides the paintComponent method of JPanel.
	 * 
	 * @param g -- graphics context onto which we can draw
	 */
	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		g.drawImage(this.bufferedImg, 0, 0, this);

	}

	// Required by a serializable class (ignore for now)
	private static final long serialVersionUID = 1L;
}