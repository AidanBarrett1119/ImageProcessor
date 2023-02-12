package assign11;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * This class represents an image as a two-dimensional array of pixels and
 * provides a number of image filters (via instance methods) for changing the
 * appearance of the image. Application of multiple filters is cumulative; e.g.,
 * obj.redBlueSwapFilter() followed by obj.rotateClockwiseFilter() results in an
 * image altered both in color and orientation.
 * 
 * Note: - The pixel in the northwest corner of the image is stored in the first
 * row, first column. - The pixel in the northeast corner of the image is stored
 * in the first row, last column. - The pixel in the southeast corner of the
 * image is stored in the last row, last column. - The pixel in the southwest
 * corner of the image is stored in the last row, first column.
 * 
 * @author Prof. Parker and Aidan Barrett
 * @version 10/24/22
 */
public class Image {

	private Pixel[][] imageArray;

	/**
	 * Creates a new Image object by reading the image file with the given filename.
	 * 
	 * DO NOT MODIFY THIS METHOD
	 * 
	 * @param filename - name of the given image file to read
	 * @throws IOException if file does not exist or cannot be read
	 */
	public Image(String filename) {
		BufferedImage imageInput = null;
		try {
			imageInput = ImageIO.read(new File(filename));
		} catch (IOException e) {
			System.out.println("Image file " + filename + " does not exist or cannot be read.");
		}

		imageArray = new Pixel[imageInput.getHeight()][imageInput.getWidth()];
		for (int i = 0; i < imageArray.length; i++)
			for (int j = 0; j < imageArray[0].length; j++) {
				int rgb = imageInput.getRGB(j, i);
				imageArray[i][j] = new Pixel((rgb >> 16) & 255, (rgb >> 8) & 255, rgb & 255);
			}
	}

	/**
	 * Create a new "default" Image object, whose purpose is to be used in testing.
	 * 
	 * The orientation of this image: cyan red green magenta yellow blue
	 *
	 * DO NOT MODIFY THIS METHOD
	 */
	public Image() {
		imageArray = new Pixel[3][2];
		imageArray[0][0] = new Pixel(0, 255, 255); // cyan
		imageArray[0][1] = new Pixel(255, 0, 0); // red
		imageArray[1][0] = new Pixel(0, 255, 0); // green
		imageArray[1][1] = new Pixel(255, 0, 255); // magenta
		imageArray[2][0] = new Pixel(255, 255, 0); // yellow
		imageArray[2][1] = new Pixel(0, 0, 255); // blue
	}

	/**
	 * Gets the pixel at the specified row and column indexes.
	 * 
	 * DO NOT MODIFY THIS METHOD
	 * 
	 * @param rowIndex    - given row index
	 * @param columnIndex - given column index
	 * @return the pixel at the given row index and column index
	 * @throws IndexOutOfBoundsException if row or column index is out of bounds
	 */
	public Pixel getPixel(int rowIndex, int columnIndex) {
		if (rowIndex < 0 || rowIndex >= imageArray.length)
			throw new IndexOutOfBoundsException("rowIndex must be in range 0-" + (imageArray.length - 1));

		if (columnIndex < 0 || columnIndex >= imageArray[0].length)
			throw new IndexOutOfBoundsException("columnIndex must be in range 0-" + (imageArray[0].length - 1));

		return imageArray[rowIndex][columnIndex];
	}

	/**
	 * Writes the image represented by this object to file. Does nothing if the
	 * image length is 0.
	 * 
	 * DO NOT MODIFY THIS METHOD
	 * 
	 * @param filename - name of image file to write
	 * @throws IOException if file does cannot be written
	 */
	public void writeImage(String filename) {
		if (imageArray.length > 0) {
			BufferedImage imageOutput = new BufferedImage(imageArray[0].length, imageArray.length,
					BufferedImage.TYPE_INT_RGB);

			for (int i = 0; i < imageArray.length; i++)
				for (int j = 0; j < imageArray[0].length; j++)
					imageOutput.setRGB(j, i, imageArray[i][j].getPackedRGB());

			try {
				ImageIO.write(imageOutput, "png", new File(filename));
			} catch (IOException e) {
				System.out.println("The image cannot be written to file " + filename);
			}
		}
	}

	/**
	 * Applies a filter to the image represented by this object such that for each
	 * pixel the red amount and blue amount are swapped.
	 * 
	 * HINT: Since the Pixel class does not include setter methods for its private
	 * instance variables, create new Pixel objects with the altered colors.
	 */
	public void redBlueSwapFilter() {

		if (imageArray.length > 0) {

			for (int i = 0; i < imageArray.length; i++) {

				for (int j = 0; j < imageArray[i].length; j++) {

					Pixel pixelToAlter = this.getPixel(i, j);

					Pixel alteredPixel = new Pixel(pixelToAlter.getBlueAmount(), pixelToAlter.getGreenAmount(),
							pixelToAlter.getRedAmount());

					imageArray[i][j] = alteredPixel;

				}
			}
		}
	}

	/**
	 * Applies a filter to the image represented by this object such that the color
	 * of each pixel is converted to its corresponding grayscale shade, producing
	 * the effect of a black and white photo. The filter sets the amount of red,
	 * green, and blue all to the value of this average: (originalRed +
	 * originalGreen + originalBlue) / 3
	 * 
	 * HINT: Since the Pixel class does not include setter methods for its private
	 * instance variables, create new Pixel objects with the altered colors.
	 */
	public void blackAndWhiteFilter() {

		if (imageArray.length > 0) {

			for (int i = 0; i < imageArray.length; i++) {

				for (int j = 0; j < imageArray[i].length; j++) {

					Pixel pixelToAlter = this.getPixel(i, j);

					int grayScaleAverage = (pixelToAlter.getRedAmount() + pixelToAlter.getBlueAmount()
							+ pixelToAlter.getGreenAmount()) / 3;

					Pixel alteredPixel = new Pixel(grayScaleAverage, grayScaleAverage, grayScaleAverage);

					imageArray[i][j] = alteredPixel;

				}
			}
		}
	}

	/**
	 * Applies a filter to the image represented by this object such that it is
	 * rotated clockwise (by 90 degrees).
	 * 
	 * HINT: If the image is not square, this filter requires creating a new array
	 * with different lengths. Use the technique of creating a double-length backing
	 * array in BetterDynamicArray (assign06) as a guide for how to make a second
	 * array and eventually reset the imageArray reference to this new array.
	 */
	public void rotateClockwiseFilter() {

		Pixel[][] alteredImage = new Pixel[imageArray[0].length][imageArray.length];

		for (int i = 0; i < imageArray.length; i++) {

			for (int j = 0; j < imageArray[0].length; j++) {

				alteredImage[j][imageArray.length - 1 - i] = imageArray[i][j];
			}
		}

		imageArray = alteredImage;
	}

	/**
	 * Applies a filter to the image represented by this object such that it is
	 * reversed along the y axis AKA a left to right reversal
	 */
	public void customFilter() {

		Pixel[][] alteredImage = new Pixel[imageArray.length][imageArray[0].length];

		for (int i = 0; i < imageArray.length; i++) {

			for (int j = 0; j < imageArray[0].length; j++) {

				alteredImage[i][imageArray[0].length - 1 - j] = imageArray[i][j];
			}
		}

		imageArray = alteredImage;
	}

	/**
	 * 
	 * Applies a filter to the image that brightens or darkens each pixel by the
	 * given int parameter. If an RGB value would go above 255 or below 0 then 255
	 * or 0 is used instead.
	 * 
	 * @param brightnessInput - the int value to brighten the image by
	 */
	public void brightnessFilter(int brightnessInput) {

		int newRedValue = 0;

		int newBlueValue = 0;

		int newGreenValue = 0;

		if (imageArray.length > 0) {

			for (int i = 0; i < imageArray.length; i++) {

				for (int j = 0; j < imageArray[i].length; j++) {

					Pixel pixelToAlter = this.getPixel(i, j);

					if (pixelToAlter.getRedAmount() + brightnessInput > 255)

						newRedValue = 255;

					else if (pixelToAlter.getRedAmount() + brightnessInput < 0)

						newRedValue = 0;

					else

						newRedValue = pixelToAlter.getRedAmount() + brightnessInput;

					if (pixelToAlter.getGreenAmount() + brightnessInput > 255)

						newGreenValue = 255;

					else if (pixelToAlter.getGreenAmount() + brightnessInput < 0)

						newGreenValue = 0;

					else

						newGreenValue = pixelToAlter.getGreenAmount() + brightnessInput;

					if (pixelToAlter.getBlueAmount() + brightnessInput > 255)

						newBlueValue = 255;

					else if (pixelToAlter.getBlueAmount() + brightnessInput < 0)

						newBlueValue = 0;

					else

						newBlueValue = pixelToAlter.getBlueAmount() + brightnessInput;

					Pixel alteredPixel = new Pixel(newRedValue, newGreenValue, newBlueValue);

					imageArray[i][j] = alteredPixel;

				}

			}

		}
	}

	/**
	 * 
	 * This method creates a new image array that is the size of the selected region
	 * to crop. It then copies all the pixels in the area to be cropped to the new
	 * image array, and sets the value of the original array to the new array
	 * 
	 * @param X1 - the X value of the first mouse event
	 * @param Y1 - the Y value of the first mouse event
	 * @param X2 - the X value of the second mouse event
	 * @param Y2 - the Y value of the second mouse event
	 */
	public void crop(int X1, int Y1, int X2, int Y2) {

		Pixel[][] alteredImage = new Pixel[Y2 - Y1][X2 - X1];

		for (int x = 0 + X1; x < X2; x++) {

			for (int y = 0 + Y1; y < Y2; y++) {

				alteredImage[y - Y1][x - X1] = this.imageArray[y - 1][x - 1];

			}

		}

		this.imageArray = alteredImage;

	}

	/**
	 * 
	 * This method creates 4 cubes around the specified location with each pixel in
	 * the cubes having the inverse of their normal color value.
	 * 
	 * @param X1 - the X value of the mouse event
	 * @param Y1 - the Y value of the mouse event
	 */
	public void negativeWindow(int X1, int Y1) {

		Pixel pixelToAlter;

		Pixel alteredPixel;

		for (int i = 0; i < 50; i++) {

			for (int j = 0; j < 50; j++) {

				if (X1 + i >= 0 && X1 + i < this.imageArray[0].length) {

					if (Y1 + j >= 0 && Y1 + j < this.imageArray.length) {

						pixelToAlter = this.getPixel(Y1 + j, X1 + i);

						alteredPixel = new Pixel(255 - pixelToAlter.getRedAmount(), 255 - pixelToAlter.getGreenAmount(),
								255 - pixelToAlter.getBlueAmount());

						this.imageArray[Y1 + j][X1 + i] = alteredPixel;

					}

				}

				if (X1 - i >= 0 && X1 - i < this.imageArray[0].length) {

					if (Y1 + j >= 0 && Y1 + j < this.imageArray.length) {

						pixelToAlter = this.getPixel(Y1 + j, X1 - i);

						alteredPixel = new Pixel(255 - pixelToAlter.getRedAmount(), 255 - pixelToAlter.getGreenAmount(),
								255 - pixelToAlter.getBlueAmount());

						this.imageArray[Y1 + j][X1 - i] = alteredPixel;

					}

				}

				if (Y1 - j >= 0 && Y1 - j < this.imageArray.length) {

					if (X1 + i >= 0 && X1 + i < this.imageArray[0].length) {

						pixelToAlter = this.getPixel(Y1 - j, X1 + i);

						alteredPixel = new Pixel(255 - pixelToAlter.getRedAmount(), 255 - pixelToAlter.getGreenAmount(),
								255 - pixelToAlter.getBlueAmount());

						this.imageArray[Y1 - j][X1 + i] = alteredPixel;

					}

				}

				if (Y1 - j >= 0 && Y1 - j < this.imageArray.length) {

					if (X1 - i >= 0 && X1 - i < this.imageArray[0].length) {

						pixelToAlter = this.getPixel(Y1 - j, X1 - i);

						alteredPixel = new Pixel(255 - pixelToAlter.getRedAmount(), 255 - pixelToAlter.getGreenAmount(),
								255 - pixelToAlter.getBlueAmount());

						this.imageArray[Y1 - j][X1 - i] = alteredPixel;

					}

				}

			}

		}

	}

	/**
	 * 
	 * This method returns the number of rows in the image object
	 * 
	 * @return - the number of rows in int form
	 */
	public int getNumberOfRows() {

		return this.imageArray.length;
	}

	/**
	 * 
	 * This method returns the number of columns in the image object
	 * 
	 * @return - the number of columns in int form
	 */
	public int getNumberOfColumns() {

		if (this.imageArray.length == 0)
			return 0;
		return this.imageArray[0].length;
	}

}