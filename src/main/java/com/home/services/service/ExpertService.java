package com.home.services.service;

import com.home.services.data.entity.Expert;
import com.home.services.data.enums.UserStatus;
import com.home.services.data.repository.CreateQuerySearchUser;
import com.home.services.data.repository.ExpertRepository;
import com.home.services.dto.DTOExpertRegister;
import com.home.services.dto.DTOSearchExpert;
import com.home.services.dto.mapper.AddressMapper;
import com.home.services.exception.FoundEmailException;
import com.home.services.exception.ImageSizeException;
import com.home.services.exception.InvalidPasswordException;
import com.home.services.exception.InvalidUserStatusException;
import com.home.services.other.Str;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Service
public final class ExpertService
{
    public final ExpertRepository expertRepository;
    private final AddressMapper addressMapper;
    private final CheckEmptyUserInfo checkEmptyUserInfo;
    private final CreateQuerySearchUser createQuerySearchUser;
    private static final int MAX_LEN_IMAGE = 300000;

    @Autowired
    public ExpertService(final ExpertRepository expertRepository , final AddressMapper addressMapper , final CheckEmptyUserInfo checkEmptyUserInfo ,
                         final CreateQuerySearchUser createQuerySearchUser)
    {
        this.expertRepository = expertRepository;
        this.addressMapper = addressMapper;
        this.checkEmptyUserInfo = checkEmptyUserInfo;
        this.createQuerySearchUser = createQuerySearchUser;
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

    public List<Expert> searchExperts(final DTOSearchExpert dtoSearchExpert) throws InvalidUserStatusException
    {
        final List<?> experts = createQuerySearchUser.createQuery("Expert" , dtoSearchExpert);

        if (experts != null && experts.size() > 0) return (List<Expert>) experts;
        else throw new NullPointerException("Not found expert");
    }
}
