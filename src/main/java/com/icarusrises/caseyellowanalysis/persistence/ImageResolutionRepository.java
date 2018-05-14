package com.icarusrises.caseyellowanalysis.persistence;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface ImageResolutionRepository extends CrudRepository<ImageResolutionInfo, String> {

    ImageResolutionInfo findByUser(String user);
}
