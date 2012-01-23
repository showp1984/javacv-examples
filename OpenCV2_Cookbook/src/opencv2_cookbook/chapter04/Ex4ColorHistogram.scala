/*
 * Copyright (c) 2011-2012 Jarek Sacha. All Rights Reserved.
 *
 * Author's e-mail: jarek.listy at gmail.com
 */

package opencv2_cookbook.chapter04

import opencv2_cookbook.OpenCVUtils._
import com.googlecode.javacv.cpp.opencv_highgui._
import java.io.File

/**
 *  The first example for section "Computing the image histogram" in Chapter 4, page 91.
 *  Computes histogram using utility class [[opencv2_cookbook.chapter04.Histogram1D]] and prints values to the screen.
 */
object Ex4ColorHistogram extends App {

    val src = loadAndShowOrExit(new File("data/group.jpg"), CV_LOAD_IMAGE_COLOR)

    // Calculate histogram
    val h = new ColorHistogram
    val histogram = h.getHistogram(src)

    println("type: " + histogram.`type`)
}