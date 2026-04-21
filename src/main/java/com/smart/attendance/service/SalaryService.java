package com.smart.attendance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smart.attendance.entity.Salary;
import com.smart.attendance.dto.SalaryResponse;
import java.util.List;
import com.smart.attendance.repository.SalaryRepository;


@Service
public class SalaryService {

    @Autowired
    private SalaryRepository repo;

    public List<SalaryResponse> getSalaryHistory(Long userId) {

        List<Salary> list = repo.findByUserIdOrderByCreatedAtDesc(userId);

        return list.stream().map(s -> {
            SalaryResponse res = new SalaryResponse();

            res.setBasic(s.getBasic());
            res.setHra(s.getHra());
            res.setTransport(s.getTransport());
            res.setSpecial(s.getSpecial());

            res.setTds(s.getTds());
            res.setPf(s.getPf());
            res.setTax(s.getTax());
            res.setOthers(s.getOthers());

            double earnings = res.getBasic() + res.getHra() + res.getTransport() + res.getSpecial();
            double deductions = res.getTds() + res.getPf() + res.getTax() + res.getOthers();

            res.setTotalEarnings(earnings);
            res.setTotalDeductions(deductions);
            res.setNetSalary(earnings - deductions);

            return res;
        }).toList();
    }

    public SalaryResponse getSalary(Long userId) {

        Salary s = repo.findTopByUserIdOrderByCreatedAtDesc(userId)
                .orElseThrow(() -> new RuntimeException("Salary not found"));

        SalaryResponse res = new SalaryResponse();

        res.setBasic(s.getBasic());
        res.setHra(s.getHra());
        res.setTransport(s.getTransport());
        res.setSpecial(s.getSpecial());

        res.setTds(s.getTds());
        res.setPf(s.getPf());
        res.setTax(s.getTax());
        res.setOthers(s.getOthers());

        double earnings = res.getBasic() + res.getHra() + res.getTransport() + res.getSpecial();
        double deductions = res.getTds() + res.getPf() + res.getTax() + res.getOthers();

        res.setTotalEarnings(earnings);
        res.setTotalDeductions(deductions);
        res.setAllowances(res.getHra() + res.getTransport() + res.getSpecial());
        res.setDeductions(deductions);
        res.setNetSalary(earnings - deductions);

        return res;
    }
}