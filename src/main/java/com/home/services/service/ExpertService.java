package com.home.services.service;

import com.home.services.data.entity.Expert;
import com.home.services.data.enums.UserStatus;
import com.home.services.data.repository.ExpertRepository;
import com.home.services.dto.DTOExpertRegister;
import com.home.services.dto.mapper.AddressMapper;
import com.home.services.exception.FoundEmailException;
import com.home.services.exception.ImageSizeException;
import com.home.services.exception.InvalidPasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class ExpertService
{
    public final ExpertRepository expertRepository;
    private final AddressMapper addressMapper;
    private final CheckEmptyUserInfo checkEmptyUserInfo;
    private static final int MAX_LEN_IMAGE = 300000;

    @Autowired
    public ExpertService(final ExpertRepository expertRepository , final AddressMapper addressMapper , final CheckEmptyUserInfo checkEmptyUserInfo)
    {
        this.expertRepository = expertRepository;
        this.addressMapper = addressMapper;
        this.checkEmptyUserInfo = checkEmptyUserInfo;
    }

    public boolean register(final DTOExpertRegister dtoExpertRegister) throws ImageSizeException, FoundEmailException, NullPointerException, InvalidPasswordException
    {
        if (dtoExpertRegister.getImg().length > 0)
            if (dtoExpertRegister.getImg().length > MAX_LEN_IMAGE) throw new ImageSizeException();

        if (checkEmptyUserInfo.check(dtoExpertRegister))
        {
            if (hasEmail(dtoExpertRegister.getEmail()))
            {
                Expert expert = new Expert();
                expert.setName(dtoExpertRegister.getName());
                expert.setFamily(dtoExpertRegister.getFamily());
                expert.setEmail(dtoExpertRegister.getEmail());
                expert.setPassword(dtoExpertRegister.getPassword());
                expert.setUserStatus(UserStatus.WAITING_ACCEPT);
                expert.setAddress(addressMapper.toAddress(dtoExpertRegister.getAddress()));

                expert.setImg(dtoExpertRegister.getImg());

                expert = expertRepository.save(expert);

                return expert.getId() > 0;
            }
            else throw new FoundEmailException(dtoExpertRegister.getEmail());
        }
        return false;
    }

    public boolean hasEmail(final String email)
    {
        return (expertRepository.findByEmail(email) != null);
    }

}
