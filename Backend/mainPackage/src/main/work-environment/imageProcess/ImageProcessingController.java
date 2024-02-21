package com.cs309.ta45.backend.mainPackage.imageProcess;


import org.apache.commons.io.FileUtils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Base64;

@RestController
public class ImageProcessingController {
    @Autowired
    private ImageRepository imageRepository;

    @GetMapping("/rotateImage")
    public Object rotateImage(){
        Image img = new Image();
        try {
            nu.pattern.OpenCV.loadLocally();

            String fname = "IowaStateUpLgoRight.jpg";
            byte[] fileContent = FileUtils.readFileToByteArray(new File(fname));
            String encodedString = Base64.getEncoder().encodeToString(fileContent);
            img.setFileName(fname);
            img.setBase64Encoding(encodedString);
            //imageRepository.save(img);

            Mat imageToProcess = Imgcodecs.imread(fname);
            Mat dst = new Mat();

            Core.rotate(imageToProcess,dst,Core.ROTATE_90_CLOCKWISE);
            Imgcodecs.imwrite("rotatedImage.jpg",dst);
            HighGui.imshow("rotated",dst);
        }catch (Exception e){
            e.printStackTrace();
        }

        return img;
    }

}
