/*
 * Copyright (c) 2011-2014 Jarek Sacha. All Rights Reserved.
 *
 * Author's e-mail: jpsacha at gmail.com
 */

package opencv2_cookbook;

import org.bytedeco.javacpp.opencv_xfeatures2d.SURF;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * @author Jarek Sacha
 */
public final class DescriptorExtractorTest {


    @Test
    public void create() throws Exception {

        // Test for bug https://github.com/bytedeco/javacpp-presets/issues/3

        SURF surfDesc = SURF.create();
        assertNotNull(surfDesc);
    }
}