package ir.maktab.finalprojectphase3.data.dao;

import ir.maktab.finalprojectphase3.data.dto.CriteriaSearchDTO;
import ir.maktab.finalprojectphase3.data.enums.ExpertRegistrationStatus;
import ir.maktab.finalprojectphase3.data.model.Expert;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ExpertDao extends JpaRepository<Expert, Long> {

    Optional<Expert> findByUsernameAndDisable(String username, boolean disable);

    boolean existsByUsername(String username);

    List<Expert> findAllByExpertRegistrationStatus(ExpertRegistrationStatus registrationStatus);

    static Specification<Expert> selectExpertsByCriteria(CriteriaSearchDTO searchDTO) {
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
            if (searchDTO.getSpeciality() != null) {
                String speciality = "%" + searchDTO.getSpeciality() + "%";
                predicates.add(cb.like(root.join("subServiceSet").get("name"), speciality));
            }
            if (searchDTO.getMinScore() != null || searchDTO.getMaxScore() != null) {
                predicates.add(cb.between(root.get("totalScore"), searchDTO.getMinScore(), searchDTO.getMaxScore()));
            }

            cq.where(cb.or(predicates.toArray(new Predicate[0])));

            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }

    Page<Expert> findAll(Specification<Expert> specification, Pageable pageable);
}
