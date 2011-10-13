/*
 * Copyright (c) 2011 Jarek Sacha. All Rights Reserved.
 *
 * Author's e-mail: jarek.listy at gmail.com
 */

package opencv2_cookbook.chapter02

import opencv2_cookbook.OpenCVUtils._
import java.io.File
import com.googlecode.javacv.cpp.opencv_highgui._
import com.googlecode.javacv.cpp.opencv_core.IplImage
import opencv2_cookbook.OpenCVImageJUtils
import util.Random


/**
 * Set individual, randomly selected, pixels to a fixed value.
 */
object Ex01Salt extends App {

    // Read input image
    val image = loadAndShowOrExit(new File("../data/boldt.jpg"), CV_LOAD_IMAGE_COLOR)

    // Add salt noise
    val dest = salt(image, 2000)

    // Display
    show(dest, "Salted")


    /**
     * Add 'salt' noise to a copy of input image
     * @param image input image
     * @param number of 'salt' grains
     */
    def salt(image: IplImage, n: Int): IplImage = {

        // Convert to Image's ImageProcessor for easy pixel operations
        val ip = OpenCVImageJUtils.toImageProcessor(image)

        // Place 'n' white spots at random locations
        val size = ip.getWidth * ip.getHeight
        val random = new Random
        for (i <- 1 to n) {
            // Create random index of a pixel
            val index = random.nextInt(size)
            // Set it to white
            ip.set(index, 0xFFFFFF)
        }

        // Convert ImageProcessor back to IplImage
        IplImage.createFrom(OpenCVImageJUtils.toBufferedImage(ip))
    }
}