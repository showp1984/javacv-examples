/*
 * Copyright (c) 2011 Jarek Sacha. All Rights Reserved.
 *
 * Author's e-mail: jarek.listy at gmail.com
 */

package opencv2_cookbook.chapter02

import opencv2_cookbook.OpenCVUtils._
import java.io.File
import com.googlecode.javacv.cpp.opencv_highgui._
import com.googlecode.javacv.cpp.opencv_core.CvMat


/**
 * Reduce colors in the image by modifying color values in all bands the same way.
 */
object Ex02ColorReduce extends App {

    // Read input image
    val image = loadMatAndShowOrExit(new File("../data/boldt.jpg"), CV_LOAD_IMAGE_COLOR)

    // Add salt noise
    val dest = colorReduce(image)

    // Display
    show(dest, "Reduced colors")


    /**
     * Add 'salt' noise to a copy of input image
     * @param image input image
     * @param number of 'salt' grains
     */
    def colorReduce(image: CvMat, div: Int = 64): CvMat = {

        // Number of lines
        val maxY = image.rows
        // total number of elements per line
        val maxX = image.cols * image.channels()

        for (y <- 0 until maxY) {
            for (x <- 0 until maxX) {
                // Convert to integer
                val v: Int = image.get(x + y * maxX).asInstanceOf[Int]
                // Use integer division to reduce number of values
                val newV: Int = v / div * div + div / 2
                // Put back into the image
                image.put(x + y * maxX, newV)
            }
        }

        image
    }
}