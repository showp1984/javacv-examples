/*
 * Copyright (c) 2011-2014 Jarek Sacha. All Rights Reserved.
 *
 * Author's e-mail: jpsacha at gmail.com
 */

package opencv2_cookbook.chapter08

import java.io.File

import opencv2_cookbook.OpenCVUtils._
import org.bytedeco.javacpp.opencv_core.KeyPointVector
import org.bytedeco.javacpp.opencv_features2d.FastFeatureDetector

/**
 * The example for section "Detecting FAST features" in Chapter 8, page 203.
 */
object Ex4FAST extends App {

  // Read input image
  val image = loadAndShowOrExit(new File("data/church01.jpg"))

  // Detect FAST features
  val ffd = FastFeatureDetector.create(
    40 /* threshold for detection */ ,
    true /* non-max suppression */ ,
    FastFeatureDetector.TYPE_9_16)
  val keyPoints = new KeyPointVector()
  ffd.detect(image, keyPoints)

  // Draw keyPoints
  show(drawOnImage(image, keyPoints), "FAST Features")
}
