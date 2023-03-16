package ir.maktab.finalprojectphase3.data.dao;

import ir.maktab.finalprojectphase3.data.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletDao extends JpaRepository<Wallet,Long> {
}
