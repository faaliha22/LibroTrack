package org.example.controller;

import org.example.entity.IssueRecord;
import org.example.service.IssueRecordService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/issues")
public class IssueRecordController {

    private final IssueRecordService issueRecordService;

    public IssueRecordController(IssueRecordService issueRecordService) {
        this.issueRecordService = issueRecordService;
    }

    @GetMapping
    public List<IssueRecord> getAllIssueRecords() {
        return issueRecordService.getAllIssueRecords();
    }

    @PostMapping
    public IssueRecord issueBook(@RequestBody IssueRecord issueRecord) {
        return issueRecordService.issueBook(issueRecord);
    }
    @PutMapping("/{id}/return")
    public IssueRecord returnBook(@PathVariable Long id) {
        return issueRecordService.returnBook(id);
    }
    @DeleteMapping("/{id}")
    public void deleteIssueRecord(@PathVariable Long id) {
        issueRecordService.deleteIssueRecord(id);
    }
}