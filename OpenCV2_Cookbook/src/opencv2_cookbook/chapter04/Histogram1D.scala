/*
 * Copyright (c) 2011-2012 Jarek Sacha. All Rights Reserved.
 *
 * Author's e-mail: jarek.listy at gmail.com
 */

package opencv2_cookbook.chapter04

import com.googlecode.javacv.cpp.opencv_core._
import com.googlecode.javacv.cpp.opencv_imgproc._
import com.googlecode.javacv.cpp.opencv_legacy._
import java.awt.image.BufferedImage
import java.awt.Color

/**
 * Helper class that simplifies usage of OpenCV `cv::calcHist` function for single channel images.
 *
 * See OpenCV [[http://opencv.itseez.com/modules/imgproc/doc/histograms.html?highlight=histogram]] documentation to learn backend details..
 */
class Histogram1D {

    val numberOfBins = 256
    var _minRange = 0.0f
    var _maxRange = 255.0f

    private def setRanges(minRange: Float, maxRange: Float) {
        _minRange = minRange
        _maxRange = maxRange
    }

    /**
     * Computes histogram of an image. This method is `private` since its proper use requires
     * knowledge of inner working of the implementation:
     *  # how to extract data from the CvHistogram structure
     *  # CvHistogram has to be manually deallocated after use.
     *
     * @param image input image
     * @return OpenCV histogram object
     */
    private def getHistogram(image: IplImage): CvHistogram = {
        // Allocate histogram object
        val dims = 1
        val sizes = Array(numberOfBins)
        val histType = CV_HIST_ARRAY
        val ranges = Array(Array(_minRange, _maxRange))
        val hist = cvCreateHist(dims, sizes, histType, ranges, 1)

        // Compute histogram
        val accumulate = 0
        val mask = null
        cvCalcHist(Array(image), hist, accumulate, mask)
        hist
    }

    /**
     * Computes histogram of an image.
     * @param image input image
     * @return histogram represented as an array
     */
    def getHistogramAsArray(image: IplImage): Array[Float] = {
        // Create and calculate histogram object
        val histogram = getHistogram(image)

        // Extract values to an array
        val dest = new Array[Float](numberOfBins)
        for (bin <- 0 until numberOfBins) {
            dest(bin) = cvQueryHistValue_1D(histogram, bin)
        }

        // Release the memory allocated for histogram
        cvReleaseHist(histogram)

        dest
    }

    def getHistogramImage(image: IplImage): BufferedImage = {

        // Output image size
        val width = numberOfBins
        val height = numberOfBins

        val hist = getHistogramAsArray(image)
        // Set highest point to 90% of the number of bins
        val scale = 0.9 / hist.max * height

        // Create a color image to draw on
        val canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val g = canvas.createGraphics()

        // Paint background
        g.setPaint(Color.WHITE)
        g.fillRect(0, 0, width, height)

        // Draw a vertical line for each bin
        g.setPaint(Color.BLUE)
        for (bin <- 0 until numberOfBins) {
            def h = math.round(hist(bin) * scale).toInt
            g.drawLine(bin, height - 1, bin, height - h - 1)
        }

        // Cleanup
        g.dispose()

        canvas
    }
}