package com.readytoplanbe.myapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "custom.application")
public class CustomProperties {
    private Export export = new Export();

    public static class Export {
        private String tempDir;

        public String getTempDir() {
            return tempDir;
        }

        public void setTempDir(String tempDir) {
            this.tempDir = tempDir;
        }
    }

    public Export getExport() {
        return export;
    }

    public void setExport(Export export) {
        this.export = export;
    }
}

