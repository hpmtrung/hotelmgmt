package vn.lotusviet.hotelmgmt.repository.mongo;

import com.mongodb.client.MongoClients;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.ImmutableMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

@SpringBootTest
@TestPropertySource(
    properties = {"logging.level.org.springframework.data.mongodb.core.MongoTemplate=INFO"},
    value = "/embed.properties")
class MongoLoggingTest {

  private static final String CONNECTION_STRING = "mongodb://%s:%d";

  private MongodExecutable mongodExecutable;
  private MongoTemplate mongoTemplate;

  @AfterEach
  void clean() {
    mongodExecutable.stop();
  }

  @BeforeEach
  void setUp() throws Exception {
    String IP = "localhost";
    int port = Network.freeServerPort(Network.getLocalHost());

    ImmutableMongodConfig config =
        MongodConfig.builder()
            .version(Version.Main.PRODUCTION)
            .net(new Net(IP, port, Network.localhostIsIPv6()))
            .build();

    MongodStarter starter = MongodStarter.getDefaultInstance();
    mongodExecutable = starter.prepare(config);
    mongodExecutable.start();
    mongoTemplate =
        new MongoTemplate(MongoClients.create(String.format(CONNECTION_STRING, IP, port)), "test");
  }

  private Book createAnyBook() {
    return new Book().setBookName("Mongo").setAuthorName("Bob");
  }

  private List<Book> createAnyBooks(int size) {
    List<Book> result = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      result.add(new Book().setBookName("book" + i).setAuthorName("author" + i));
    }
    return result;
  }

  @Test
  void whenInsertDocument_thenFindByIdIsOK() {
    Book book = createAnyBook();
    mongoTemplate.insert(book);

    assertThat(mongoTemplate.findById(book.getId(), Book.class)).isEqualTo(book);
  }

  @Test
  void whenUpdateDocument_thenGetFieldIsUpdated() {
    // Given
    Book book = createAnyBook();
    mongoTemplate.insert(book);

    // When
    String updatedAuthorName = "updatedAuthorName";
    book.setAuthorName(updatedAuthorName);
    mongoTemplate.updateFirst(
        Query.query(Criteria.where("bookName").is(book.getBookName())),
        Update.update("authorName", book.getAuthorName()),
        Book.class);

    // Then
    assertThat(mongoTemplate.findById(book.getId(), Book.class))
        .extracting(Book::getAuthorName)
        .isEqualTo(book.getAuthorName());
  }

  @Test
  void whenInsertMultipleDocuments_thenFindAllIsOK() {
    List<Book> books = createAnyBooks(2);

    mongoTemplate.insert(books, Book.class);

    assertThat(mongoTemplate.findAll(Book.class)).hasSize(books.size());
  }

  @Test
  void givenExistDocument_whenDelete_thenDocumentIsDeleted() {
    // Given
    Book book = createAnyBook();
    mongoTemplate.insert(book);

    // When
    mongoTemplate.remove(book);

    // Then
    assertThat(mongoTemplate.findAll(Book.class)).isEmpty();
  }

  @Test
  void whenAggregateByField_thenGroupByCountIsOk() {
    // Given
    final String authorName = "Author";
    final int numBook = 3;
    List<Book> books = new ArrayList<>();
    for (int i = 0; i < numBook; i++) {
      books.add(new Book().setBookName("book" + i).setAuthorName(authorName));
    }
    books.add(new Book().setBookName("anotherBook").setAuthorName("AnotherAuthor"));

    mongoTemplate.insert(books, Book.class);

    // when
    GroupOperation groupByAuthorName = group("authorName").count().as("authorCount");
    Aggregation aggregation = newAggregation(groupByAuthorName);

    AggregationResults<GroupBookByAuthor> aggregationResult =
        mongoTemplate.aggregate(aggregation, "book", GroupBookByAuthor.class);

    // Then
    List<GroupBookByAuthor> groupBookByAuthors =
        StreamSupport.stream(aggregationResult.spliterator(), false).collect(Collectors.toList());
    assertThat(
            groupBookByAuthors.stream()
                .filter(r -> r.authorName.equals(authorName))
                .findAny()
                .orElse(null))
        .extracting(GroupBookByAuthor::getAuthorCount)
        .isEqualTo(numBook);
  }

  private static class GroupBookByAuthor {
    @Id private String authorName;
    private int authorCount;

    public String getAuthorName() {
      return authorName;
    }

    public GroupBookByAuthor setAuthorName(String authorName) {
      this.authorName = authorName;
      return this;
    }

    public int getAuthorCount() {
      return authorCount;
    }

    public GroupBookByAuthor setAuthorCount(int authorCount) {
      this.authorCount = authorCount;
      return this;
    }
  }

  @Document(collection = "book")
  private static class Book {

    @MongoId private ObjectId id;

    private String bookName;

    private String authorName;

    public ObjectId getId() {
      return id;
    }

    public Book setId(ObjectId id) {
      this.id = id;
      return this;
    }

    public String getBookName() {
      return bookName;
    }

    public Book setBookName(String bookName) {
      this.bookName = bookName;
      return this;
    }

    public String getAuthorName() {
      return authorName;
    }

    public Book setAuthorName(String authorName) {
      this.authorName = authorName;
      return this;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Book)) return false;
      Book book = (Book) o;
      return Objects.equals(getId(), book.getId());
    }

    @Override
    public int hashCode() {
      return Objects.hash(getId());
    }
  }
}