package com.homeservices.service;

import com.homeservices.config.SpringConfig;
import com.homeservices.data.entity.Address;
import com.homeservices.data.entity.Experts;
import com.homeservices.data.enums.UserStatus;
import com.homeservices.data.repository.ExpertRepository;
import com.homeservices.dto.DTOExpertRegister;
import com.homeservices.exception.ImageSizeException;
import org.springframework.stereotype.Service;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Service
public record ExpertService(ExpertRepository repository , AddressService addressService)
{
    private static final int MAX_LEN_IMAGE = 300000;


    public boolean register(DTOExpertRegister dtoExpertRegister) throws ImageSizeException
    {

        if (dtoExpertRegister.getImg().length > 0)
            if (dtoExpertRegister.getImg().length > MAX_LEN_IMAGE) throw new ImageSizeException();

        Address address = addressService.addAddress(dtoExpertRegister.getAddress());

        Experts user = new Experts();
        user.setName(dtoExpertRegister.getName());
        user.setFamily(dtoExpertRegister.getFamily());
        user.setEmail(dtoExpertRegister.getEmail());
        user.setPassword(dtoExpertRegister.getPassword());
        user.setUserStatus(UserStatus.valueOf(dtoExpertRegister.getUserStatus()));
        user.setAccountCredit(dtoExpertRegister.getAccountCredit());
        user.setAddress(address);

        user.setImg(dtoExpertRegister.getImg());
        user.setAreaOfExpertise(dtoExpertRegister.getAreaOfExpertise());

        user = repository.save(user);

        return user.getId() > 0;
    }

    public List<Experts> getExpertByAreaOfExpertise(final String areaOfExpertise)
    {
        CriteriaBuilder criteriaBuilder = SpringConfig.getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Experts> query = criteriaBuilder.createQuery(Experts.class);

        Root<Experts> from = query.from(Experts.class);

        CriteriaQuery<Experts> selected = query.select(from).where(criteriaBuilder.equal(from.get("areaOfExpertise") , areaOfExpertise));

        TypedQuery<Experts> result = SpringConfig.getEntityManager().createQuery(selected);

        return result.getResultList();
    }
}
