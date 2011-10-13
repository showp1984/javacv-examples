/*
 * Copyright (c) 2011 Jarek Sacha. All Rights Reserved.
 *
 * Author's e-mail: jarek.listy at gmail.com
 */

package opencv2_cookbook.chapter02

import opencv2_cookbook.OpenCVUtils._
import java.io.File
import com.googlecode.javacv.cpp.opencv_core._
import com.googlecode.javacv.cpp.opencv_highgui._


/**
 * Blend two images using weighted addition.
 */
object Ex04BlendImages extends App {

    // Read input image
    val image1 = loadAndShowOrExit(new File("../data/boldt.jpg"), CV_LOAD_IMAGE_COLOR)
    val image2 = loadAndShowOrExit(new File("../data/rain.jpg"), CV_LOAD_IMAGE_COLOR)

    // Define output image
    val result = cvCreateImage(cvGetSize(image1), image1.depth, 3)

    cvAddWeighted(image1, 0.7, image2, 0.9, 0.0, result)

    // Display
    show(result, "Blended")
}