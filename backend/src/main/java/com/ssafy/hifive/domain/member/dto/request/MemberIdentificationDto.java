package com.ssafy.hifive.domain.member.dto.request;

import com.ssafy.hifive.domain.member.entity.Member;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public final class MemberIdentificationDto {
	private String identificationImg;
	private String name;

	public Member toEntity(Member member) {
		return Member.builder()
			.identificationImg(identificationImg)
			.name(name)
			.build();
	}
}
