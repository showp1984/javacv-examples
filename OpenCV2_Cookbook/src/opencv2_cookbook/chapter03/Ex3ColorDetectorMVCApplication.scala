/*
 * Copyright (c) 2011-2012 Jarek Sacha. All Rights Reserved.
 *
 * Author's e-mail: jarek.listy at gmail.com
 */

package opencv2_cookbook.chapter03

import com.googlecode.javacv.cpp.opencv_core.IplImage
import java.awt.Cursor._
import javax.swing.{JColorChooser, ImageIcon}
import swing._
import Dialog.Message.Error
import FileChooser.Result.Approve
import GridBagPanel.Fill
import event.{ButtonClicked, ValueChanged}


/**
 * Example for section "Using a Model-View-Controller architecture to design an application" in Chapter 3.
 * This object corresponds to the MainWindow class in the C++ code.
 */
object Ex3ColorDetectorMVCApplication extends SimpleSwingApplication {
    private lazy val fileChooser = new FileChooser
    private val controller = ColorDetectorController


    def top = new MainFrame {
        title = "Color Detector MVC"

        val openImageButton = new Button("Open Image")
        val processImageButton = new Button("Process Image") {
            enabled = false
        }
        val selectColorButton = new Button("Select Color")

        // Component for displaying the image
        val imageView = new Label

        // Color Distance Threshold components
        val colorDistanceLabel = new Label("Color Distance Threshold: ???")
        colorDistanceLabel.horizontalAlignment = Alignment.Leading
        val colorDistanceSlider = new Slider() {
            min = 0
            max = 3 * 255
        }

        // Listen and react to user actions
        listenTo(
            openImageButton,
            selectColorButton,
            colorDistanceSlider,
            processImageButton
        )
        reactions += {
            case ButtonClicked(`openImageButton`) => onOpenImage()
            case ButtonClicked(`selectColorButton`) => onSelectColor()
            case ButtonClicked(`processImageButton`) => onProcessImage()
            case ValueChanged(`colorDistanceSlider`) => onColorDistanceSliderChange()
        }

        // Create vertical buttons panel
        val buttonsPanel = new GridBagPanel {
            private val c = new Constraints {
                fill = Fill.Horizontal
                gridx = 0
                gridy = 0
                weightx = 0.5
                insets = new Insets(5, 5, 5, 5)
            }

            // Open image
            add(openImageButton)

            // Options
            add(new Separator())
            add(selectColorButton)
            add(colorDistanceLabel)
            add(colorDistanceSlider)
            add(new Separator())

            // Process image
            add(processImageButton)

            /**
             * Add component below previous one
             */
            private def add(component: Component) {
                c.gridy += 1
                layout(component) = c
            }
        }

        // Layout frame contents
        contents = new BorderPanel() {
            // Action buttons on the left
            add(new FlowPanel(buttonsPanel), BorderPanel.Position.West)
            // Image display in the center
            val imageScrollPane = new ScrollPane(imageView) {
                preferredSize = new Dimension(640, 480)
            }
            add(imageScrollPane, BorderPanel.Position.Center)
        }

        // Mark for display in the center of the screen
        centerOnScreen()

        // Sync display for the first time
        onColorDistanceSliderChange()

        /**
         * Show the wit cursor while given code `op` is executing.
         */
        def waitCursor(op: => Unit) {
            val previous = cursor
            cursor = getPredefinedCursor(WAIT_CURSOR)
            try {
                op
            } finally {
                cursor = previous
            }
        }

        def display(image: Option[IplImage]) {
            image match {
                case Some(x) => imageView.icon = new ImageIcon(x.getBufferedImage)
                case None => {}
            }
        }

        /**
         * Ask user for location and open new image.
         */
        def onOpenImage() {
            waitCursor {
                // Ask user for the location of the image file
                if (fileChooser.showOpenDialog(buttonsPanel) != Approve) {
                    return
                }

                // Load the image
                val path = fileChooser.selectedFile.getAbsoluteFile
                if (path == null) {
                    return;
                }

                // Load image and update display.
                if (controller.setInputImage(path.getAbsolutePath)) {
                    display(controller.inputImage)
                    processImageButton.enabled = true
                } else {
                    Dialog.showMessage(buttonsPanel, "Cannot open image file: " + path, top.title, Error)
                    None
                }
            }
        }

        /**
         * Select target color.
         */
        def onSelectColor() {
            waitCursor {
                val color = JColorChooser.showDialog(buttonsPanel.self, "Select Target Color", controller.targetColor)
                if (color != null) {
                    controller.targetColor = color
                }
            }
        }

        /**
         * Process input image.
         */
        def onProcessImage() {
            waitCursor {
                // Process and update image display if image is loaded
                controller.inputImage match {
                    case Some(image) =>
                        controller.process()
                        imageView.icon = new ImageIcon(controller.result.get.getBufferedImage)
                    case None =>
                        Dialog.showMessage(buttonsPanel, "Image not opened", title, Error)
                }
            }
        }

        /**
         * Set color distance threshold to current value of the `colorDistanceSlider`.
         */
        def onColorDistanceSliderChange() {
            val value = colorDistanceSlider.value
            controller.colorDistanceThreshold = value
            colorDistanceLabel.text = "Color Distance Threshold: " + value
        }
    }
}