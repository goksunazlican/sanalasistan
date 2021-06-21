package com.virtualassistant.vaws.plan.model;

import java.time.LocalDateTime;

import com.virtualassistant.vaws.user.model.LoginRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyPlan {
	private String title;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
}
