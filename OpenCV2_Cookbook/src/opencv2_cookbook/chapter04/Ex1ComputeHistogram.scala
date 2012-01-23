/*
 * Copyright (c) 2011-2012 Jarek Sacha. All Rights Reserved.
 *
 * Author's e-mail: jarek.listy at gmail.com
 */

package opencv2_cookbook.chapter04

import opencv2_cookbook.OpenCVUtils._
import com.googlecode.javacv.cpp.opencv_highgui._
import java.io.File
import math.round

/**
 *  The first example for section "Computing the image histogram" in Chapter 4, page 91.
 *  Computes histogram using utility class [[opencv2_cookbook.chapter04.Histogram1D]] and prints values to the screen.
 */
object Ex1ComputeHistogram extends App {

    // Load image as a grayscale since we will be calculating histogram of an image with a single channel
    val src = loadAndShowOrExit(new File("data/group.jpg"), CV_LOAD_IMAGE_GRAYSCALE)

    // Calculate histogram
    val h = new Histogram1D
    val histogram = h.getHistogramAsArray(src)

    // Print histogram values, the Scala way
    histogram.zipWithIndex.foreach {
        case (count, bin) => println("" + bin + ": " + round(count))
    }

    // Validate histogram computations
    val numberOfPixels = src.width() * src.height()
    println("Number of pixels     : " + numberOfPixels)
    val sumOfHistogramBins = round(histogram.sum)
    println("Sum of histogram bins: " + sumOfHistogramBins)
    require(numberOfPixels == sumOfHistogramBins, "Number of pixels must be equal the sum of histogram bins")
}