package com.orrange.admin.vo;

public class AdminVoteSummaryVO {
    private Integer eligible;
    private Integer participants;
    private Double participationRate;
    private Integer ballots;

    public Integer getEligible() { return eligible; }
    public void setEligible(Integer eligible) { this.eligible = eligible; }
    public Integer getParticipants() { return participants; }
    public void setParticipants(Integer participants) { this.participants = participants; }
    public Double getParticipationRate() { return participationRate; }
    public void setParticipationRate(Double participationRate) { this.participationRate = participationRate; }
    public Integer getBallots() { return ballots; }
    public void setBallots(Integer ballots) { this.ballots = ballots; }
}
