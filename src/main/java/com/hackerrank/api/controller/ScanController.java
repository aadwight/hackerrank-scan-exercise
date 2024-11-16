package com.hackerrank.api.controller;

import com.hackerrank.api.model.Scan;
import com.hackerrank.api.service.ScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(path = "/{id}")
    public Scan findScan(@PathVariable("id") Long id) {
        return scanService.getScanById(id);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteScan(@PathVariable("id") Long id) {
        scanService.deleteById(id);
    }

    @GetMapping(path = "/search/{domainName}")
    public List<Scan> findScans(@PathVariable("domainName") String domainName, @RequestParam(name = "orderBy") Long numPages) {
        return scanService.findByDomainName(domainName, numPages);
    }

}
