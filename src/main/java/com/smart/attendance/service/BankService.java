package com.smart.attendance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smart.attendance.entity.BankDetails;
import com.smart.attendance.dto.BankRequest;
import com.smart.attendance.dto.BankResponse;
import com.smart.attendance.entity.bankOtp;
import com.smart.attendance.repository.BankRepository;
import com.smart.attendance.repository.bankOtpRepository;


@Service
public class BankService {

    @Autowired
    private BankRepository repo;

    @Autowired
    private bankOtpRepository otpRepo;

    public BankResponse getBankDetails(Long userId) {

        BankDetails b = repo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Bank details not found"));

        BankResponse res = new BankResponse();

        res.setBankName(b.getBankName());
        res.setAccountNumber(b.getAccountNumber());
        res.setAccountType(b.getAccountType());
        res.setIfsc(b.getIfsc());
        res.setHolderName(b.getHolderName());
        res.setBranch(b.getBranch());

        return res;
    }

    public BankDetails saveOrUpdate(Long userId, BankRequest req) {

        BankDetails bank = repo.findByUserId(userId)
                .orElse(new BankDetails());

        bank.setUserId(userId);
        bank.setBankName(req.bankName);
        bank.setAccountNumber(req.accountNumber);
        bank.setAccountType(req.accountType);
        bank.setIfsc(req.ifsc);
        bank.setHolderName(req.holderName);
        bank.setBranch(req.branch);

        if(req.accountNumber.length() < 8){
            throw new RuntimeException("Invalid account number");
        }

        return repo.save(bank);
    }

    public BankDetails saveOrUpdate(Long userId, String email, BankRequest req) {

        bankOtp otp = otpRepo.findTopByEmailOrderByIdDesc(email)
                .orElseThrow(() -> new RuntimeException("OTP required"));

        if (!otp.isVerified()) {
            throw new RuntimeException("OTP not verified");
        }

        BankDetails bank = repo.findByUserId(userId)
                .orElse(new BankDetails());

        bank.setUserId(userId);

        bank.setBankName(req.bankName);
        bank.setAccountNumber(req.accountNumber);
        bank.setAccountType(req.accountType);
        bank.setIfsc(req.ifsc);
        bank.setHolderName(req.holderName);
        bank.setBranch(req.branch);

        return repo.save(bank);
    }
}