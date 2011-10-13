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
 * Paste small image into a larger one using a region of interest. Mask is optional.
 */
object Ex05ROILogo extends App {

    // Read input image
    val logo = loadAndShowOrExit(new File("../data/logo.bmp"), CV_LOAD_IMAGE_COLOR)
    val mask = loadAndShowOrExit(new File("../data/logo.bmp"), CV_LOAD_IMAGE_GRAYSCALE)
    val image = loadAndShowOrExit(new File("../data/boldt.jpg"), CV_LOAD_IMAGE_COLOR)

    val roi = new IplROI()
    roi.xOffset(385)
    roi.yOffset(270)
    roi.width(logo.width)
    roi.height(logo.height)

    val imageROI = image.roi(roi)

    cvCopy(logo, imageROI, mask)

    // Display
    show(image, "With Logo")
}