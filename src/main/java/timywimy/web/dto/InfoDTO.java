package timywimy.web.dto;

import timywimy.web.dto.common.BasicDTO;

public class InfoDTO implements BasicDTO {
    private String name = "TimyWimy API";
    private String version = "0.1";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
