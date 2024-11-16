package com.hackerrank.api.controller;

import com.hackerrank.api.model.Scan;
import com.hackerrank.api.service.ScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

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
        Scan s = scanService.getScanById(id);
        if(s.isDeleted()){
            throw new NoSuchElementException();
        }
        return s;
    }

    @DeleteMapping(path = "/{id}")
    public void deleteScan(@PathVariable("id") Long id) {
        findScan(id); // Check to see if this record has already been deleted
        scanService.deleteById(id);
    }

    @GetMapping(path = "/search/{domainName}")
    public List<Scan> findScans(@PathVariable("domainName") String domainName, @RequestParam(name = "orderBy") String orderBy) {
        return scanService.findByDomainName(domainName, orderBy);
    }

}
