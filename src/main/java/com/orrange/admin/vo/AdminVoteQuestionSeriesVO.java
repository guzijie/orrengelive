package com.orrange.admin.vo;

import java.util.List;

public class AdminVoteQuestionSeriesVO {
    private List<String> labels;
    private List<Integer> counts;
    private List<Double> ratios;

    public List<String> getLabels() { return labels; }
    public void setLabels(List<String> labels) { this.labels = labels; }
    public List<Integer> getCounts() { return counts; }
    public void setCounts(List<Integer> counts) { this.counts = counts; }
    public List<Double> getRatios() { return ratios; }
    public void setRatios(List<Double> ratios) { this.ratios = ratios; }
}
