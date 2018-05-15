package com.icarusrises.caseyellowanalysis.boot;

import com.icarusrises.caseyellowanalysis.persistence.ImageResolutionInfo;
import com.icarusrises.caseyellowanalysis.persistence.ImageResolutionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
//@Component
public class Boot {

    private ImageResolutionRepository imageResolutionRepository;

    @Autowired
    public Boot(ImageResolutionRepository imageResolutionRepository) {
        this.imageResolutionRepository = imageResolutionRepository;
    }

    @PostConstruct
    private void init() {
        log.info("Dango try to save new ImageResolutionInfo");
        imageResolutionRepository.save(new ImageResolutionInfo("s1", "bezeq", 10));
        log.info("Dango successfully save new ImageResolutionInfo");
        ImageResolutionInfo imageResolutionInfo = imageResolutionRepository.findByUser("dan");
        System.out.println("Dango");
    }
}
