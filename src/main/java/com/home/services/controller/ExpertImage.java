package com.home.services.controller;

import com.home.services.data.entity.User;
import com.home.services.service.ExpertService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping(value = "/expert-image", method = RequestMethod.GET)
public record ExpertImage(ExpertService expertService)
{


    @RequestMapping(value = {"/" , "/{USER_ID}"}, method = RequestMethod.GET, produces = "image/png")
    @RolesAllowed({"ADMIN" , "EXPERT"})
    public byte[] getExpertImage(@PathVariable(value = "USER_ID") final String strUserId , final HttpServletResponse response)
    {
        final long userId = checkStrId(strUserId);
        if (userId > 0)
        {
            final User expertFindById = expertService.expertRepository().findById(userId);
            if (expertFindById != null)
            {
                final byte[] img = expertFindById.getImg();
                if (img != null && img.length > 0)
                {
                    return img;
                }
            }
        }

        final File defaultProfileFile = new File(System.getProperty("user.dir") + File.separator + "data" + File.separator + "images" + File.separator + "default-profile.png");

        if (defaultProfileFile.exists())
        {
            try
            {
                return Files.readAllBytes(defaultProfileFile.toPath());
            }
            catch (IOException ignored)
            {
            }
        }


        return null;
    }

    private long checkStrId(final String strId)
    {
        try
        {
            return Long.parseLong(strId);
        }
        catch (Exception ignored)
        {
        }
        return 0;
    }
}
