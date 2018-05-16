package com.icarusrises.caseyellowanalysis.persistence.repositories;

import com.icarusrises.caseyellowanalysis.persistence.model.UserImageResolutionInfo;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface UserImageResolutionInfoRepository extends CrudRepository<UserImageResolutionInfo, String> {

    UserImageResolutionInfo findByUser(String user);
}
