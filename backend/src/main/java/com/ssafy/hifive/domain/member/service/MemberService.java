package com.ssafy.hifive.domain.member.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ssafy.hifive.domain.member.dto.request.MemberIdentificationDto;
import com.ssafy.hifive.domain.member.dto.request.MemberNicknameDto;
import com.ssafy.hifive.domain.member.dto.request.MemberUpdateDto;
import com.ssafy.hifive.domain.member.dto.response.MemberResponseDto;
import com.ssafy.hifive.domain.member.entity.Member;
import com.ssafy.hifive.domain.member.repository.MemberRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

	public Member findByEmail(String email) {
		return memberRepository.findByEmail(email)
			.orElseThrow(() -> new EntityNotFoundException("User Not Found"));
	}

	public Member findById(Member member) {
		return memberRepository.findById(member.getMemberId())
			.orElseThrow(() -> new EntityNotFoundException("User Not Found"));
	}

	public ResponseEntity<MemberResponseDto> getMemberDetail(Member member) {
		log.info("getMemberDetail" + member.getMemberId());
		return ResponseEntity.ok(null);
	}

	public void nicknameCheck(MemberNicknameDto memberNicknameDto, Member member) {

	}

	public void updateMember(MemberUpdateDto updateMember, Member member) {

	}

	public void createIdentification(MemberIdentificationDto memberIdentificationDto, Member member) {

	}

	public void deleteMember(Member member) {

	}

}
