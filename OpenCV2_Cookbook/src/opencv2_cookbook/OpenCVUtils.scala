/*
 * Copyright (c) 2011 Jarek Sacha. All Rights Reserved.
 *
 * Author's e-mail: jarek.listy at gmail.com
 */

package opencv2_cookbook

import com.googlecode.javacv.CanvasFrame
import javax.swing.JFrame
import com.googlecode.javacv.cpp.opencv_highgui._
import java.awt.{Color, Graphics2D, Image}
import java.awt.geom.Ellipse2D
import com.googlecode.javacv.cpp.opencv_core._
import com.googlecode.javacv.cpp.opencv_features2d.KeyPoint
import java.io.{FileNotFoundException, IOException, File}


object OpenCVUtils {

    /**
     * Load an image and show in a CanvasFrame.
     *
     * If image cannot be loaded the application will exit with code 1.
     *
     * @param file image file
     * @param flags Flags specifying the color type of a loaded image:
     * <ul>
     *     <li> {@code >0} Return a 3-channel color image</li>
     *     <li> {@code =0} Return a grayscale image</li>
     *     <li> {@code <0} Return the loaded image as is. Note that in the current implementation
     *     the alpha channel, if any, is stripped from the output image. For example, a 4-channel
     *     RGBA image is loaded as RGB if flags {@code >} 0.</li>
     * </ul>
     * Default is grayscale.
     * @return Loaded image
     */
    def loadAndShowOrExit(file: File, flags: Int = CV_LOAD_IMAGE_GRAYSCALE): IplImage = {
        try {
            val image = load(file, flags)
            show(image, file.getName)
            image
        }
        catch {
            case ex: IOException => {
                println("Couldn't load image: " + file.getAbsolutePath)
                sys.exit(1)
            }
        }
    }


    /**
     * Load an image.
     *
     * If image cannot be loaded the application will exit with code 1.
     *
     * @param file image file
     * @param flags Flags specifying the color type of a loaded image:
     * <ul>
     *     <li> {@code >0} Return a 3-channel color image</li>
     *     <li> {@code =0} Return a grayscale image</li>
     *     <li> {@code <0} Return the loaded image as is. Note that in the current implementation
     *     the alpha channel, if any, is stripped from the output image. For example, a 4-channel
     *     RGBA image is loaded as RGB if flags {@code >} 0.</li>
     * </ul>
     * Default is grayscale.
     * @throw FileNotFoundException when file does not exist
     * @throw IOException when image cannot be read
     * @return Loaded image
     */
    def load(file: File, flags: Int = CV_LOAD_IMAGE_GRAYSCALE): IplImage = {
        // Verify file
        if (!file.exists()) {
            throw new FileNotFoundException("Image file does not exist: " + file.getAbsolutePath)
        }
        // Read input image
        val image = cvLoadImage(file.getAbsolutePath, flags)
        if (image == null) {
            throw new IOException("Couldn't load image: " + file.getAbsolutePath)
        }
        image
    }


    /**
     * Load an image and show in a CanvasFrame. If image cannot be loaded the application will exit with code 1.
     * @param flags Flags specifying the color type of a loaded image:
     * <ul>
     *     <li> {@code >0} Return a 3-channel color image</li>
     *     <li> {@code =0} Return a grayscale image</li>
     *     <li> {@code <0} Return the loaded image as is. Note that in the current implementation
     *     the alpha channel, if any, is stripped from the output image. For example, a 4-channel
     *     RGBA image is loaded as RGB if flags {@code >} 0.</li>
     * </ul>
     * Default is grayscale.
     * @return loaded image
     */
    def loadMatAndShowOrExit(file: File, flags: Int = CV_LOAD_IMAGE_GRAYSCALE): CvMat = {
        // Read input image
        val image = loadMatOrExit(file, flags)
        show(image, file.getName)
        image
    }


    /**
     * Load an image. If image cannot be loaded the application will exit with code 1.
     * @param flags Flags specifying the color type of a loaded image:
     * <ul>
     *     <li> {@code >0} Return a 3-channel color image</li>
     *     <li> {@code =0} Return a grayscale image</li>
     *     <li> {@code <0} Return the loaded image as is. Note that in the current implementation
     *     the alpha channel, if any, is stripped from the output image. For example, a 4-channel
     *     RGBA image is loaded as RGB if flags {@code >} 0.</li>
     * </ul>
     * Default is grayscale.
     * @return loaded image
     */
    def loadMatOrExit(file: File, flags: Int = CV_LOAD_IMAGE_GRAYSCALE): CvMat = {
        // Read input image
        val image = cvLoadImageM(file.getAbsolutePath, flags)
        if (image == null) {
            println("Couldn't load image: " + file.getAbsolutePath)
            sys.exit(1)
        }
        image
    }


    /**
     * Show image in a window. Closing the window will exit the application.
     */
    def show(image: IplImage, title: String) {
        val canvas = new CanvasFrame(title)
        canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
        canvas.showImage(image)
    }


    /**
     * Convert in {@code CvMat} object to {@code IplImage}.
     */
    def toIplImage(mat: CvMat): IplImage = {
        val image = cvCreateImage(mat.cvSize(), mat.elemSize(), 1)
        cvGetImage(mat, image)
        image
    }


    /**
     * Show image in a window. Closing the window will exit the application.
     */
    def show(mat: CvMat, title: String) {
        val canvas = new CanvasFrame(title)
        canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
        canvas.showImage(toIplImage(mat))
    }


    /**
     * Show image in a window. Closing the window will exit the application.
     */
    def show(image: Image, title: String) {
        val canvas = new CanvasFrame(title)
        canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
        canvas.showImage(image)
    }


    /**
     *     Draw circles at feature point locations on an image.
     */
    def drawOnImage(image: IplImage, points: CvPoint2D32f): Image = {
        //        val color = CvScalar.WHITE
        //        val radius: Int = 3
        //        val thickness: Int = 2
        //        points.foreach(p => {
        //            println("(" + p.x + ", " + p.y + ")")
        //            val center = new CvPoint(new CvPoint2D32f(p.x, p.y))
        //            cvCircle(image, center, radius, color, thickness, 8, 0)
        //        })

        // OpenCV drawing seems to crash a lot, so use Java2D
        val radius = 3;
        val bi = image.getBufferedImage
        val g2d = bi.getGraphics.asInstanceOf[Graphics2D]
        val w = radius * 2;
        g2d.setColor(Color.WHITE)

        val n = points.capacity()
        println("n: " + n)
        for (i <- 0 until n) {
            val p = points.position(i)
            g2d.draw(new Ellipse2D.Double(p.x - radius, p.y - radius, w, w))
        }

        bi
    }


    /**
     *  Draw circles at key point locations on an image.
     *  Circle radius is proportional to key point size.
     */
    def drawOnImage(image: IplImage, points: Array[KeyPoint]): Image = {

        // OpenCV drawing seems to crash a lot, so use Java2D
        val minR = 2
        val bi = image.getBufferedImage
        val g2d = bi.getGraphics.asInstanceOf[Graphics2D]
        g2d.setColor(Color.WHITE)

        points.foreach(p => {
            val radius = p.size() / 2
            val r = if (radius == Float.NaN || radius < minR) minR else radius
            val pt = p.pt
            g2d.draw(new Ellipse2D.Double(pt.x - r, pt.y - r, r * 2, r * 2))
        })

        bi
    }
}