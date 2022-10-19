package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.models.Bucket;
import web.models.User;
import web.repository.BucketRepository;

@Service
public class BucketService {

    @Autowired
    private BucketRepository bucketRepository;

    public Bucket createBucket(User user){
        Bucket bucket = new Bucket();
        bucket.setUser(user);
        bucketRepository.save(bucket);


        return bucket;
    }

}
