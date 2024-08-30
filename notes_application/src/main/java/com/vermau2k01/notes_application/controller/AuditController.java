package com.vermau2k01.notes_application.controller;

import com.vermau2k01.notes_application.entity.AuditLog;
import com.vermau2k01.notes_application.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("audit")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;

    public List<AuditLog> getAuditLog() {
      return auditService.getAllLogs();
    }

    @GetMapping("/note/{id}")
    public List<AuditLog> getNoteAuditLog(@PathVariable String id)
    {
       return auditService.getNoteAuditLog(id);
    }
}
