package com.ssafy.hifive.domain.story.dto.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoryParam {
	@Schema(description = "페이지")
	public Integer page;
}
