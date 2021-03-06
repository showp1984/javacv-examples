/*
 * Copyright (c) 2011-2014 Jarek Sacha. All Rights Reserved.
 *
 * Author's e-mail: jpsacha at gmail.com
 */

package opencv2_cookbook.chapter05


import java.io.File

import opencv2_cookbook.OpenCVUtils._


/**
 * Example of detecting edges and corners using morphological filters. Based on section "Detecting edges and
 * corners using morphological filters".
 */
object Ex3EdgesAndCorners extends App {

  // Read input image
  val image = loadIplAndShowOrExit(new File("data/building.jpg"))

  val morpho = new MorphoFeatures
  morpho.threshold = 40

  val edges = morpho.getEdges(image)
  show(edges, "Edges")

  val corners = morpho.getCorners(image)
  val cornersOnImage = morpho.drawOnImage(corners, image)
  show(cornersOnImage, "Corners on image")
}


