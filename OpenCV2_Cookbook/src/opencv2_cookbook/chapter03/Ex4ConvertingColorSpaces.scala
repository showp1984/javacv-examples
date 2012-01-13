/*
 * Copyright (c) 2011-2012 Jarek Sacha. All Rights Reserved.
 *
 * Author's e-mail: jarek.listy at gmail.com
 */

package opencv2_cookbook.chapter03

import opencv2_cookbook.OpenCVUtils._
import com.googlecode.javacv.cpp.opencv_highgui._
import java.io.File
import java.awt.Color

/**
 * Example for section "Converting color spaces" in Chapter 3.
 * Colors are detected in L*a*b* color space rather than in RGB.
 *
 * Compare it to [[opencv2_cookbook.chapter03.Ex1ColorDetector]]
 */
object Ex4ConvertingColorSpaces extends App {

    // 1. Create image processor object
    val colorDetector = new ColorDetectorLab

    // 2. Read input image
    val src = loadAndShowOrExit(new File("data/boldt.jpg"), CV_LOAD_IMAGE_COLOR)

    // 3. Set the input parameters
    colorDetector.colorDistanceThreshold = 30
    // here blue sky, RGB=(130, 190, 230) <=> L*a*b*=(74.3705, -9.0003, -25.9781)
    // Since we will be using unsigned 8-bit per channel RGB to unsigned 8-bit per channel L*a*b*
    // values are4 converted to fit 0-255 range
    //       L <- L*255/100
    //       a <- a + 128
    //       b <- b + 128
    // Additionally OpenCV stores L*a*b* as b*a*L*, so we have (note use of integer arithmetic)
    colorDetector.targetColor = new Color(-26 + 128, -9 + 128, (74 * 255) / 100)


    // 4. Process that input image and display the result
    val dest = colorDetector.process(src)
    show(dest, "result")
}