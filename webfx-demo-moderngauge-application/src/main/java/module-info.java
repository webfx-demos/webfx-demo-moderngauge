// File managed by WebFX (DO NOT EDIT MANUALLY)

module webfx.demo.moderngauge.application {

    // Direct dependencies modules
    requires javafx.base;
    requires javafx.controls;
    requires javafx.graphics;
    requires webfx.kit.util.scene;
    requires webfx.lib.medusa;
    requires webfx.platform.console;
    requires webfx.platform.os;
    requires webfx.platform.scheduler;

    // Exported packages
    exports dev.webfx.demo.moderngauge;

    // Provided services
    provides javafx.application.Application with dev.webfx.demo.moderngauge.ModernGaugeApplication;

}