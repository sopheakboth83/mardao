package net.sf.mardao.test.aed.dao;

import java.util.List;

import net.sf.mardao.test.aed.domain.Appendix;
import net.sf.mardao.test.aed.domain.Book;
import net.sf.mardao.test.aed.domain.Chapter;
import net.sf.mardao.test.aed.domain.Footnote;
import net.sf.mardao.test.aed.domain.Page;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.datastore.Key;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.transform.TransformerConfigurationException;
import net.sf.mardao.api.dao.AEDDaoImpl;
import net.sf.mardao.test.aed.domain.DCategory;
import org.xml.sax.SAXException;

public class UberDaoBean {
    static final Logger LOG  = LoggerFactory.getLogger(UberDaoBean.class);

    static final String ISBN = "ISBN-123-4569677-01";

    private BookDao     bookDao;
    private ChapterDao  chapterDao;
    private PageDao     pageDao;
    private FootnoteDao footnoteDao;
    private AppendixDao appendixDao;
    private DCategoryDao categoryDao;

    public void setup() throws SAXException, FileNotFoundException, IOException, TransformerConfigurationException {
        Book book = new Book();
        book.setISBN(ISBN);
        book.setTitle("Good morning midnight");
        bookDao.persist(book);

        Book book2 = new Book();
        book2.setISBN("73-9482-49");
        book2.setTitle("Animal Farm");
        bookDao.persist(book2);

        Book book3 = bookDao.persist("25/34 & = \"}", "25/34 & =");

        Appendix appendixA = appendixDao.persist(book3.getPrimaryKey(), "Appendix 3/4 & \"B\"");

        Chapter prologue = new Chapter();
        prologue.setId(42L);
        prologue.setBook(book.getPrimaryKey());
        prologue.setName("Prologue");
        chapterDao.persist(prologue);

        Chapter intermezzo = new Chapter();
        intermezzo.setId(73L);
        intermezzo.setBook(book.getPrimaryKey());
        intermezzo.setName("Intermezzo");
        chapterDao.persist(intermezzo);

        Chapter generated = chapterDao.persist(book.getPrimaryKey(), null, "GeneratedChapter");
        LOG.info("Persisted generated chapter {}", generated);
        Chapter actual = chapterDao.findByPrimaryKey(book.getPrimaryKey(), generated.getId());
        LOG.info("Queried generated chapter {}", actual);

        Page page1 = new Page();
        page1.setBody("Lorem ipsum dolor ...");
        page1.setBook(book.getISBN());
        page1.setChapter(prologue.getPrimaryKey());
        pageDao.persist(page1);
        LOG.info("persisted {}", page1);

        Footnote footnote = new Footnote();
        footnote.setName("Be aware that...");
        footnote.setPage((Key) page1.getPrimaryKey());
        footnoteDao.persist(footnote);
        
        for (int i = 0; i < 10; i++) {
            DCategory cat = new DCategory();
            cat.setTitle("Category number " + i);
            categoryDao.persist(cat);
        }

        test();
        
        AEDDaoImpl.xmlWriteToBlobs(bookDao, chapterDao, pageDao, footnoteDao);
    }

    public void test() {
        List<Book> books = bookDao.findAll();
        chapterDao.findAll();
        pageDao.findAll();
        footnoteDao.findAll();

        Book book = bookDao.findByPrimaryKey(ISBN);
        if (false == book.getUpdatedDate().equals(book.getCreatedDate())) {
            LOG.error("Expected updatedDate {} to be equal to createdDate {}", book.getUpdatedDate(), book.getCreatedDate());
        }

        List<Chapter> chapters = chapterDao.findByBook((Key) book.getPrimaryKey());
        if (chapters.isEmpty()) {
            LOG.error("Expected chapters in book {}", ISBN);
        }

        book.setTitle("Updated book title");
        bookDao.update(book);
        if (book.getUpdatedDate().compareTo(book.getCreatedDate()) <= 0) {
            LOG.error("Expected updatedDate {} to be after createdDate {}", book.getUpdatedDate(), book.getCreatedDate());
        }

        book = bookDao.findByPrimaryKey("25/34 & = \"}");
        Appendix appendix = appendixDao.findByPrimaryKey(book.getPrimaryKey(), "Appendix 3/4 & \"B\"");
        if (null == appendix) {
            LOG.error("Expected to find appendix for book {}", book);
        }
        
        // cache exercise
        List<DCategory> cats = null;
        for (int i = 0; i < 10; i++) {
            cats = categoryDao.findAll();
        }
        DCategory fifth = cats.get(4);
        List<Long> ids = new ArrayList<Long>();
        for (int i = 2; i < 7; i++) {
            ids.add(cats.get(i).getSimpleKey());
        }
        
        // update fifth element
        fifth.setTitle("Updated title for fifth cat");
        categoryDao.update(fifth);
        
        // re-retrieve
        for (int i = 0; i < 2; i++) {
            categoryDao.findByPrimaryKeys(ids);
        }
    }

    public void destroy() {
        bookDao.deleteAll();
    }

    public ChapterDao getChapterDao() {
        return chapterDao;
    }

    public void setChapterDao(ChapterDao chapterDao) {
        this.chapterDao = chapterDao;
    }

    public PageDao getPageDao() {
        return pageDao;
    }

    public void setPageDao(PageDao pageDao) {
        this.pageDao = pageDao;
    }

    public void setBookDao(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public BookDao getBookDao() {
        return bookDao;
    }

    public FootnoteDao getFootnoteDao() {
        return footnoteDao;
    }

    public void setFootnoteDao(FootnoteDao footnoteDao) {
        this.footnoteDao = footnoteDao;
    }

    public final void setAppendixDao(AppendixDao appendixDao) {
        this.appendixDao = appendixDao;
    }

    public void setCategoryDao(DCategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

}
