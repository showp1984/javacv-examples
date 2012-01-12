/*
 * Copyright (c) 2011-2012 Jarek Sacha. All Rights Reserved.
 *
 * Author's e-mail: jarek.listy at gmail.com
 */

package opencv2_cookbook.chapter03

import opencv2_cookbook.OpenCVImageJUtils._
import com.googlecode.javacv.cpp.opencv_core.IplImage
import ij.process.ByteProcessor
import java.awt.Color
import math._


/**
 * Example of using a strategy pattern in algorithm design.
 * The pattern encapsulates an algorithm into a separate class.
 * To run this example use [[opencv2_cookbook.chapter03.example1.Ex1ColorDetector]].
 *
 * The algorithm converts the input image to a binary by checking is pixel color is within a given distance from a desired color.
 * Pixels with color close to the desired color are white, other black.
 *
 * This Scala code functionally is equivalent to C++ code in chapter 3 section
 * "Using the Strategy pattern in algorithm design".
 * The original example in the book is using "C++ API". To make operations on image pixels easier and more efficient
 * OpenCV color image is converted to ImageJ representation during processing.
 *
 * Unlike the in the C++ example, this class does not pre-allocates and hold space for process image,
 * it is create only when needed.
 */
class ColorDetector(private var _minDist: Int = 100,
                    private var _target: Color = new Color(130, 190, 230)) {


    def colorDistanceThreshold = _minDist

    def colorDistanceThreshold_=(dist: Int) {_minDist = max(0, dist)}

    def targetColor = _target

    def targetColor_=(color: Color) {_target = color}

    def process(iplImage: IplImage): IplImage = {
        // Convert to ColorProcessor for easier pixel access
        val src = toColorProcessor(iplImage)
        val dest = new ByteProcessor(src.getWidth, src.getHeight)
        for (y <- 0 until src.getHeight) {
            for (x <- 0 until src.getWidth) {

                if (distance(src.getColor(x, y)) < _minDist) {
                    dest.set(x, y, 255)
                }
            }
        }

        // Convert back to IplImage
        IplImage.createFrom(toBufferedImage(dest))
    }

    private def distance(color: java.awt.Color): Double = {
        abs(_target.getRed - color.getRed) + abs(_target.getGreen - color.getGreen) + abs(_target.getBlue - color.getBlue)
    }
}