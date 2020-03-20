package com.selbekk.lars.timerapp;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SkinBase;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;

import java.io.IOException;

public class RadialProgressIndicatorSkin extends SkinBase<ProgressIndicator> {
    ProgressIndicator control;
    DoubleProperty angleCompletedProperty = new SimpleDoubleProperty();
    DoubleProperty progressProperty;

    @FXML
    Circle fullRing;
    @FXML
    Arc partialRing;

    public RadialProgressIndicatorSkin(ProgressIndicator control) {
        super(control);
        this.control = control;
        progressProperty = control.progressProperty();
        angleCompletedProperty.bind(control.progressProperty().multiply(360));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("RadialProgressIndicator.fxml"));
        loader.setController(this);
        try {
            Parent parent = loader.load();
            this.getChildren().setAll(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }

        control.getStyleClass().setAll("radial");
        control.getStylesheets().add(getClass().getResource("radialProgressIndicator.css").toExternalForm());

        control.setMouseTransparent(true);
    }

    public void initialize() {
        partialRing.lengthProperty().bind(angleCompletedProperty);
        setProgress(0.75);
    }

    public void setProgress(double progress) {
        control.progressProperty().set(progress);
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        super.layoutChildren(contentX, contentY, contentWidth, contentHeight);
    }

    @Override
    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
//        return computePrefWidth(height, topInset, rightInset, bottomInset, leftInset);
        return 200;
    }

    @Override
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
//        return computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
        return 200;
    }

    @Override
    protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
//        return computePrefWidth(height, topInset, rightInset, bottomInset, leftInset);
        return 300;
    }

    @Override
    protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
//        return computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
        return 300;
    }

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return 200;
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return 300;
    }
}
