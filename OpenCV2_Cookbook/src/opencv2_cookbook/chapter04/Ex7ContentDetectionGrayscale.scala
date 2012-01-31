/*
 * Copyright (c) 2011-2012 Jarek Sacha. All Rights Reserved.
 *
 * Author's e-mail: jarek.listy at gmail.com
 */

package opencv2_cookbook.chapter04

import opencv2_cookbook.OpenCVUtils._
import com.googlecode.javacv.cpp.opencv_core._
import com.googlecode.javacv.cpp.opencv_highgui._
import com.googlecode.javacv.cpp.opencv_imgproc._
import java.awt.{Color, Shape, Rectangle}
import java.awt.image.BufferedImage
import java.io.File

/**
 * Uses histogram of region in an grayscale image to create 'template', looks through the whole image to detect pixels that are
 * similar to that template.
 * Example for section "Backprojecting a histogram to detect specific image content" in Chapter 4.
 */
object Ex7ContentDetectionGrayscale extends App {

    // Load image as a grayscale
    val src = loadOrExit(new File("data/waves.jpg"), CV_LOAD_IMAGE_GRAYSCALE)

    // Display image with marked ROI
    val rect = new Rectangle(360, 44, 40, 50)
    showWithOverlay(src, rect, "Input")

    // Define ROI
    val roi = new IplROI()
    roi.xOffset(rect.x)
    roi.yOffset(rect.y)
    roi.width(rect.width)
    roi.height(rect.height)

    val imageROI = src.roi(roi)

    // Compute histogram within the ROI
    val h = new Histogram1D().getHistogram(src)

    // Normalize histogram so the sum of all bins is equal to 1.
    cvNormalizeHist(h, 1)

    // Create out put image of the same size as input, but with Float 32 pixels
    src.roi(null)
    val src32F = cvCreateImage(cvGetSize(src), IPL_DEPTH_32F, 1)
    cvConvertScale(src, src32F, 1, 0)
    val dest32F = cvCreateImage(cvGetSize(src32F), src32F.depth(), src32F.nChannels)
    cvCalcBackProject(Array(src32F), dest32F, h)

    // Show results
    scaleAndShow(dest32F, "Backprojection result")


    /**
     *  Show image with an overlay.
     */
    def showWithOverlay(image: IplImage, overlay: Shape, title: String) {
        val bi = image.getBufferedImage
        val canvas = new BufferedImage(bi.getWidth, bi.getHeight, BufferedImage.TYPE_INT_RGB)
        val g = canvas.createGraphics()
        g.drawImage(bi, 0, 0, null)
        g.setPaint(Color.RED)
        g.draw(overlay)
        g.dispose()
        show(canvas, title)
    }

    /**
     *  Scale Float 32 image to pixel value range [0,1], so it can be properly displayed by `CanvasFrame`.
     */
    def scaleAndShow(image: IplImage, title: String) {
        val min = Array(Double.MaxValue)
        val max = Array(Double.MinValue)
        cvMinMaxLoc(image, min, max, null, null, null)
        val scale = 1 / (max(0) - min(0))
        val imageScaled = cvCreateImage(cvGetSize(src), IPL_DEPTH_32F, 1)
        cvConvertScale(image, imageScaled, scale, 0)

        // Show results
        show(imageScaled, title)
    }
}
