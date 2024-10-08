package com.ssafy.hifive.global.config.websocket;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.hifive.global.error.ErrorCode;
import com.ssafy.hifive.global.error.type.BadRequestException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationWebSocketHandler extends TextWebSocketHandler {
	private final ConcurrentMap<Long, ConcurrentMap<String, WebSocketSession>> sessions = new ConcurrentHashMap<>();
	private final ConcurrentMap<Long, ConcurrentMap<Long, String>> memberSessionMap = new ConcurrentHashMap<>();
	private final ObjectMapper jacksonObjectMapper;

	@Override
	public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
		Long fanmeetingId = Long.parseLong(session.getUri().getPath().split("/")[3]);
		String sessionId = session.getId();
		Long memberId = (Long)session.getAttributes().get("memberId");

		sessions.putIfAbsent(fanmeetingId, new ConcurrentHashMap<>());
		memberSessionMap.putIfAbsent(fanmeetingId, new ConcurrentHashMap<>());

		if (memberSessionMap.get(fanmeetingId).containsKey(memberId)) {
			sendMessageToSession(fanmeetingId, memberId, "이미 세션이 연결된 사용자입니다.", "alreadyConnected");
			session.close();
			return;
		}

		sessions.get(fanmeetingId).put(sessionId, session);
		memberSessionMap.get(fanmeetingId).put(memberId, sessionId);

	}

	@Override
	public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) throws Exception {
		Long fanmeetingId = Long.parseLong(session.getUri().getPath().split("/")[3]);
		String sessionId = session.getId();
		Long memberId = (Long)session.getAttributes().get("memberId");

		if (sessions.containsKey(fanmeetingId)) {
			sessions.get(fanmeetingId).remove(sessionId);
			memberSessionMap.get(fanmeetingId).remove(memberId);
		}
	}

	public void isSessionValid(Long fanmeetingId, Long memberId) {
		//세션맵 자체가 초기화 되어있을 때
		if (memberSessionMap.isEmpty()) {
			log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@111111");
			throw new BadRequestException(ErrorCode.WEBSOCKET_NO_SESSION);
		}
		//세션맵에 해당하는 팬미팅이 없을 때
		if (memberSessionMap.get(fanmeetingId).isEmpty()) {
			log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@22222");
			throw new BadRequestException(ErrorCode.WEBSOCKET_NO_SESSION);
		}
		//sessionId가 없을 때
		String sessionId = memberSessionMap.get(fanmeetingId).get(memberId);
		if (sessionId == null) {
			log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@333333");
			throw new BadRequestException(ErrorCode.WEBSOCKET_NO_SESSION);
		}
	}

	public void sendMessageToSession(Long fanmeetingId, Long memberId, String message, String event) {
		String sessionId = memberSessionMap.get(fanmeetingId).get(memberId);

		// log.info("member 세션아이디{}", sessionId);
		// log.info("sessions.get(fanmeetingId){}", sessions.get(fanmeetingId));
		// log.info("memberSessionMap.get(fanmeetingId){}", memberSessionMap.get(fanmeetingId));
		WebSocketSession session = sessions.get(fanmeetingId).get(sessionId);
		// log.info("member 세션{}", session.isOpen());
		// log.info("member 세션{}", session.getUri());
		if (session != null && session.isOpen()) {
			try {
				String jsonMessage = jacksonObjectMapper.writeValueAsString(
					new WebSocketMessage(message, event));
				// log.info(jsonMessage.toString());
				session.sendMessage(new TextMessage(jsonMessage));
			} catch (Exception e) {
				// log.info("안보내짐");
				// log.info(e.getMessage());
				throw new BadRequestException(ErrorCode.WEBSOCKET_MESSAGE_SEND_ERROR);
			}
		} else {
			throw new BadRequestException(ErrorCode.WEBSOCKET_CONNECTION_ERROR);
		}
	}

	public void broadcastMessageToFanmeeting(Long fanmeetingId, WebSocketMessage webSocketMessage) throws Exception {
		ConcurrentMap<String, WebSocketSession> fanmeetingSessions = sessions.get(fanmeetingId);

		if (fanmeetingSessions != null) {
			String jsonMessage = jacksonObjectMapper.writeValueAsString(webSocketMessage);
			TextMessage textMessage = new TextMessage(jsonMessage);
			for (WebSocketSession session : fanmeetingSessions.values()) {
				if (session.isOpen()) {
					session.sendMessage(textMessage);
				}
			}
		}
	}
}
