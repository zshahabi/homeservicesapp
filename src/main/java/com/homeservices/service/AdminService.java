package com.homeservices.service;

import com.homeservices.data.repository.AdminRepository;
import org.springframework.stereotype.Service;

@Service
public record AdminService(AdminRepository repository)
{
}
