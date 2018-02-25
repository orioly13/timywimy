package timywimy.web.dto;

import timywimy.web.dto.common.BasicDTO;

public class InfoDTO implements BasicDTO {
    private String name;
    private String version;

    public InfoDTO(String name, String version) {
        this.name = name;
        this.version = version;
    }

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
