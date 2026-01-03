package org.example.core_bancaire_soap.repository;

import org.example.core_bancaire_soap.entity.MobileRecharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RechargeRepo extends JpaRepository<MobileRecharge,Integer> {

}
