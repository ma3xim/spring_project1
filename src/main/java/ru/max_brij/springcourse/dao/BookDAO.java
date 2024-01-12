package ru.max_brij.springcourse.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.max_brij.springcourse.models.Book;
import ru.max_brij.springcourse.models.Person;

import java.util.List;
import java.util.Optional;

@Component
public class BookDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> index(){
        return jdbcTemplate.query("select id, name, author, year from book", new BeanPropertyRowMapper<>(Book.class));
    }

    public Book show(int id){
        return jdbcTemplate.query("select * from book where id=?", new BeanPropertyRowMapper<>(Book.class), id)
                .stream().findAny().orElse(null);
    }


    public void save(Book book){
        jdbcTemplate.update("insert into book(name, author, year) values(?,?,?)", book.getName(), book.getAuthor(), book.getYear());
    }

    public void update(int id, Book updatedBook) {
        jdbcTemplate.update("update Book set name=?, author=?, year=? where id=?", updatedBook.getName(), updatedBook.getAuthor(), updatedBook.getYear(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("delete from book where id=?", id);
    }

    public Optional<Person> getBookOwner(int id){
        return jdbcTemplate.query("select person.* from book join person on book.keeper_id=Person.id where book.id=?",
                new BeanPropertyRowMapper<>(Person.class), id).stream().findAny();
    }

    public void release(int id){
        jdbcTemplate.update("update book set keeper_id=null where id=?", id);
    }

    public void assign(int id, Person selectedPerson){
        jdbcTemplate.update("update book set keeper_id=? where id=?", selectedPerson.getId(), id);
    }
}

