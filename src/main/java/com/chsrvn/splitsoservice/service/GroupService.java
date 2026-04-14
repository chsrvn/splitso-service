package com.chsrvn.splitsoservice.service;

import com.chsrvn.splitsoservice.entity.Group;
import com.chsrvn.splitsoservice.entity.GroupMember;
import com.chsrvn.splitsoservice.repository.GroupMemberRepository;
import com.chsrvn.splitsoservice.repository.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;

    public GroupService(GroupRepository groupRepository, GroupMemberRepository groupMemberRepository) {
        this.groupRepository = groupRepository;
        this.groupMemberRepository = groupMemberRepository;
    }

    public Group createGroup(Group group) {
        // Business Logic: Save group and automatically add creator as 'admin'
        Group savedGroup = groupRepository.save(group);

        GroupMember adminMember = new GroupMember();
        adminMember.setGroup(savedGroup);
        adminMember.setUserId(group.getCreatedBy());
        adminMember.setRole("admin");
        groupMemberRepository.save(adminMember);

        return savedGroup;
    }

    public Group updateGroup(UUID groupId, Group details, UUID userId) {
        validateAdminRole(groupId, userId);

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        group.setName(details.getName());
        group.setDescription(details.getDescription());
        group.setType(details.getType());

        return groupRepository.save(group);
    }

    public void deleteGroup(UUID groupId, UUID userId) {
        validateAdminRole(groupId, userId);
        groupRepository.deleteById(groupId);
    }

    private void validateAdminRole(UUID groupId, UUID userId) {
        GroupMember member = groupMemberRepository.findByGroupIdAndUserId(groupId, userId)
                .orElseThrow(() -> new RuntimeException("User is not a member of this group"));

        if (!"admin".equalsIgnoreCase(member.getRole())) {
            throw new RuntimeException("Access Denied: Only admins can perform this action");
        }
    }
}
