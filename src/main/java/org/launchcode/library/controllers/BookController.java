package org.launchcode.library.controllers;

import jakarta.validation.Valid;
import org.launchcode.library.models.Book;
import org.launchcode.library.models.BookCheckout;
import org.launchcode.library.models.BooksInventory;
import org.launchcode.library.models.data.BookCheckoutRepository;
import org.launchcode.library.models.data.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookCheckoutRepository bookCheckoutRepository;

    @GetMapping("/") //http://localhost:8080/books/
    public String index(Model model) {
        model.addAttribute("title", "All Books");
        model.addAttribute("books", bookRepository.findAll());
        return "books/index";
    }

    @GetMapping("add") //http://localhost:8080/books/add
    public String displayAddBookForm(Model model) {

        model.addAttribute(new Book());
        model.addAttribute("title", "Add a book");

        return "books/add";
    }

    @PostMapping("add")  //http://localhost:8080/books/add
    public String processAddBookForm(@ModelAttribute @Valid Book newBook,
                                         Errors errors, Model model) {

        if (errors.hasErrors()) {
            return "books/add";
        }
        newBook.setAvailableCopiesToIssue(newBook.getCopies());

        bookRepository.save(newBook);


        return "redirect:";
    }
    @GetMapping("update") //http://localhost:8080/books/update?bookId=xx
    public String displayUpdateBookForm(@RequestParam Integer bookId, Model model){
        model.addAttribute("title", "Update book");

        Optional<Book> result = bookRepository.findById(bookId);
        Book book = result.get();
        model.addAttribute(book);
        model.addAttribute(new BooksInventory());

        return "books/update";
    }
    @PostMapping("update") //http://localhost:8080/books/update
    public String processUpdateBookForm(@ModelAttribute @Valid Book book,@ModelAttribute @Valid BooksInventory booksInventory,
                                    Errors errors,
                                    Model model) {


        if (!errors.hasErrors()) {

            if(booksInventory.getBooksToAdd()>0) {
                int copies=book.getCopies()+ booksInventory.getBooksToAdd();
                int availableCopies=book.getAvailableCopiesToIssue()+booksInventory.getBooksToAdd();

                book.setCopies(copies);
                book.setAvailableCopiesToIssue(availableCopies);
            }
            if(booksInventory.getBooksToRemove()>0){
                int copies= book.getCopies()- booksInventory.getBooksToRemove();

                int availableCopies= book.getAvailableCopiesToIssue()- booksInventory.getBooksToRemove();
                book.setCopies(copies);
                book.setAvailableCopiesToIssue(availableCopies);
            }
            bookRepository.save(book);

            return "redirect:detail?bookId=" + book.getId();
        }


        return "redirect:update";
    }
    @GetMapping("detail") //http://localhost:8080/books/detail?bookId=xx
    public String displayBookDetail(@RequestParam Integer bookId, Model model){
        Optional<Book> result = bookRepository.findById(bookId);
        Book book = result.get();
        model.addAttribute(book);

        return "books/detail";
    }

    @GetMapping("deletelist") //http://localhost:8080/books/deletelist
    public String displayDeleteBooksForm(Model model) {
        model.addAttribute("title", "Delete Books");
        model.addAttribute("books", bookRepository.findAll());
        return "books/delete";
    }



    @PostMapping("deletebooks") //http://localhost:8080/books/delete
    public String processDeleteBooks(@RequestParam(required = false) Integer[] bookIds) {

        if (bookIds != null) {
            for (int id : bookIds) {
                bookRepository.deleteById(id);
            }
        }

        return "redirect:";
    }
    @GetMapping("delete") //http://localhost:8080/books/delete?bookId=xx
    public String displayBookDelete(@RequestParam Integer bookId, Model model){
        Optional<Book> result = bookRepository.findById(bookId);
        Book book = result.get();
        model.addAttribute(book);
        model.addAttribute("title", "Delete book");

        return "books/delete";
    }

    @PostMapping("delete") //http://localhost:8080/books/delete?bookId=xxxx
    public String processDeleteBook(@RequestParam(required = true) Integer bookId) {

        if (bookId != null) {

            bookRepository.deleteById(bookId);
        }


        return "redirect:";
    }
    @GetMapping("checkout") //http://localhost:8080/books/checkout
    public String displayCheckout(Model model, int bookId){
        model.addAttribute(new BookCheckout());
        model.addAttribute("bookId",bookId);
        model.addAttribute("title", "Checkout book");

        return "books/checkout";
    }

    @PostMapping("checkout") //http://localhost:8080/books/checkout?bookId=xxxx
    public String processCheckout(@ModelAttribute @Valid BookCheckout newBookCheckout,int bookId, int studentId,
                                  Errors errors, Model model) {
        if (errors.hasErrors()) {
            return "books/checkout";
        }
        newBookCheckout.setCheckout(true);
        Optional<Book> result = bookRepository.findById(bookId);
        Book book = result.get();
        newBookCheckout.setBook(book);
        //student code later
        newBookCheckout.setStudentId(studentId);
        newBookCheckout.setIssueDate(new Date());
        int availableCopies = book.getAvailableCopiesToIssue();
        availableCopies--;
        bookCheckoutRepository.save(newBookCheckout);
        book.setAvailableCopiesToIssue(availableCopies);
        bookRepository.save(book);
        return "redirect:";
    }

    @GetMapping("checkin") //http://localhost:8080/books/checkin
    public String displayCheckin(Model model, int bookId){
        model.addAttribute("bookId",bookId);
        model.addAttribute("title", "Checkin book");

        return "books/checkin";
    }

    @PostMapping("checkin") //http://localhost:8080/books/checkout?bookId=xxxx
    public String processCheckin(@RequestParam(required = true) Integer bookId, @RequestParam(required = true) Integer studentId) {
        Optional<Book> result = bookRepository.findById(bookId);
        Book book = result.get();
        BookCheckout bookCheckout=bookCheckoutRepository.findByBookIdAndStudentIdAndIsCheckout(bookId,studentId,true);
        bookCheckout.setCheckout(false);
        bookCheckout.setActualReturnDate(new Date());
        int availableCopies = book.getAvailableCopiesToIssue();
        availableCopies++;
        bookCheckoutRepository.save(bookCheckout);
        book.setAvailableCopiesToIssue(availableCopies);
        bookRepository.save(book);
        return "redirect:";
    }


}