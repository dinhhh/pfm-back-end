package com.hust.pfmbackend.model.response.statistic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@Builder
@ToString
/**
 * Equivalent format with dataPoints property in CanvasJSChart class (front-end)
 */
public class Point {

    private String label;
    private double y; // in billion VND

}
