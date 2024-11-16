package com.hackerrank.api.service.impl;

import com.hackerrank.api.exception.BadRequestException;
import com.hackerrank.api.model.Scan;
import com.hackerrank.api.repository.ScanRepository;
import com.hackerrank.api.service.ScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultScanService implements ScanService {
  private final ScanRepository scanRepository;

  @Autowired
  DefaultScanService(ScanRepository scanRepository) {
    this.scanRepository = scanRepository;
  }

  @Override
  public List<Scan> getAllScan() {
    return scanRepository.findAll().stream()
            .filter(scan -> !scan.isDeleted())
            .collect(Collectors.toList());
  }


  @Override
  public Scan createNewScan(Scan scan) {
    if (scan.getId() != null) {
      throw new BadRequestException("The ID must not be provided when creating a new Scan");
    }

    return scanRepository.save(scan);
  }

  @Override
  public Scan getScanById(Long id) {
    return scanRepository.findById(id).get();
  }

  @Override
  public void deleteById(Long id) {
    Scan s = scanRepository.findById(id).get();
    s.setDeleted(true);
    // saveAndFlush might be overkill here, depending on whether we care more about consistency or efficiency
    scanRepository.saveAndFlush(s);
  }

    @Override
    public List<Scan> findByDomainName(String domainName, String orderBy) {

    // This is extremely ugly.  The better solution is something like...
    //
    //   scanRepository.findByDomainName(domainName, Sort.by(orderBy));
    //
    // For whatever reason, the line above doesn't work right out of the box.
    // I've run out of time to properly debug it, so instead
    // here is a working solution that is hardcoded to the specified data model.

      System.out.println("searching for domainName " + domainName + " order by " + orderBy);
      if ("domain_name".equals(orderBy)) {
        return scanRepository.findByDomainNameOrderByDomainNameAsc(domainName);
      } else if ("num_pages".equals(orderBy)) {
        return scanRepository.findByDomainNameOrderByNumPagesAsc(domainName);
      } else if ("num_broken_links".equals(orderBy)) {
        return scanRepository.findByDomainNameOrderByNumBrokenLinksAsc(domainName);
      } else if ("num_missing_images".equals(orderBy)) {
        return scanRepository.findByDomainNameOrderByNumMissingImagesAsc(domainName);
      } else {
        throw new BadRequestException(String.format("Unknown order parameter: %s", orderBy));
      }
    }
}
