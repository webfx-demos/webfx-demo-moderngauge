// File managed by WebFX (DO NOT EDIT MANUALLY)

module webfx.demo.moderngauge.application {

    // Direct dependencies modules
    requires javafx.controls;
    requires javafx.graphics;
    requires webfx.lib.medusa;

    // Exported packages
    exports dev.webfx.demo.moderngauge;

    // Provided services
    provides javafx.application.Application with dev.webfx.demo.moderngauge.ModernGaugeApplication;

}