package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.models.Image;
import web.repository.ImageRepository;

import javax.transaction.Transactional;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Transactional
    public void deleteImg(Image img){
        imageRepository.deleteById(img.getId());
    }

    @Transactional
    public Image findImgById(Long id) throws Exception {
        var img =imageRepository.findById(id);
        if(img.isEmpty()){
            throw new Exception("Cant find image");
        }
        return img.get();
    }
}
