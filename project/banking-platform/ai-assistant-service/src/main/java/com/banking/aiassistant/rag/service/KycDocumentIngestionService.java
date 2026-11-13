
package com.banking.aiassistant.rag.service;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

@Service
public class KycDocumentIngestionService {

	private final VectorStore vectorStore;

	private final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

	public KycDocumentIngestionService(VectorStore vectorStore) {
		this.vectorStore = vectorStore;
	}

	public int ingestKycDocument() {

		try {
			Resource[] resources = resourceResolver.getResources("classpath*:knowledge/*");

			TokenTextSplitter splitter = TokenTextSplitter.builder().withChunkSize(500).build();

			int totalChunks = 0;

			for (Resource resource : resources) {

				String fileName = resource.getFilename();

				if (fileName == null) {
					continue;
				}

				String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase(Locale.ROOT);

				List<Document> documents;
				
				System.out.println("Processing file: " + fileName);
				System.out.println("Resource: " + resource.getDescription());

				// Select reader based on file extension
				switch (extension) {

				case "txt" -> documents = new TextReader(resource).get();

				case "pdf" -> documents = new PagePdfDocumentReader(resource).get();

				default -> {
					System.out.println("Skipping unsupported file: " + fileName);
					continue;
				}
				}

				// Add source metadata to every document
				documents.forEach(document -> document.getMetadata().put("source", fileName));

				// Split extracted content into smaller chunks
				List<Document> chunks = splitter.apply(documents);

				if (chunks.isEmpty()) {
					throw new IllegalStateException("No chunks generated for: " + fileName);
				}

				// Delete old chunks belonging to this source
				FilterExpressionBuilder filterBuilder = new FilterExpressionBuilder();

				vectorStore.delete(filterBuilder.eq("source", fileName).build());

				// Insert latest chunks
				vectorStore.write(chunks);

				totalChunks += chunks.size();

				System.out.println("Ingested: " + fileName + ", chunks: " + chunks.size());
			}

			if (totalChunks == 0) {
				throw new IllegalStateException("No supported TXT or PDF documents found");
			}

			return totalChunks;

		} catch (IOException exception) {

			throw new IllegalStateException("Document ingestion failed", exception);
		}
	}
}
