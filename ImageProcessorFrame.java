package assign11;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.filechooser.FileNameExtensionFilter;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * 
 * This class creates the GUI for the Image Processor Program using the GUI
 * components to execute methods contained in the Image class and alter an image
 * loaded using the JFileChooser while displaying the image changes as they are
 * applied. The UI elements to alter the image become enabled when a file is
 * loaded and then after applying as many filters as the user wants they can
 * save the image as a jpg file
 * 
 * @author Aidan Barrett
 * @version 11/23/22
 *
 */
public class ImageProcessorFrame extends JFrame implements ActionListener, ChangeListener, MouseListener {

	JMenuBar imageProgramBar;
	JMenu imageLoaderMenu;
	JMenu imageFilterMenu;
	JMenuItem openFile;
	JMenuItem saveFileAs;
	JMenuItem negativeWindow;
	JMenuItem redBlueSwapFilter;
	JMenuItem blackWhiteFilter;
	JMenuItem rotateClockwiseFilter;
	JMenuItem reflectFilter;
	JMenuItem brightnessFilter;
	JMenuItem cropFilter;
	JSlider brightnessSlider;
	Image fileToEdit;
	ImagePanel defaultPanel;
	private ArrayList<MouseEvent> mouseClicks = new ArrayList<MouseEvent>();
	int cropCorners = 0;
	boolean markerActive = false;

	/**
	 * 
	 * This method builds the Image Processor frame with a blank frame and a
	 * JMenuBar. The menu bar has 2 menus, one containing the option to load an
	 * image or save a filtered image and the second one containing various filters
	 * the user can apply to the image once loaded. All menu options other than
	 * loading an image are disabled until the user loads an image.
	 * 
	 */
	public ImageProcessorFrame() {

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		imageProgramBar = new JMenuBar();

		imageLoaderMenu = new JMenu("File");
		imageProgramBar.add(imageLoaderMenu);

		openFile = new JMenuItem("Open new image file");
		openFile.addActionListener(this);
		openFile.setToolTipText("Open an image file to edit");
		imageLoaderMenu.add(openFile);

		saveFileAs = new JMenuItem("Save filtered image as");
		saveFileAs.addActionListener(this);
		saveFileAs.setToolTipText("Save a filtered image file");
		imageLoaderMenu.add(saveFileAs);
		saveFileAs.setEnabled(false);

		imageFilterMenu = new JMenu("Filters");
		imageProgramBar.add(imageFilterMenu);

		redBlueSwapFilter = new JMenuItem("Red-blue Swap");
		redBlueSwapFilter.addActionListener(this);
		redBlueSwapFilter.setToolTipText("Swap an Image's red and blue values");
		imageFilterMenu.add(redBlueSwapFilter);
		redBlueSwapFilter.setEnabled(false);

		blackWhiteFilter = new JMenuItem("Black and white");
		blackWhiteFilter.addActionListener(this);
		blackWhiteFilter.setToolTipText("Make an image black and white");
		imageFilterMenu.add(blackWhiteFilter);
		blackWhiteFilter.setEnabled(false);

		rotateClockwiseFilter = new JMenuItem("Rotate clockwise");
		rotateClockwiseFilter.addActionListener(this);
		rotateClockwiseFilter.setToolTipText("Rotate an image clockwise");
		imageFilterMenu.add(rotateClockwiseFilter);
		rotateClockwiseFilter.setEnabled(false);

		reflectFilter = new JMenuItem("Reflect image");
		reflectFilter.addActionListener(this);
		reflectFilter.setToolTipText("Reflect an image horizontally");
		imageFilterMenu.add(reflectFilter);
		reflectFilter.setEnabled(false);

		brightnessFilter = new JMenuItem("Adjust Brightness");
		brightnessFilter.addActionListener(this);
		brightnessFilter.setToolTipText("Use a slider to adjust image brightness");
		imageFilterMenu.add(brightnessFilter);
		brightnessFilter.setEnabled(false);

		cropFilter = new JMenuItem("Crop Image");
		cropFilter.addActionListener(this);
		cropFilter.setToolTipText("Click on the top left and bottom right of an area to crop a square area");
		imageFilterMenu.add(cropFilter);
		cropFilter.setEnabled(false);

		negativeWindow = new JMenuItem("Negative Window Stamp");
		negativeWindow.addActionListener(this);
		negativeWindow
				.setToolTipText("click to toggle the stamp on or off, then click on the image to apply the stamp");
		imageFilterMenu.add(negativeWindow);
		negativeWindow.setEnabled(false);

		brightnessSlider = new JSlider(-200, 200);
		brightnessSlider.setPaintTicks(true);
		brightnessSlider.setMajorTickSpacing(10);
		brightnessSlider.addChangeListener(this);

		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(0, new JLabel("0"));
		labelTable.put(50, new JLabel("50"));
		labelTable.put(100, new JLabel("100"));
		labelTable.put(150, new JLabel("150"));
		labelTable.put(200, new JLabel("200"));
		labelTable.put(-50, new JLabel("-50"));
		labelTable.put(-100, new JLabel("-100"));
		labelTable.put(-150, new JLabel("-150"));
		labelTable.put(-200, new JLabel("-200"));
		brightnessSlider.setLabelTable(labelTable);
		brightnessSlider.setPaintLabels(true);

		this.setJMenuBar(imageProgramBar);

		this.setTitle("Photo Editor");
		this.setPreferredSize(new Dimension(600, 600));
		this.pack();

	}

	/**
	 * 
	 * This method creates a JFileChooser window and allows the user to select a
	 * valid image file those being, jpg, jpeg png, bmp, or gif files. If the window
	 * is closed before a file is chosen, or if an invalid file type is chosen a
	 * JOptionPane pops up informing the user that no image was loaded. If a valid
	 * file type is chosen then an Image object is created based on the file
	 * selected, and the blank pane on the ImageProcessor is updated with a visual
	 * representation of the image object. The other menu options are also enabled
	 * now that an image is loaded.
	 * 
	 */
	private void openFileOption() {

		JFileChooser opener = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "bmp", "gif");
		opener.setFileFilter(filter);
		int returnVal = opener.showOpenDialog(getParent());
		if (returnVal == JFileChooser.APPROVE_OPTION) {

			fileToEdit = new Image(opener.getSelectedFile().getName());
			defaultPanel = new ImagePanel(fileToEdit);
			defaultPanel.setLayout(new BorderLayout());
			this.setSize(new Dimension(fileToEdit.getNumberOfColumns(), fileToEdit.getNumberOfRows() + 61));
			defaultPanel.addMouseListener(this);

			this.setContentPane(defaultPanel);
			saveFileAs.setEnabled(true);
			redBlueSwapFilter.setEnabled(true);
			blackWhiteFilter.setEnabled(true);
			rotateClockwiseFilter.setEnabled(true);
			reflectFilter.setEnabled(true);
			brightnessFilter.setEnabled(true);
			negativeWindow.setEnabled(true);
			defaultPanel.add(brightnessSlider, BorderLayout.SOUTH);
			brightnessSlider.setVisible(false);
			cropFilter.setEnabled(false);

			revalidate();

		}

		if (returnVal != JFileChooser.APPROVE_OPTION) {

			JOptionPane.showMessageDialog(this, "An Invalid file type has been chosen, no image has been loaded",
					"Invalid Type", JOptionPane.ERROR_MESSAGE);

		}
	}

	/**
	 * 
	 * This method creates a JFileChooser window and allows the user to select or
	 * create a jpg file. If the window is closed before a file is chosen, or if an
	 * invalid file type is chosen a JOptionPane pops up informing the user that no
	 * file was saved. If a valid file type is chosen then the Image object
	 * displayed in the GUI is saved as a jpg file with the selected name and
	 * location.
	 * 
	 */
	private void saveFileOption() {

		JFileChooser opener = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG", "jpg");
		opener.setFileFilter(filter);
		int returnVal = opener.showOpenDialog(getParent());
		if (returnVal == JFileChooser.APPROVE_OPTION) {

			fileToEdit.writeImage(opener.getSelectedFile().getName());

		}

		if (returnVal != JFileChooser.APPROVE_OPTION) {

			JOptionPane.showMessageDialog(this, "An Invalid file type has been chosen, no image has been saved",
					"Invalid Type", JOptionPane.ERROR_MESSAGE);
		}

	}

	/**
	 * 
	 * This method toggles the markerActive value. While the value is true the crop
	 * function is dissabled, and whenever the user clicks on a location it creates
	 * 4 49 by 49 cubes with one pixel lines separating each of them. Each of the
	 * pixels in the cube have a color value that is the opposite of their original
	 * value.
	 * 
	 */
	private void negativeWindowMarker() {

		if (markerActive)

			markerActive = false;

		else

			markerActive = true;

	}

	/**
	 * 
	 * This method takes the loaded Image object and swaps the Red and Blue values
	 * of each pixel, the GUI is then revalidated to display the updated image.
	 * 
	 */
	private void redBlueSwapOption() {

		fileToEdit.redBlueSwapFilter();

		defaultPanel = new ImagePanel(fileToEdit);

		this.setContentPane(defaultPanel);

		defaultPanel.addMouseListener(this);

		defaultPanel.setLayout(new BorderLayout());

		defaultPanel.add(brightnessSlider, BorderLayout.SOUTH);

		brightnessSlider.setVisible(false);

		revalidate();

	}

	/**
	 * 
	 * This method takes the loaded Image object and converts each of the pixels to
	 * grayscale, the GUI is then revalidated to display the updated image.
	 * 
	 */
	private void blackWhiteFilterOption() {

		fileToEdit.blackAndWhiteFilter();

		defaultPanel = new ImagePanel(fileToEdit);

		this.setContentPane(defaultPanel);

		defaultPanel.addMouseListener(this);

		defaultPanel.setLayout(new BorderLayout());

		defaultPanel.add(brightnessSlider, BorderLayout.SOUTH);

		brightnessSlider.setVisible(false);

		revalidate();

	}

	/**
	 * 
	 * This method takes the loaded Image object and rotates it 90 degrees
	 * clockwise, the GUI is then revalidated to display the updated image.
	 * 
	 */
	private void clockWiseFilter() {

		fileToEdit.rotateClockwiseFilter();

		defaultPanel = new ImagePanel(fileToEdit);
		this.setSize(new Dimension(fileToEdit.getNumberOfColumns(), fileToEdit.getNumberOfRows() + 62));

		this.setContentPane(defaultPanel);

		defaultPanel.addMouseListener(this);

		defaultPanel.setLayout(new BorderLayout());

		defaultPanel.add(brightnessSlider, BorderLayout.SOUTH);

		brightnessSlider.setVisible(false);

		revalidate();

	}

	/**
	 * 
	 * This method takes the loaded Image object and reflects it's position across
	 * the Y axis, the GUI is then revalidated to display the updated image
	 * 
	 */
	private void reflectFilter() {

		fileToEdit.customFilter();

		defaultPanel = new ImagePanel(fileToEdit);
		this.setSize(new Dimension(fileToEdit.getNumberOfColumns(), fileToEdit.getNumberOfRows() + 62));

		this.setContentPane(defaultPanel);

		defaultPanel.addMouseListener(this);

		defaultPanel.setLayout(new BorderLayout());

		defaultPanel.add(brightnessSlider, BorderLayout.SOUTH);

		brightnessSlider.setVisible(false);

		revalidate();
	}

	/**
	 * 
	 * This method takes the area of the rectangle created with the paint component
	 * method in the ImagePanel class, and crops out the rest of the image so only
	 * the highlighted portion is present in the GUI. It then resets the arraylist
	 * containing the click areas to allow the user to crop the image again if
	 * desired, as well as re-enabling the other filters.
	 * 
	 */
	private void cropFilter() {

		fileToEdit.crop(mouseClicks.get(0).getX(), mouseClicks.get(0).getY(), mouseClicks.get(1).getX(),
				mouseClicks.get(1).getY());

		defaultPanel = new ImagePanel(fileToEdit);

		this.setSize(new Dimension(fileToEdit.getNumberOfColumns(), fileToEdit.getNumberOfRows() + 62));

		this.setContentPane(defaultPanel);

		defaultPanel.setLayout(new BorderLayout());

		defaultPanel.addMouseListener(this);

		revalidate();

		cropCorners = 0;

		saveFileAs.setEnabled(true);
		redBlueSwapFilter.setEnabled(true);
		blackWhiteFilter.setEnabled(true);
		rotateClockwiseFilter.setEnabled(true);
		reflectFilter.setEnabled(true);
		brightnessFilter.setEnabled(true);
		negativeWindow.setEnabled(true);
		defaultPanel.add(brightnessSlider, BorderLayout.SOUTH);
		brightnessSlider.setVisible(false);
		cropFilter.setEnabled(false);

		mouseClicks = new ArrayList<MouseEvent>();

	}

	/**
	 * 
	 * This method finds which menu item is selected via the action listener in the
	 * item and executes the associated method. These methods let the user load an
	 * image, save a loaded image, and filter the loaded image. The only exception
	 * to this is the brightness filter, which simply makes the slider visible.
	 * 
	 */
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == openFile) {

			openFileOption();

		}

		if (e.getSource() == saveFileAs) {

			saveFileOption();

		}

		if (e.getSource() == redBlueSwapFilter) {

			redBlueSwapOption();

		}

		if (e.getSource() == blackWhiteFilter) {

			blackWhiteFilterOption();

		}

		if (e.getSource() == rotateClockwiseFilter) {

			clockWiseFilter();

		}

		if (e.getSource() == reflectFilter) {

			reflectFilter();

		}

		if (e.getSource() == brightnessFilter) {

			brightnessSlider.setVisible(true);

		}

		if (e.getSource() == cropFilter) {

			cropFilter();

		}

		if (e.getSource() == negativeWindow) {

			negativeWindowMarker();

		}

	}

	/**
	 * 
	 * This method checks for a change in the brightness slider, and once it
	 * finishes moving, takes the value indicated by the slider and executes the
	 * brightness filter value with the indicated int value. The GUI is then updated
	 * to reflect this change and the brightness slider is hidden again.
	 * 
	 */
	public void stateChanged(ChangeEvent e) {

		if (e.getSource() == brightnessSlider) {

			JSlider src = (JSlider) e.getSource();
			if (!src.getValueIsAdjusting()) {
				int val = (int) src.getValue();
				if (src == this.brightnessSlider) {

					fileToEdit.brightnessFilter(val);

					defaultPanel = new ImagePanel(fileToEdit);

					this.setContentPane(defaultPanel);

					defaultPanel.setLayout(new BorderLayout());

					defaultPanel.add(brightnessSlider, BorderLayout.SOUTH);

					brightnessSlider.setVisible(false);

					defaultPanel.addMouseListener(this);

					revalidate();

				}

			}

		}

	}

	/**
	 * 
	 * This method is executed when the mouse is clicked, it then checkes if the
	 * marker Active value is true or false. If it's true then 4 49 by 49 pixel
	 * cubes are created around the clicked location with color values that are the
	 * inverse of their normal color.
	 * 
	 * If the marker active value is false then the MouseEvent is added to the
	 * mouseClicks arrayList and the crop corners int value is increased by 1. When
	 * the crop corners value equals 2 the GUI is repainted to place a transparent
	 * gray rectangle over the crop area, and all filters except the crop filter are
	 * disabled.
	 * 
	 */
	public void mouseClicked(MouseEvent e) {

		if (markerActive) {

			fileToEdit.negativeWindow(e.getX(), e.getY());

			defaultPanel = new ImagePanel(fileToEdit);

			this.setSize(new Dimension(fileToEdit.getNumberOfColumns(), fileToEdit.getNumberOfRows() + 62));

			this.setContentPane(defaultPanel);

			defaultPanel.addMouseListener(this);

			defaultPanel.setLayout(new BorderLayout());

			defaultPanel.add(brightnessSlider, BorderLayout.SOUTH);

			brightnessSlider.setVisible(false);

			revalidate();

		}

		if (!markerActive) {

			this.mouseClicks.add(e);

			cropCorners++;

			if (cropCorners == 2) {

				Graphics g = defaultPanel.getGraphics();

				this.defaultPanel.paintComponent(g, mouseClicks.get(0).getX(), mouseClicks.get(0).getY(),
						mouseClicks.get(1).getX(), mouseClicks.get(1).getY());

				saveFileAs.setEnabled(false);
				redBlueSwapFilter.setEnabled(false);
				blackWhiteFilter.setEnabled(false);
				rotateClockwiseFilter.setEnabled(false);
				reflectFilter.setEnabled(false);
				brightnessFilter.setEnabled(false);
				negativeWindow.setEnabled(false);
				defaultPanel.add(brightnessSlider, BorderLayout.SOUTH);
				brightnessSlider.setVisible(false);
				cropFilter.setEnabled(true);

			}
		}

	}

	/**
	 * 
	 * These methods are inherited from the MouseListener, but are not used
	 * 
	 */
	public void mouseDragged(MouseEvent arg0) {
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent arg0) {
	}

	public void mouseReleased(MouseEvent arg0) {
	}

	/**
	 * Required by a serializable class
	 */
	private static final long serialVersionUID = 1L;

}