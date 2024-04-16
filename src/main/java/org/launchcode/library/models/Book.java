package org.launchcode.library.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Book extends AbstractEntity{
    //moved name from AbstarctEntity showing error with student name
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;

    @Size(max = 500, message = "Description too long!")
    private String description;

    @NotBlank(message = "Author Name is required")
    private String authorName;

    private int publishingYear;

    private double price;

    @NotBlank(message = "Genre is required")
    private String genre;

    private int copies;

    private int availableCopiesToIssue;

    @OneToMany
    @JoinColumn(name="book_id")
    private List<BookCheckout> bookCheckouts = new ArrayList<>();

        public int getAvailableCopiesToIssue() {
        return availableCopiesToIssue;
    }

    public void setAvailableCopiesToIssue(int availableCopiesToIssue) {
        this.availableCopiesToIssue = availableCopiesToIssue;
    }


    public Book() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int getPublishingYear() {
        return publishingYear;
    }

    public void setPublishingYear(int publishingYear) {
        this.publishingYear = publishingYear;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public List<BookCheckout> getBookCheckouts() {
        return bookCheckouts;
    }

    public void setBookCheckouts(List<BookCheckout> bookCheckouts) {
        this.bookCheckouts = bookCheckouts;
    }
}
