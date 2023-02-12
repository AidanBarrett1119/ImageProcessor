package assign11;

/**
 * This class represents the RGB value of a pixel with individual int values
 * between 0 and 255 to represent the red, green, and blue values of the pixel.
 * It also provides a number of methods to get the individual values of the
 * pixel item as well as the packed RGB vaue using the 3 color values given
 * 
 * @author Aidan Barrett
 * @version 10/23/22
 */

public class Pixel {

	private int redAmount;

	private int greenAmount;

	private int blueAmount;

	/**
	 * Creates a new pixel object by using the int values provided as the Red, Green
	 * and Blue values. If the int value provided is less than zero or greater than
	 * 255 then an IllegalArgumentException is thrown
	 * 
	 * 
	 * @param redAmount   - the int value that represents the red amount
	 * @param greenAmount - the int value that represents the green amount
	 * @param blueAmount  - the int value that represents the blue amount
	 */
	public Pixel(int redAmount, int greenAmount, int blueAmount) {

		if (redAmount > 255 || redAmount < 0 || greenAmount > 255 || greenAmount < 0 || blueAmount > 255
				|| blueAmount < 0)

			throw new IllegalArgumentException("RGB value not between 0 and 255");

		else {

			this.redAmount = redAmount;

			this.greenAmount = greenAmount;

			this.blueAmount = blueAmount;

		}
	}

	/**
	 * Returns the red amount as an int value
	 * 
	 * @return - the red value of the pixel as an int
	 */
	public int getRedAmount() {

		return this.redAmount;
	}

	/**
	 * Returns the green amount as an int value
	 * 
	 * @return - the green value of the pixel as an int
	 */
	public int getGreenAmount() {

		return this.greenAmount;
	}

	/**
	 * Returns the blue amount as an int value
	 * 
	 * @return - the blue value of the pixel as an int
	 */
	public int getBlueAmount() {

		return this.blueAmount;
	}

	/**
	 * Returns the packed RGB value of the pixel object by using the red, green, and
	 * blue values
	 * 
	 * @return - the packed RGB value as an int
	 */
	public int getPackedRGB() {

		return (this.redAmount << 16) | (this.greenAmount << 8) | (this.blueAmount);
	}

}