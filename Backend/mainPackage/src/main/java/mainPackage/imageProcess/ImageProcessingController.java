package mainPackage.imageProcess;


import org.apache.commons.io.FileUtils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.File;
import java.util.Base64;

@Component
public class ImageProcessingController {
    @Autowired
    private ImageRepository imageRepository;

    //create
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
            //Mat imageToSave = Imgcodecs.imread(fname);
            Core.rotate(imageToProcess,dst,Core.ROTATE_90_CLOCKWISE);
            Imgcodecs.imwrite("rotatedImage.jpg",dst);
            //HighGui.imshow("rotated",dst);
        }catch (Exception e){
            e.printStackTrace();
        }

        return img;
    }

    // Create Image
    @PostMapping("img/save")
    public Object saveImage(@RequestBody Image img){
        try {
            nu.pattern.OpenCV.loadLocally();

            byte[] decoded = Base64.getDecoder().decode(img.getBase64Encoding());
            String fileName = img.getFileName();

            // Save the decoded image to the specified file path
            FileUtils.writeByteArrayToFile(new File(fileName), decoded);

            // Read the saved image as bytes again
            byte[] savedImageBytes = FileUtils.readFileToByteArray(new File(fileName));

            // Encode the saved image bytes as Base64 string
            return Base64.getEncoder().encodeToString(savedImageBytes);
            // FileUtils.writeByteArrayToFile(new File(img.getFileName()),decoded);
//            imageRepository.save(img);

        } catch (Exception e){
            e.printStackTrace();
            return "internal server error";
        }
    }


}
