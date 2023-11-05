package gui.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;

import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class Files {

    public static File chooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Escolha a imagem");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            return selectedFile;
        }

        return null;
    }

    public static String copyPasteImage(Path source) {
        String DestinyPath = "imgs/assets_images";

        String fileName = source.getFileName().toString();
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > 0) {
            String extension = fileName.substring(dotIndex);
            String newFileName = String.valueOf(Instant.now().toEpochMilli()) + extension;
            Path copiedImage = Paths.get(DestinyPath, newFileName);
            try {
                java.nio.file.Files.copy(source, copiedImage, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                Alerts.showAlert("IOException", "Failed to copy image", e.getMessage(), AlertType.ERROR);
            }

            return copiedImage.toUri().toString();
        } else {
            return null;
        }

    }
}
