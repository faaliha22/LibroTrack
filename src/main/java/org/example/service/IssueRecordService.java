package org.example.service;

import org.example.entity.Book;
import org.example.entity.IssueRecord;
import org.example.entity.Member;
import org.example.repository.BookRepository;
import org.example.repository.IssueRecordRepository;
import org.example.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class IssueRecordService {

    private final IssueRecordRepository issueRecordRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    public IssueRecordService(
            IssueRecordRepository issueRecordRepository,
            BookRepository bookRepository,
            MemberRepository memberRepository) {

        this.issueRecordRepository = issueRecordRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
    }

    public List<IssueRecord> getAllIssueRecords() {
        return issueRecordRepository.findAll();
    }

    public IssueRecord issueBook(IssueRecord issueRecord) {

        if (issueRecord.getBook() == null ||
                issueRecord.getBook().getId() == null) {

            throw new IllegalArgumentException(
                    "Please select a book."
            );
        }

        if (issueRecord.getMember() == null ||
                issueRecord.getMember().getId() == null) {

            throw new IllegalArgumentException(
                    "Please select a member."
            );
        }

        if (issueRecord.getIssueDate() == null) {

            throw new IllegalArgumentException(
                    "Issue date is required."
            );
        }

        if (issueRecord.getIssueDate().isAfter(LocalDate.now())) {

            throw new IllegalArgumentException(
                    "Issue date cannot be in the future."
            );
        }

        Long bookId = issueRecord.getBook().getId();
        Long memberId = issueRecord.getMember().getId();

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Selected book was not found."
                        )
                );

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Selected member was not found."
                        )
                );

        // Check whether at least one copy is available
        if (book.getAvailableCopies() <= 0) {

            throw new IllegalArgumentException(
                    "No copies of this book are currently available."
            );
        }

        issueRecord.setBook(book);
        issueRecord.setMember(member);

        // One copy is issued
        book.setAvailableCopies(
                book.getAvailableCopies() - 1
        );

        bookRepository.save(book);

        return issueRecordRepository.save(issueRecord);
    }

    public IssueRecord returnBook(Long id) {

        IssueRecord issueRecord =
                issueRecordRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Issue record was not found."
                                )
                        );

        if (issueRecord.getReturnDate() != null) {

            throw new IllegalArgumentException(
                    "This book has already been returned."
            );
        }

        issueRecord.setReturnDate(LocalDate.now());

        Book book = issueRecord.getBook();

        // One copy is returned
        if (book.getAvailableCopies() < book.getTotalCopies()) {

            book.setAvailableCopies(
                    book.getAvailableCopies() + 1
            );
        }

        bookRepository.save(book);

        return issueRecordRepository.save(issueRecord);
    }

    public void deleteIssueRecord(Long id) {
        issueRecordRepository.deleteById(id);
    }
}