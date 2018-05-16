package com.icarusrises.caseyellowanalysis.boot;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.icarusrises.caseyellowanalysis.persistence.repositories.UserImageResolutionInfoRepository;
import com.icarusrises.caseyellowanalysis.persistence.model.UserImageResolutionInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@Slf4j
//@Component
public class Boot {

    private AmazonDynamoDB amazonDynamoDB;
    private UserImageResolutionInfoRepository imageResolutionRepository;

    @Autowired
    public Boot(UserImageResolutionInfoRepository imageResolutionRepository, AmazonDynamoDB amazonDynamoDB) {
        this.imageResolutionRepository = imageResolutionRepository;
        this.amazonDynamoDB = amazonDynamoDB;
    }

    @PostConstruct
    private void init() {

//        UserImageResolutionInfo userImageResolutionInfo = new

        log.info("Dango try to save new ImageResolutionInfo");

        log.info("Dango successfully save new ImageResolutionInfo");
        System.out.println("Dango");
    }



//    @PostConstruct
    public void setup() throws Exception {
        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);

            CreateTableRequest tableRequest = dynamoDBMapper.generateCreateTableRequest(UserImageResolutionInfo.class);
            tableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
            amazonDynamoDB.createTable(tableRequest);

        System.out.println("dango");
            //...

//            dynamoDBMapper.batchDelete((List<ProductInfo>)repository.findAll());

    }
}
