package mainPackage.imageProcess;

import jakarta.persistence.*;

@Entity
@TableGenerator(
        name = "imageGenerator",
        allocationSize = 1,
        initialValue = 1)
@Table(name="images")
public class Image {
    @Column(name = "fileName")
    private String fileName;

    private String base64Encoding;

    @Id
    @GeneratedValue(
            strategy=GenerationType.TABLE,
            generator="imageGenerator")
    @Column(name="id")
    private int id;

    // Constructor with filename and Base64 encoding
    public Image(String fileName, String base64Encoding) {
        this.fileName = fileName;
        this.base64Encoding = base64Encoding;
    }

    public Image() {
    }

    public int getId(){
        return id;
    }
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getBase64Encoding() {
        return base64Encoding;
    }

    public void setBase64Encoding(String base64Encoding) {
        this.base64Encoding = base64Encoding;
    }

}
