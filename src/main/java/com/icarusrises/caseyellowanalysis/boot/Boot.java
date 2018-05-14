package com.icarusrises.caseyellowanalysis.boot;

import com.icarusrises.caseyellowanalysis.persistence.ImageResolutionInfo;
import com.icarusrises.caseyellowanalysis.persistence.ImageResolutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

//@Component
public class Boot {

    private ImageResolutionRepository imageResolutionRepository;

    @Autowired
    public Boot(ImageResolutionRepository imageResolutionRepository) {
        this.imageResolutionRepository = imageResolutionRepository;
    }

    @PostConstruct
    private void init() {
        imageResolutionRepository.save(new ImageResolutionInfo("dan", "hot"));
        System.out.println("Dango");
    }
}
