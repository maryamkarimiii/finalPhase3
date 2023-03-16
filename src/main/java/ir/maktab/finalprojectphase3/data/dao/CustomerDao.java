package ir.maktab.finalprojectphase3.data.dao;

import ir.maktab.finalprojectphase3.data.dto.CriteriaSearchDTO;
import ir.maktab.finalprojectphase3.data.model.Customer;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface CustomerDao extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer>
        , PagingAndSortingRepository<Customer, Long> {
    Optional<Customer> findByUsername(String username);

    static Specification<Customer> selectCustomersByCriteria(CriteriaSearchDTO searchDTO) {
        return (root, cq, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (searchDTO.getFirstName() != null) {
                String firstName = "%" + searchDTO.getFirstName() + "%";
                predicates.add(cb.like(root.get("firstName"), firstName));
            }
            if (searchDTO.getLastName() != null) {
                String lastName = "%" + searchDTO.getLastName() + "%";
                predicates.add(cb.like(root.get("lastName"), lastName));
            }
            if (searchDTO.getEmail() != null) {
                String email = "%" + searchDTO.getEmail() + "%";
                predicates.add(cb.like(root.get("email"), email));
            }

            cq.where(cb.or(predicates.toArray(new Predicate[0])));

            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }

    Page<Customer> findAll(Specification<Customer> specification, Pageable pageable);
}
