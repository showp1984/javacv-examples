/*
 * Copyright (c) 2011-2012 Jarek Sacha. All Rights Reserved.
 *
 * Author's e-mail: jarek.listy at gmail.com
 */

package opencv2_cookbook.chapter04

import com.googlecode.javacv.cpp.opencv_core._
import com.googlecode.javacv.cpp.opencv_imgproc._

/**
 * Helper class that simplifies usage of OpenCV `cv::calcHist` function for single channel images.
 *
 * See OpenCV [[http://opencv.itseez.com/modules/imgproc/doc/histograms.html?highlight=histogram]] documentation to learn backend details..
 */
class ColorHistogram {

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
    def getHistogram(image: IplImage): CvHistogram = {
        // Allocate histogram object
        val dims = 3
        val sizes = Array(numberOfBins, numberOfBins, numberOfBins)
        val histType = CV_HIST_SPARSE
        val minMax = Array(_minRange, _maxRange)
        val ranges = Array(minMax, minMax, minMax)
        val uniform = 1
        val hist = cvCreateHist(dims, sizes, histType, null: Array[Array[Float]], uniform)

        // Compute histogram
        val accumulate = 0
        val mask = null
        cvCalcHist(Array(image), hist, accumulate, mask)
        hist
    }
}