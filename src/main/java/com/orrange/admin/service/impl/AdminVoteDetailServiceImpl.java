package com.orrange.admin.service.impl;

import com.orrange.admin.mapper.AdminVoteDetailMapper;
import com.orrange.admin.service.AdminVoteDetailService;
import com.orrange.admin.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class AdminVoteDetailServiceImpl implements AdminVoteDetailService {
    @Autowired
    private AdminVoteDetailMapper mapper;

    @Override
    public AdminVoteDetailVO getDetail(Integer id, Boolean withStats, Boolean withQuestions) {
        if (withStats == null) withStats = true;
        if (withQuestions == null) withQuestions = true;

        AdminVoteDetailVO base = mapper.selectBase(id);
        if (base == null) return null;

        // ensure counts exist
        if (base.getQuestionCount() == null) base.setQuestionCount(mapper.countQuestion(id));
        if (base.getTotalVotes() == null) base.setTotalVotes(mapper.countTotalVotes(id));

        if (withStats) {
            int eligible = Optional.ofNullable(mapper.countEligible(id)).orElse(0);
            int participants = Optional.ofNullable(mapper.countParticipants(id)).orElse(0);
            int ballots = Optional.ofNullable(mapper.countTotalVotes(id)).orElse(0);
            double rate = 0.0;
            if (eligible > 0) {
                rate = BigDecimal.valueOf((double) participants / (double) eligible)
                        .setScale(2, RoundingMode.HALF_UP).doubleValue();
            }
            AdminVoteSummaryVO summary = new AdminVoteSummaryVO();
            summary.setEligible(eligible);
            summary.setParticipants(participants);
            summary.setParticipationRate(rate);
            summary.setBallots(ballots);
            base.setSummary(summary);
        }

        if (withQuestions) {
            List<AdminVoteQuestionVO> questions = mapper.listQuestionsBase(id);
            if (questions == null) questions = new ArrayList<>();
            // load options (flat) then attach per question
            List<AdminVoteQuestionOptionVO> flat = mapper.listQuestionOptions(id);
            Map<Integer, List<AdminVoteQuestionOptionVO>> byQ = new HashMap<>();
            if (flat != null) {
                for (AdminVoteQuestionOptionVO opt : flat) {
                    // we need questionId from result set; adapt by casting via reflection map? Better: re-query with map result
                }
            }
            // Since our VO lacks questionId in option, refetch as map
            List<Map<String, Object>> optRows = mapper.listQuestionOptionsAsMap(id);
            Map<Integer, List<AdminVoteQuestionOptionVO>> grouped = new HashMap<>();
            if (optRows != null) {
                for (Map<String, Object> r : optRows) {
                    Integer qid = ((Number) r.get("questionId")).intValue();
                    String text = (String) r.get("text");
                    Integer count = ((Number) r.getOrDefault("count", 0)).intValue();
                    AdminVoteQuestionOptionVO o = new AdminVoteQuestionOptionVO();
                    o.setText(text);
                    o.setCount(count);
                    grouped.computeIfAbsent(qid, k -> new ArrayList<>()).add(o);
                }
            }
            for (AdminVoteQuestionVO q : questions) {
                List<AdminVoteQuestionOptionVO> opts = grouped.getOrDefault(q.getQuestionId(), Collections.emptyList());
                int sum = 0;
                for (AdminVoteQuestionOptionVO o : opts) sum += Optional.ofNullable(o.getCount()).orElse(0);
                for (AdminVoteQuestionOptionVO o : opts) {
                    double ratio = (sum == 0) ? 0.0 : BigDecimal.valueOf((double) o.getCount() / (double) sum)
                            .setScale(3, RoundingMode.HALF_UP).doubleValue();
                    o.setRatio(ratio);
                }
                q.setOptions(opts);
                AdminVoteQuestionSeriesVO s = new AdminVoteQuestionSeriesVO();
                List<String> labels = new ArrayList<>();
                List<Integer> counts = new ArrayList<>();
                List<Double> ratios = new ArrayList<>();
                for (AdminVoteQuestionOptionVO o : opts) {
                    labels.add(o.getText());
                    counts.add(o.getCount());
                    ratios.add(o.getRatio());
                }
                s.setLabels(labels);
                s.setCounts(counts);
                s.setRatios(ratios);
                q.setSeries(s);
            }
            base.setQuestions(questions);
        }
        return base;
    }
}

