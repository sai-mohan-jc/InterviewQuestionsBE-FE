
package com.banking.aiassistant.rag.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import com.banking.aiassistant.rag.dto.KycRagResponse;
import com.banking.aiassistant.rag.dto.KycSourceReference;

@Service
public class KycRagService {

	private final ChatClient chatClient;
	private final QuestionAnswerAdvisor qaAdvisor;
	private final VectorStore vectorStore;

	public KycRagService(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {

		this.chatClient = chatClientBuilder.build();
		this.vectorStore = vectorStore;

		this.qaAdvisor = QuestionAnswerAdvisor.builder(vectorStore)
				.searchRequest(SearchRequest.builder().topK(3).similarityThreshold(0.2).build()).build();
	}

	public KycRagResponse askKycQuestion(String question) {

		var response = chatClient.prompt().system("""
				You are a banking KYC policy assistant.
				Answer questions using the retrieved KYC
				policy document context.
				Do not invent policy information.
				If the answer is not available in the
				retrieved context, clearly inform the user.
				""").user(question).advisors(qaAdvisor).call().chatClientResponse();

		String answer = response.chatResponse().getResult().getOutput().getText();


@SuppressWarnings("unchecked")
List<Document> documents = (List<Document>) response.context()
    .get(QuestionAnswerAdvisor.RETRIEVED_DOCUMENTS);

record SourceKey(String fileName, Integer pageNumber) {}

Map<SourceKey, List<Document>> groupedDocuments =
    documents == null
        ? Map.of()
        : documents.stream()
            .collect(Collectors.groupingBy(
                document -> {
                    var metadata = document.getMetadata();

                    String fileName = Objects.toString(
                        metadata.get("source"), "Unknown"
                    );

                    Integer pageNumber = toInteger(
                        metadata.get("page_number")
                    );

                    return new SourceKey(fileName, pageNumber);
                },
                LinkedHashMap::new,
                Collectors.toList()
            ));

List<KycSourceReference> sources = groupedDocuments.entrySet()
    .stream()
    .map(entry -> {
        String content = entry.getValue().stream()
            .map(Document::getText)
            .filter(Objects::nonNull)
            .map(String::trim)
            .filter(text -> !text.isBlank())
            .distinct()
            .collect(Collectors.joining("\n\n"));

        return new KycSourceReference(
            entry.getKey().fileName(),
            entry.getKey().pageNumber(),
            content
        );
    })
    .toList();


		return new KycRagResponse(question, answer, sources);
	}

	private Integer toInteger(Object value) {
		if (value instanceof Number number) {
			return number.intValue();
		}

		if (value != null) {
			return Integer.valueOf(value.toString());
		}

		return null;
	}

	public List<String> findSources(String question) {

		List<Document> documents = vectorStore
				.similaritySearch(SearchRequest.builder().query(question).topK(3).similarityThreshold(0.2).build());

		return documents.stream().map(document -> document.getMetadata().get("source")).filter(Objects::nonNull)
				.map(Object::toString).distinct().toList();
	}
}
