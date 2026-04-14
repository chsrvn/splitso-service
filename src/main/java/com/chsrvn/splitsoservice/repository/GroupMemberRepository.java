package com.chsrvn.splitsoservice.repository;

import com.chsrvn.splitsoservice.entity.Group;
import com.chsrvn.splitsoservice.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, UUID> {

    // Find all memberships for a specific group
    List<GroupMember> findByGroupId(UUID groupId);

    // Find all groups a specific user is part of
    List<GroupMember> findByUserId(UUID userId);

    // Check if a user is already in a group (to prevent duplicates)
    Optional<GroupMember> findByGroupIdAndUserId(UUID groupId, UUID userId);

    // Optimized query to fetch Group details directly for a user
    @Query("SELECT gm.group FROM GroupMember gm WHERE gm.userId = :userId")
    List<Group> findAllGroupsByUserId(@Param("userId") UUID userId);
}
