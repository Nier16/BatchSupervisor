package fr.ag2rlamondiale.espacetiers.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.ag2rlamondiale.espacetiers.entity.MyPlannification;

@Repository
public interface PlannificationRepository extends JpaRepository<MyPlannification, Long>{
	public List<MyPlannification> findByBashID(Long id);
}
