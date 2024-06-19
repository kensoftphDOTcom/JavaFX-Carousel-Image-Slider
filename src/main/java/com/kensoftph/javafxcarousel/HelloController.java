package com.kensoftph.javafxcarousel;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.MotionBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HelloController {
    @FXML
    private Button next;
    @FXML
    private Button prev;
    @FXML
    private StackPane stackPane;

    private List<Image> imageList = new ArrayList<>();
    private int currentIndex = 0;

    @FXML
    private void prev() {
        if (imageList == null || imageList.isEmpty()) {
            return;
        }
        currentIndex = (currentIndex - 1 + imageList.size()) % imageList.size();
        slideImage(-600);
    }

    @FXML
    private void next() {
        if (imageList == null || imageList.isEmpty()) {
            return;
        }
        currentIndex = (currentIndex + 1) % imageList.size();
        slideImage(600);
    }

    @FXML
    private void loadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Images");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        List<File> selectedImages = fileChooser.showOpenMultipleDialog(stackPane.getScene().getWindow());
        if (selectedImages != null) {
            imageList.clear();

            for (File file : selectedImages) {
                imageList.add(new Image(file.toURI().toString()));
            }

            if (!imageList.isEmpty()) {
                currentIndex = 0;

                ImageView imageView = new ImageView(imageList.get(currentIndex));
                imageView.setPreserveRatio(true);
                imageView.setFitHeight(500);
                imageView.setFitWidth(500);

                stackPane.getChildren().add(imageView);
            }
        }
    }

    private void slideImage(double distance) {
        ImageView newImage = new ImageView(imageList.get(currentIndex));
        ImageView oldImage = (ImageView) stackPane.getChildren().getFirst();

        newImage.setFitHeight(500);
        newImage.setFitWidth(500);
        newImage.setPreserveRatio(true);

        TranslateTransition newImageTransition = new TranslateTransition(Duration.millis(300), newImage);
        TranslateTransition oldImageTransition = new TranslateTransition(Duration.millis(300), oldImage);

        newImageTransition.setFromX(distance);
        newImageTransition.setToX(0);

        oldImageTransition.setByX(-distance);

        next.setDisable(true);
        prev.setDisable(true);

        MotionBlur motionBlur = new MotionBlur();
        motionBlur.setRadius(50);

        stackPane.getChildren().add(newImage);
        newImage.setEffect(motionBlur);
        oldImage.setEffect(motionBlur);

        newImageTransition.setOnFinished(event -> {
            next.setDisable(false);
            prev.setDisable(false);
            newImage.setEffect(null);
            oldImage.setEffect(null);
        });

        oldImageTransition.setOnFinished(event -> {
            stackPane.getChildren().remove(oldImage);
            next.setDisable(false);
            prev.setDisable(false);
            newImage.setEffect(null);
            oldImage.setEffect(null);
        });

        newImageTransition.play();
        oldImageTransition.play();
    }
}