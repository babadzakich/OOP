package ru.nsu.chuvashov.configholder;

import lombok.Getter;

@Getter
public class Tools {
    String criteria;
    String buildTool;
    public Tools(String criteria, String buildTool) {
        this.criteria = criteria;
        this.buildTool = buildTool;
    }

}
