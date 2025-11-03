package dev.webfx.demo.moderngauge;

import dev.webfx.kit.util.scene.DeviceSceneUtil;
import dev.webfx.platform.console.Console;
import dev.webfx.platform.os.OperatingSystem;
import dev.webfx.platform.scheduler.Scheduled;
import dev.webfx.platform.scheduler.Scheduler;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.WritableValue;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * @author Bruno Salmon
 */
public final class ModernGaugeApplication extends Application {

    @Override
    public void start(Stage stage) {
        Gauge gauge = GaugeBuilder.create()
                .skinType(Gauge.SkinType.MODERN)
                .prefSize(400, 400)
                .sectionTextVisible(true)
                .title("MODERN")
                .unit("UNIT")
                .thresholdVisible(true)
                .animated(true)
                .build();

        Text clickText = new Text(OperatingSystem.isMobile() ? "Tap to control" : "Click to control");

        Slider valueSlider = new Slider(0, 100, 0);
        Slider thresholdSlider = new Slider(0, 100, 85);
        Text valueText = createText("Value"), thresholdText = createText("Threshold");

        // Binding Gauge value and threshold with sliders
        gauge.valueProperty().bindBidirectional(valueSlider.valueProperty());
        gauge.tresholdProperty().bindBidirectional(thresholdSlider.valueProperty());

        // This improves slider performance on low devices (reduce click target possibilities)
        gauge.setMouseTransparent(true);
        Rectangle clip = new Rectangle();
        gauge.setClip(clip);

        Pane root = new Pane(gauge, clickText) {
            @Override
            public void layoutChildren() {
                double w = getWidth(), h = getHeight(), vtx, vty, vsx, vsy, ttx, tty, tsx, tsy, tw, th, sw, sh, gx, gy, gw, gh;
                Orientation so;
                Font textFont = Font.font("Verdana", Math.min(w, h) * 0.05);
                valueText.setFont(textFont);
                thresholdText.setFont(textFont);
                tw = thresholdText.getLayoutBounds().getWidth() * 1.2;
                if (w >= 1.3 * h) {
                    so = Orientation.VERTICAL; // Sliders orientation
                    sw = Math.max(tw, (w - h) / 2); sh = 0.8 * h; // Sliders area width & height
                    vsx = 0; vsy = (h - sh) / 2; // Value slider x & y
                    tsx = w - sw; tsy = vsy; // Threshold slider x & y
                    gw = w - 1.5 * sw; gh = h; // Gauge width & height
                    gx = w / 2 - gw / 2; gy = 0; // Gauge x & y
                    vtx = vsx; vty = 0; tw = sw; th = vsy; // Value text
                    ttx = tsx; tty = 0; // Threshold text
                } else {
                    vtx = ttx = 0;
                    so = Orientation.HORIZONTAL;  // Sliders orientation
                    sh = Math.max(40, (h - w) / 2); sw = 0.95 * (w - tw); // Slider areas width & height
                    vsx = tw ; vsy = 0; // Value Slider x & y
                    tsx = vsx; tsy = h - sh; // Threshold slider x & y
                    gx = 0; gy = sh; // Gauge x & y
                    gw = w; gh = h - 2 * sh; // Gauge width & height
                    vty = vsy; th = sh; // Value text
                    tty = tsy; // Threshold text
                }
                valueSlider.setOrientation(so);
                thresholdSlider.setOrientation(so);
                clip.setWidth(gw); clip.setHeight(gh);
                layoutInArea(clickText, 0, 0, w, h * 0.95, 0, HPos.CENTER, VPos.CENTER);
                layoutInArea(gauge, gx, gy, gw, gh, 0, HPos.CENTER, VPos.CENTER);
                layoutInArea(valueText, vtx, vty, tw, th, 0, HPos.CENTER, VPos.CENTER);
                layoutInArea(valueSlider, vsx, vsy, sw, sh, 0, HPos.CENTER, VPos.CENTER);
                layoutInArea(thresholdText, ttx, tty, tw, th, 0, HPos.CENTER, VPos.CENTER);
                layoutInArea(thresholdSlider, tsx, tsy, sw, sh, 0, HPos.CENTER, VPos.CENTER);
            }
        };
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        Scene scene = DeviceSceneUtil.newScene(root, 800, 600, Color.BLACK);
        stage.setScene(scene);
        stage.setTitle("Modern Gauge");
        stage.show();

        clickText.setFont(Font.font("Arial", FontWeight.BOLD, null, scene.getWidth() * 0.1));
        clickText.setFill(Color.CYAN);
        clickText.setEffect(new DropShadow());
        clickText.setMouseTransparent(true);
        animateProperty(5000, clickText.opacityProperty(), 0);

        Scheduled scheduled = Scheduler.schedulePeriodic(1500, () -> gauge.setValue(Math.random() * gauge.getRange() + gauge.getMinValue()));
        root.setCursor(Cursor.HAND);
        scene.setOnMouseClicked(e -> {
            scheduled.cancel();
            gauge.setAnimated(false);
            root.getChildren().setAll(gauge, valueText, valueSlider, thresholdText, thresholdSlider);
            root.setCursor(Cursor.DEFAULT);
            valueSlider.setCursor(Cursor.HAND);
            thresholdSlider.setCursor(Cursor.HAND);
        });
    }

    private Text createText(String s) {
        Text text = new Text(s);
        text.setFill(Color.WHITE);
        return text;
    }

    static <T> Timeline animateProperty(int durationMillis, WritableValue<T> target, T endValue) {
        Timeline timeline = new Timeline(new KeyFrame(new Duration(durationMillis), new KeyValue(target, endValue, Interpolator.EASE_BOTH)));
        timeline.play();
        return timeline;
    }

}
