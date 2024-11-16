package com.hackerrank.api.controller;

import com.hackerrank.api.model.Scan;
import com.hackerrank.api.service.ScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/scan")
public class ScanController {
  private final ScanService scanService;

  @Autowired
  public ScanController(ScanService scanService) {
    this.scanService = scanService;
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<Scan> getAllScan() {
    return scanService.getAllScan();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Scan createScan(@RequestBody Scan scan) {
    return scanService.createNewScan(scan);
  }

}
