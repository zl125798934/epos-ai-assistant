package cn.zhang.eposaiassistant.config;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import jakarta.annotation.Resource;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RAG（检索增强生成）配置类。
 *
 * <p>负责加载文档、构建向量索引并提供内容检索器。</p>
 */
@Configuration
public class RagConfig {

  @Resource
  private EmbeddingModel qwenEmbeddingModel;

  @Resource
  private EmbeddingStore<TextSegment> embeddingStore;

  /**
   * 创建内容检索器 Bean。
   *
   * @return 内容检索器
   */
  @Bean
  public ContentRetriever contentRetriever() {
    // ------ RAG ------
    initRag();

    // 4. 自定义内容查询器
    ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
        .embeddingStore(embeddingStore)
        .embeddingModel(qwenEmbeddingModel)
        .maxResults(5) // 最多 5 个检索结果
        .minScore(0.75) // 过滤掉分数小于 0.75 的结果
        .build();
    return contentRetriever;
  }

  /**
   * 初始化 RAG。
   */
  private void initRag() {
    // 1. 加载文档
    List<Document> documents = FileSystemDocumentLoader.loadDocuments("src/main/resources/docs");
    // 2. 文档切割：将每个文档按每段进行分割，最大 500 字符，每次重叠最多 100 个字符
    DocumentByParagraphSplitter paragraphSplitter = new DocumentByParagraphSplitter(500, 100);
    // 3. 自定义文档加载器
    EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
        .documentSplitter(paragraphSplitter)
        // 为了提高搜索质量，为每个 TextSegment 添加文档名称
        .textSegmentTransformer(textSegment -> TextSegment.from(
            textSegment.metadata().getString("file_name") + "\n" + textSegment.text(),
            textSegment.metadata()
        ))
        // 使用指定的向量模型
        .embeddingModel(qwenEmbeddingModel)
        .embeddingStore(embeddingStore)
        .build();
    // 加载文档
    ingestor.ingest(documents);
  }
}
