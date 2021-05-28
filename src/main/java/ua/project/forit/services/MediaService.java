package ua.project.forit.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.project.forit.data.entities.models.User;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
public class MediaService
{
    protected final Cloudinary cloudinary;
    protected final Uploader uploader;

    public MediaService()
    {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "for-it-in-net",
                "api_key", "959844365938111",
                "api_secret", "L8mjsKp8nAIQ8F-OElEvg1rQ10w"));

        uploader = cloudinary.uploader();
    }

    public String setUserImg(MultipartFile multipartFile, final User user) throws IOException
    {
        if(user.getUserImg() != null)
            uploader.destroy(user.getUserImg(), ObjectUtils.asMap());

        user.setUserImg((String) uploadFile(multipartFile).get("public_id"));

        return user.getUserImg();
    }

    protected Map uploadFile(MultipartFile file) throws IOException
    {
        return uploader.upload(multipartFileToFile(file), ObjectUtils.asMap());
    }

    protected File multipartFileToFile(MultipartFile file) throws IOException
    {
        File convFile = new File(System.getProperty("java.io.tmpdir")+ "/" + file.getName());
        file.transferTo(convFile);

        return convFile;
    }
}
