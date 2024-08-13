package com.ssafy.hifive.domain.openvidu.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.hifive.domain.openvidu.dto.response.OpenViduRecordDto;
import com.ssafy.hifive.global.error.ErrorCode;
import com.ssafy.hifive.global.error.type.BadRequestException;
import com.ssafy.hifive.global.error.type.DataNotFoundException;

import io.openvidu.java.client.OpenVidu;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import io.openvidu.java.client.Recording;
import io.openvidu.java.client.RecordingLayout;
import io.openvidu.java.client.RecordingProperties;
import io.openvidu.java.client.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenViduRecordService {
	@Value("${openvidu.secret}")
	private String openviduSecret;

	private static final String path = "/app/recordings";

	public OpenViduRecordDto recordVideo(OpenVidu openVidu, String fanmeetingId) throws
		OpenViduJavaClientException, OpenViduHttpException {
		Session session = openVidu.getActiveSession(fanmeetingId);
		if (session == null) {
			throw new DataNotFoundException(ErrorCode.FANMEETING_NO_SESSION, "해당 세션 Id가 존재하지 않습니다.");
		}
		session.fetch();

		RecordingProperties properties = new RecordingProperties.Builder()
			.name("fanmeeting-" + fanmeetingId)
			.outputMode(Recording.OutputMode.INDIVIDUAL)
			.recordingLayout(RecordingLayout.BEST_FIT)
			.build();

		return OpenViduRecordDto.from(openVidu.startRecording(fanmeetingId, properties).getId());
	}

	public void stopRecordVideo(OpenVidu openVidu, String recordId) throws
		Exception {
		if (openVidu.getRecording(recordId) == null) {
			throw new BadRequestException(ErrorCode.RECORDING_NOT_FOUND, "Record가 존재하지 않습니다." + recordId);
		}
		openVidu.stopRecording(recordId);

		log.info(openVidu.getRecording(recordId).getUrl());
		File donwloadZip = downloadFile(openVidu.getRecording(recordId).getUrl(), recordId);
		processDownloadedZip(donwloadZip);
	}

	private File downloadFile(String fileUrl, String fileName) throws IOException {
		URL url = new URL(fileUrl);
		File destinationFile = new File(path, fileName + ".zip");
		// Create URL connection
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		try {
			// Set HTTP method
			connection.setRequestMethod("GET");

			// Set authorization header
			String auth = "OPENVIDUAPP" + ":" + openviduSecret;
			String encodeAuth = Base64.getEncoder().encodeToString(auth.getBytes());
			String authHeader = "Basic " + encodeAuth;
			connection.setRequestProperty("Authorization", authHeader);

			// Check response code
			int responseCode = connection.getResponseCode();
			if (responseCode != HttpURLConnection.HTTP_OK) {
				throw new IOException("HTTP error code: " + responseCode);
			}

			// Read the input stream
			try (InputStream inputStream = connection.getInputStream();
				 FileOutputStream out = new FileOutputStream(destinationFile)) {
				byte[] buffer = new byte[1024];
				int bytesRead;
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					out.write(buffer, 0, bytesRead);
				}
			}
		} finally {
			connection.disconnect();
		}
		return destinationFile;
	}

	private void processDownloadedZip(File zipFile) throws IOException {
		Map<String, String> userIdToFileMap = new HashMap<>();

		try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
			ZipEntry zipEntry;
			while ((zipEntry = zis.getNextEntry()) != null) {
				if (zipEntry.getName().endsWith(".json")) {
					File jsonFile = extractFile(zis, zipEntry.getName());
					userIdToFileMap = processJsonFile(jsonFile);
				}
			}
			zis.closeEntry();
		}
	}

	private Map<String, String> processJsonFile(File jsonFile) throws IOException {
		Map<String, String> userIdToFileMap = new HashMap<>();
		ObjectMapper objectMapper = new ObjectMapper();

		JsonNode rootNode = objectMapper.readTree(jsonFile);
		JsonNode filesNode = rootNode.get("files");

		if (filesNode.isArray()) {
			for (JsonNode fileNode : filesNode) {
				log.info("1231241242");
				log.info("fileNode : ", fileNode);
				log.info("1231241242");
				String fileName = fileNode.get("name").asText();
				String userId = fileNode.get("clientData").get("userId").asText();
				userIdToFileMap.put(userId, fileName);
				log.info("=================================");
				log.info("File Name : " + fileName + " |||||||| UserId : " + userId);
				log.info("=================================");
			}
		}
		return userIdToFileMap;
	}

	private File extractFile(InputStream inputStream, String fileName) throws IOException {
		File extractedFile = new File(path, fileName);
		try (FileOutputStream fos = new FileOutputStream(extractedFile)) {
			byte[] buffer = new byte[1024];
			int length;
			while ((length = inputStream.read(buffer)) > 0) {
				fos.write(buffer, 0, length);
			}
		}
		return extractedFile;
	}

	private String getUserIdFromMap(Map<String, String> userIdToFileMap, String fileName) {
		return userIdToFileMap.getOrDefault(fileName, null);
	}
}
