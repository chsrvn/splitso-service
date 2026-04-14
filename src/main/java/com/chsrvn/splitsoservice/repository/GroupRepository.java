package com.chsrvn.splitsoservice.repository;

import com.chsrvn.splitsoservice.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface GroupRepository extends JpaRepository<Group, UUID> {

    // Find all groups created by a specific user
    List<Group> findByCreatedBy(UUID userId);

    // Search for groups by name (case-insensitive)
    List<Group> findByNameContainingIgnoreCase(String name);
}
